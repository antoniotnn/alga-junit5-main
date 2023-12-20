package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class) // necessário ao usar Annotation @Mock apenas.
public class CadastroEditorComMockTest {

    Editor editor;
    @Mock
    ArmazenamentoEditor armazenamentoEditor;
    @Mock
    GerenciadorEnvioEmail gerenciadorEnvioEmail;
    @InjectMocks // para injetar os Mocks acima por por parâmetro no construtor do CadastroEditor na sua criação.
    CadastroEditor cadastroEditor;


    @BeforeEach
    void beforeEach() {
        editor = new Editor(null, "Antonio", "antonio@teste.com", BigDecimal.TEN, true);

        //ArmazenamentoEditor armazenamentoEditor = Mockito.mock(ArmazenamentoEditor.class);
//        Mockito.when(armazenamentoEditor.salvar(editor))
//                .thenReturn(new Editor(1L, "Antonio", "antonio@teste.com", BigDecimal.TEN, true));

        // Uma forma de não especificar a instância do editor específica e sim Qualquer Uma que for passada. (Parâmetro Dinâmico)
        //Mockito.when(armazenamentoEditor.salvar(Mockito.any(Editor.class)))
        Mockito.when(armazenamentoEditor.salvar(editor)) // outra forma de customizar o retorno do método da classe (Editor) que foi invocada.
            .thenAnswer(invocacao -> {
                Editor editorPassado = invocacao.getArgument(0, Editor.class);
                editorPassado.setId(1L);
                return editorPassado;
            });

        //GerenciadorEnvioEmail gerenciadorEnvioEmail = Mockito.mock(GerenciadorEnvioEmail.class);

        //cadastroEditor = new CadastroEditor(armazenamentoEditor, gerenciadorEnvioEmail); // comentado pois o @InjectMocks faz esse trabalho.
    }

    @Test
    void Dado_um_editor_valido_Quando_criar_Entao_deve_retornar_um_id_de_cadastro() {
        Editor editorSalvo = cadastroEditor.criar(editor);
        assertEquals(1L, editorSalvo.getId());
    }
}
