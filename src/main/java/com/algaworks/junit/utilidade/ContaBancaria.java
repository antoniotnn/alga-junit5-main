package com.algaworks.junit.utilidade;

import java.math.BigDecimal;

public class ContaBancaria {

    private BigDecimal saldo;
    private static final String VALOR_INVALIDO = "Valor Inválido.";

    public ContaBancaria(BigDecimal saldo) {
        this.saldo = saldo;
        if (this.saldo == null) throw new IllegalArgumentException(VALOR_INVALIDO);
    }

    public void saque(BigDecimal valor) {
        this.validarSaque(valor);
        this.saldo = this.saldo.subtract(valor);
    }

    public void deposito(BigDecimal valor) {
        this.validarValor(valor);
        this.saldo = this.saldo.add(valor);
    }

    public BigDecimal saldo() {
        return this.saldo;
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException(VALOR_INVALIDO);
    }

    private void validarSaque(BigDecimal valor) {
        this.validarValor(valor);
        if (this.saldo.compareTo(valor) < 0) throw new RuntimeException("Saldo Insuficiente");
    }
}
