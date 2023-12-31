package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.exception.EditorNaoEncontradoException;
import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class) // necessário ao usar Annotation @Mock apenas.
public class CadastroEditorComMockTest {

    @Captor // outra forma de usar o Argument Captor, via annotation, para fazer o mesmo do que está comentado logo mais abaixo.
    ArgumentCaptor<Mensagem> mensagemArgumentCaptor;
    @Mock
    ArmazenamentoEditor armazenamentoEditor;
    @Mock
    GerenciadorEnvioEmail gerenciadorEnvioEmail;
    @InjectMocks
    CadastroEditor cadastroEditor;


    @Nested
    class CadastroComEditorValido {

        @Spy
        Editor editor = EditorTestData.umEditorNovo().build();

        @BeforeEach
        void beforeEach() {
            Mockito.when(armazenamentoEditor.salvar(Mockito.any(Editor.class)))
                    .thenAnswer(invocacao -> {
                        Editor editorPassado = invocacao.getArgument(0, Editor.class);
                        editorPassado.setId(1L);
                        return editorPassado;
                    });

        }

        @Test
        void Dado_um_editor_valido_Quando_criar_Entao_deve_retornar_um_id_de_cadastro() {
            Editor editorSalvo = cadastroEditor.criar(editor);
            assertEquals(1L, editorSalvo.getId());
        }

        @Test
        void Dado_um_editor_valido_Quando_criar_Entao_deve_chamar_metodo_salvar_do_armazenamento() {
            cadastroEditor.criar(editor);

            // Verificando se o Método salvar foi chamado 1 vez nesse teste, com a instancia de "editor" passada.
            Mockito.verify(armazenamentoEditor, Mockito.times(1))
                    .salvar(Mockito.eq(editor));
        }

        @Test
        void Dado_um_editor_valido_Quando_criar_e_lancar_exception_ao_salvar_Entao_nao_deve_enviar_email() {
            Mockito.when(armazenamentoEditor.salvar(editor))
                    .thenThrow(new RuntimeException());

            assertAll(
                    () -> assertThrows(RuntimeException.class, () -> cadastroEditor.criar(editor)),
                    // Garantir que o método abaixo nunca tenha sido chamado nesse teste.
                    () -> Mockito.verify(gerenciadorEnvioEmail, Mockito.never()).enviarEmail(Mockito.any())
            );
        }

        @Test
        void Dado_um_editor_valido_Quando_cadastrar_Entao_deve_enviar_email_com_destino_ao_editor() {
            //Capturar argumento passado na execução do método, possui outra forma também via anottation
//        ArgumentCaptor<Mensagem> mensagemArgumentCaptor = ArgumentCaptor.forClass(Mensagem.class);

            Editor editorSalvo = cadastroEditor.criar(editor);

            Mockito.verify(gerenciadorEnvioEmail).enviarEmail(mensagemArgumentCaptor.capture());

            Mensagem mensagem = mensagemArgumentCaptor.getValue();

            assertEquals(editorSalvo.getEmail(), mensagem.getDestinatario());
        }

        @Test
        void Dado_um_editor_valido_Quando_cadastrar_Entao_deve_verificar_o_email() {
            //criar uma instancia de um objeto real que pode ser espionado, também pode ser feito via annotation.
//        Editor editorSpy = Mockito.spy(editor);
            cadastroEditor.criar(editor);
            //verifica se o getEmail() foi chamado pelo menos 1 vez. (aqui na verdade é chamado 2 vezes)
            Mockito.verify(editor, Mockito.atLeast(1)).getEmail();
        }

        // Alterando retorno do Mock após chamadas consecutivas
        @Test
        void Dado_um_editor_com_email_existente_Quando_cadastrar_Entao_deve_lancar_exception() {
            Mockito.when(armazenamentoEditor.encontrarPorEmail("antonio@teste.com"))
                    .thenReturn(Optional.empty())
                    .thenReturn(Optional.of(editor));

            Editor editorComEmailExistente = EditorTestData.umEditorNovo().build();
            cadastroEditor.criar(editor);
            assertThrows(RegraNegocioException.class, () -> cadastroEditor.criar(editorComEmailExistente));

        }

        //Verificando ordem de chamada de métodos
        @Test
        void Dado_um_editor_valido_Quando_cadastrar_Entao_deve_enviar_email_apos_salvar() {
            cadastroEditor.criar(editor);

            InOrder inOrder = Mockito.inOrder(armazenamentoEditor, gerenciadorEnvioEmail);
            inOrder.verify(armazenamentoEditor, Mockito.times(1)).salvar(editor);
            inOrder.verify(gerenciadorEnvioEmail, Mockito.times(1)).enviarEmail(Mockito.any(Mensagem.class));
        }
    }

    @Nested
    class CadastroComEditorNull {
        // 4.14. Entendendo problema de mocks não utilizados (UnnecessaryStubbingException)
        @Test
        void Dado_um_editor_null_Quando_cadastrar_Entao_deve_lancar_exception() {
            assertThrows(NullPointerException.class, () -> cadastroEditor.criar(null));
            Mockito.verify(armazenamentoEditor, Mockito.never()).salvar(Mockito.any());
            Mockito.verify(gerenciadorEnvioEmail, Mockito.never()).enviarEmail(Mockito.any());
        }
    }

    @Nested
    class EdicaoComEditorValido {

        @Spy
        Editor editor = EditorTestData.umEditorExistente().build();

        @BeforeEach
        void beforeEach() {
            Mockito.when(armazenamentoEditor.salvar(editor)).thenAnswer(invocacao -> invocacao.getArgument(0, Editor.class));
            Mockito.when(armazenamentoEditor.encontrarPorId(1L)).thenReturn(Optional.of(editor));
        }

        @Test
        void Dado_um_editor_valido_Quando_editar_Entao_deve_alterar_o_editor_salvo() {
            Editor editorAtualizado = EditorTestData.umEditorExistente()
                    .comEmail("antonio.pires@teste.com")
                    .comNome("Antonio Pires")
                    .build();

            cadastroEditor.editar(editorAtualizado);
            Mockito.verify(editor, Mockito.times(1)).atualizarComDados(editorAtualizado);

            InOrder inOrder = Mockito.inOrder(editor, armazenamentoEditor);
            inOrder.verify(editor).atualizarComDados(editorAtualizado);
            inOrder.verify(armazenamentoEditor).salvar(editor);
        }

    }

    @Nested
    class EdicaoComEditorInexistente {

        Editor editor = EditorTestData.umEditorComIdInexistente().build();

        @BeforeEach
        void beforeEach() {
            Mockito.when(armazenamentoEditor.encontrarPorId(99L)).thenReturn(Optional.empty());
        }

        @Test
        void Dado_um_editor_que_nao_exista_Quando_editar_Entao_deve_lancar_exception() {
            assertThrows(EditorNaoEncontradoException.class, () -> cadastroEditor.editar(editor));
            Mockito.verify(armazenamentoEditor, Mockito.never()).salvar(Mockito.any(Editor.class));
        }
    }

}
