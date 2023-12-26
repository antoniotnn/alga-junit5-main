package com.algaworks.junit.utilidade;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static com.algaworks.junit.utilidade.SaudacaoUtil.saudar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes no utilitário de saudação")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SaudacaoUtilTest {

    private static final String SAUDACAO_INCORRETA = "Saudação incorreta!";

    @Test
    @DisplayName("Deve saudar com Bom dia")
    void saudarComBomDia() { //Organizar teste com Padrão AAA - Arrange - Act - Assert
        //Arrange
        int horaValida = 9;

        //Act
        String saudacao = saudar(horaValida);

        //Assert
        assertEquals("Bom dia", saudacao);
    }

    @Test
    @DisplayName("Deve saudar com Bom dia usando AssertJ")
    void saudarComBomDiaUsandoAssertJ() { //Organizar teste com Padrão AAA - Arrange - Act - Assert
        int horaValida = 9;

        String saudacao = saudar(horaValida);

        //AssertJ
        assertThat(saudacao)
//                .withFailMessage("Saudação incorreta!")
//                .isNotNull()
//                .isNotBlank()
                .isEqualTo("Bom dia");
    }

    @Test
    @DisplayName("Deve saudar com Bom dia, recebendo números aleatórios dentro da range")
    public void deveSaudarBomDia() {
        //Arrange
        Random faixaBomDia = new Random();
        int numeroAleatorioDentroDaFaixaBomDia = faixaBomDia.nextInt(5, 12);

        //Act
        String saudacao = saudar(numeroAleatorioDentroDaFaixaBomDia);

        //Assert
        assertEquals("Bom dia", saudacao, SAUDACAO_INCORRETA);
    }

    @Test
    @DisplayName("Deve saudar com Bom dia a Partir das 5h")
    public void saudarComBomDiaAPartir5h() {
        int horaValida = 5;
        String saudacao = saudar(horaValida);
        assertEquals("Bom dia", saudacao, SAUDACAO_INCORRETA);
    }

    @Test
//    @DisplayName("Deve saudar com Boa tarde")
    public void Dado_um_horario_vespertino_Quando_saudar_Entao_deve_retornar_boa_tarde() {
        Random faixaBoaTarde = new Random();
        int numeroAleatorioDentroDaFaixaBoaTarde = faixaBoaTarde.nextInt(12, 18);
        String saudacao = saudar(numeroAleatorioDentroDaFaixaBoaTarde);
        assertEquals("Boa tarde", saudacao, SAUDACAO_INCORRETA);
    }

    @Test
    @DisplayName("Deve saudar com Boa noite")
    public void deveSaudarBoaNoite() {
        Random faixaBoaNoite = new Random();
        int numeroAleatorioDentroDaFaixaBoaNoite = faixaBoaNoite.nextInt(18, 24);
        String saudacao = saudar(numeroAleatorioDentroDaFaixaBoaNoite);
        assertEquals("Boa noite", saudacao, SAUDACAO_INCORRETA);
    }

    @Test
//    @DisplayName("Deve saudar com Boa noite as 4h")
    public void Dado_um_horario_noturno_Quando_saudar_Entao_deve_retornar_boa_noite() {
        int horaValida = 4;
        String saudacao = saudar(horaValida);
        assertEquals("Boa noite", saudacao, SAUDACAO_INCORRETA);
    }

    @Test
//    @DisplayName("Deve Lançar Exception")
    public void Dado_uma_hora_invalida_Quando_saudar_Entao_deve_lancar_exception() {
        // Arrange
        int horaInvalida = -10;

        //Act
        Executable chamadaInvalidaDeMetodo = () -> saudar(horaInvalida);

        //Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, chamadaInvalidaDeMetodo);
        assertEquals("Hora inválida", e.getMessage());
    }

    @Test
    public void Dado_uma_hora_invalida_Quando_saudar_Entao_deve_lancar_exception_usando_AssertJ() {
        int horaInvalida = -10;
//        IllegalArgumentException e = Assertions.catchThrowableOfType(() -> saudar(horaInvalida), IllegalArgumentException.class);
//        assertThat(e).hasMessage("Hora inválida");
        Assertions.assertThatThrownBy(() -> saudar(horaInvalida))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hora inválida");
    }

    @Test
//    @DisplayName("Não deve lançar Exception")
    public void Dado_uma_hora_invalida_Quando_saudar_Entao_nao_deve_lancar_exception() {
        //Arrange
        int horaValida = 0;

        //Act
        Executable chamadaValidaDeMetodo = () -> saudar(horaValida);

        //Assert
        assertDoesNotThrow(chamadaValidaDeMetodo);
    }

    @ParameterizedTest
    @ValueSource(ints = {5,6,7,8,9,10,11})
    public void Dado_horario_matinal_Quando_saudar_Entao_deve_retornar_bom_dia(int hora) {
        String saudacao = saudar(hora);
        assertEquals("Bom dia", saudacao);
    }

}
