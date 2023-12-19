package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {

    @Test
    void assercaoAgrupada() {
        Pessoa pessoa = new Pessoa("Antonio", "Pires");

        assertAll("Asserções de pessoa",
                () -> assertEquals("Antonio", pessoa.getNome()),
                () -> assertEquals("Pires", pessoa.getSobrenome()));
    }
}
