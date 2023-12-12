package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ContaBancariaTest {

    @Nested
    class Saque {

        @Test
        void deveRealizarSaque() {
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));
            assertDoesNotThrow(() -> contaBancaria.saque(new BigDecimal("500")));
            assertEquals(new BigDecimal("500"), contaBancaria.saldo());
        }

        @Test
        void deveLancarIllegalArgumentExceptionAoRealizarSaqueComValorZero() {
            assertThrows(RuntimeException.class, () -> {
                ContaBancaria contaBancaria = new ContaBancaria(BigDecimal.TEN);
                contaBancaria.saque(BigDecimal.ZERO);
            });
        }

        @Test
        void deveLancarIllegalArgumentExceptionAoRealizarSaqueComValorMenorQueZero() {
            assertThrows(RuntimeException.class, () -> {
                ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));
                contaBancaria.saque(BigDecimal.valueOf(-1L));
            });
        }

        @Test
        void deveLancarRunTimeExceptionSeSaldoInsuficienteAoRealizarSaque() {
            assertThrows(RuntimeException.class, () -> {
                ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));
                contaBancaria.saque(BigDecimal.valueOf(1500));
            });
        }

        @Test
        void deveRealizarSaqueComValorIgual() {
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));
            assertDoesNotThrow(() -> contaBancaria.saque(new BigDecimal("1000")));
            assertEquals(BigDecimal.ZERO, contaBancaria.saldo());
        }

        @Test
        void deveLancarIllegalArgumentExceptionAoRealizarSaqueComValorNulo() {
            assertThrows(IllegalArgumentException.class, () -> {
                ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));
                contaBancaria.saque(null);
            });
        }

    }

    @Nested
    class Deposito {

        @Test
        void deveRealizarDeposito() {
            ContaBancaria contaBancaria = new ContaBancaria(BigDecimal.TEN);
            assertDoesNotThrow(() -> contaBancaria.deposito(BigDecimal.TEN));
            assertEquals(new BigDecimal("20"), contaBancaria.saldo());
        }

        @Test
        void deveLancarIllegalArgumentExceptionAoRealizarDepositoComValorZero() {
            assertThrows(RuntimeException.class, () -> {
                ContaBancaria contaBancaria = new ContaBancaria(BigDecimal.TEN);
                contaBancaria.deposito(BigDecimal.ZERO);
            });
        }

        @Test
        void deveLancarIllegalArgumentExceptionAoRealizarDepositoComValorMenorQueZero() {
            assertThrows(RuntimeException.class, () -> {
                ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));
                contaBancaria.deposito(BigDecimal.valueOf(-1L));
            });
        }

        @Test
        void deveLancarIllegalArgumentExceptionAoRealizarDepositoComValorNulo() {
            assertThrows(IllegalArgumentException.class, () -> {
                ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));
                contaBancaria.deposito(null);
            });
        }
    }

    @Nested
    class Saldo {

        @Test
        void deveRealizarSaqueAposDeposito() {
            ContaBancaria contaBancaria = new ContaBancaria(BigDecimal.TEN);
            contaBancaria.deposito(BigDecimal.TEN);
            contaBancaria.saque(new BigDecimal("5"));
            assertEquals(new BigDecimal("15"), contaBancaria.saldo());
        }

        @Test
        void deveValidarSaldo() {
            ContaBancaria contaBancaria = new ContaBancaria(BigDecimal.TEN);
            assertEquals(BigDecimal.TEN, contaBancaria.saldo());
        }

        @Test
        void deveLancarIllegalArgumentExceptionAoCriarConta() {
            assertThrows(IllegalArgumentException.class, () -> new ContaBancaria(null));
        }

    }

}
