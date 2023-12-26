package com.algaworks.junit.ecommerce;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Carrinho de Compras")
class CarrinhoCompraTest {

    @Nested
    @DisplayName("Dado um carrinho vazio de um cliente")
    class CarrinhoCompraItensVazio {

        private CarrinhoCompra carrinhoCompra;

        @BeforeEach
        void beforeEach() {
            Cliente cliente = new Cliente(1L, "Antonio Pires");
            carrinhoCompra = new CarrinhoCompra(cliente);
        }

        @Nested
        @DisplayName("Quando adicionar produto de quantidade maior que zero")
        class AdicaoProdutoQuantidadeMaiorZero {

            int numeroAleatorioMaiorQueZero = new Random().nextInt(1,  Integer.MAX_VALUE) + 1;

            private Produto produto = new Produto(
                    1L, "Iphone X", "Smartphone da Apple", new BigDecimal("3000"));

            @Test
            @DisplayName("Então não deve lançar exception")
            void deveAdicionarProdutoSemLancarException() {
                assertDoesNotThrow(() -> carrinhoCompra.adicionarProduto(produto, numeroAleatorioMaiorQueZero));
            }

            @Test
            @DisplayName("E deve atualizar carrinho corretamente")
            void deveAtualizarCarrinhoCorretamente() {
                carrinhoCompra.adicionarProduto(produto, numeroAleatorioMaiorQueZero);
                assertEquals(numeroAleatorioMaiorQueZero, carrinhoCompra.getItens().get(0).getQuantidade());
            }

        }

        @Nested
        @DisplayName("Quando adicionar produto de quantidade menor que zero")
        class AdicaoProdutoQuantidadeMenorZero {

            private Produto produto = new Produto(
                    1L, "Iphone X", "Smartphone da Apple", new BigDecimal("3000"));

            int numeroAleatorioMenorQueZero = (new Random().nextInt(1,  Integer.MAX_VALUE) + 1) * (-1);

            @Test
            @DisplayName("Então deve lançar exception")
            void deveLancarExceptionAoTentarAdicionarProduto() {
                assertThrows(IllegalArgumentException.class,
                        () -> carrinhoCompra.adicionarProduto(produto, numeroAleatorioMenorQueZero));
            }

            @Test
            @DisplayName("E não deve atualizar carrinho")
            void naoDeveAtualizarCarrinho() {
                try {
                    carrinhoCompra.adicionarProduto(produto, numeroAleatorioMenorQueZero);
                } catch (IllegalArgumentException e) {
                    assertTrue(carrinhoCompra.getItens().isEmpty());
                }
            }

        }

    }

    @Nested
    @DisplayName("Dado um carrinho de um cliente com duas unidades de um produto")
    class CarrinhoCompraItensProdutoComDuasUnidades {

        private CarrinhoCompra carrinhoCompra;
        private Produto produto;

        @BeforeEach
        void beforeEach() {
            Cliente cliente = new Cliente(1L, "Antonio Pires");
            carrinhoCompra = new CarrinhoCompra(cliente);
            produto = new Produto(
                    1L, "Iphone X", "Smartphone da Apple", new BigDecimal("3000"));
            carrinhoCompra.adicionarProduto(produto, 2);
        }

        @Nested
        @DisplayName("Quando aumentar quantidade de um produto existente")
        class AumentarQuantidadeProdutoExistente {

            @Test
            @DisplayName("Então não deve lançar Exception")
            void deveAumentarQuantidadeProdutoExistenteSemLancarRuntimeException() {
                assertDoesNotThrow(() -> carrinhoCompra.aumentarQuantidadeProduto(produto));
            }

            @Test
            @DisplayName("E deve atualizar carrinho corretamente")
            void deveAtualizarCarrinhoCorretamente() {
                carrinhoCompra.aumentarQuantidadeProduto(produto);
                assertEquals(3, carrinhoCompra.getItens().get(0).getQuantidade());
            }

        }

        @Nested
        @DisplayName("Quando aumentar quantidade de um produto não existente")
        class AumentarQuantidadeProdutoNaoExistente {

            Produto produtoInexistente;

            @BeforeEach
            void beforeEach() {
                produtoInexistente = new Produto(
                        2L, "PS5", "Console da Sony", new BigDecimal("4000"));
            }

            @Test
            @DisplayName("Então deve lançar exception")
            void deveLancarExceptionAoTentarAdicionarProdutoNaoExistente() {
                assertThrows(RuntimeException.class,
                        () -> carrinhoCompra.aumentarQuantidadeProduto(produtoInexistente));
            }

            @Test
            @DisplayName("E não deve atualizar carrinho")
            void naoDeveAtualizarCarrinho() {
                try {
                    carrinhoCompra.aumentarQuantidadeProduto(produtoInexistente);
                } catch (RuntimeException e) {
                    assertEquals(2, carrinhoCompra.getItens().get(0).getQuantidade());
                }
            }

        }

        @Nested
        @DisplayName("Quando diminuir quantidade de um produto existente e não zerar a quantidade")
        class DiminuirQuantidadeProdutoExistente {

            @Test
            @DisplayName("Então não deve lançar Exception")
            void deveDiminuirQuantidadeProdutoExistenteSemLancarRuntimeException() {
                assertDoesNotThrow(() -> carrinhoCompra.diminuirQuantidadeProduto(produto));
            }

            @Test
            @DisplayName("E deve atualizar carrinho corretamente")
            void deveAtualizarCarrinhoCorretamente() {
                carrinhoCompra.diminuirQuantidadeProduto(produto);
                assertEquals(1, carrinhoCompra.getItens().get(0).getQuantidade());
            }

        }

        @Nested
        @DisplayName("Quando diminuir quantidade de um produto existente, e zerar a quantidade")
        class DiminuirQuantidadeProdutoExistenteZerarQuantidade {

            int quantidadeInicialCarrinho;

            @BeforeEach
            void beforeEach() {
                quantidadeInicialCarrinho = carrinhoCompra.getItens().get(0).getQuantidade();
            }

            @Test
            @DisplayName("Então não deve lançar Exception")
            void deveDiminuirQuantidadeProdutoExistenteSemLancarRuntimeException() {
                assertDoesNotThrow(() -> {
                    IntStream.range(0, quantidadeInicialCarrinho)
                            .forEach(i -> carrinhoCompra.diminuirQuantidadeProduto(produto));
                });
            }

            @Test
            @DisplayName("E deve atualizar carrinho corretamente")
            void deveAtualizarCarrinhoCorretamente() {
                IntStream.range(0, quantidadeInicialCarrinho)
                        .forEach(i -> carrinhoCompra.diminuirQuantidadeProduto(produto));
                assertTrue(carrinhoCompra.getItens().isEmpty());
            }
        }

        @Nested
        @DisplayName("Quando diminuir quantidade de um produto não existente")
        class DiminuirQuantidadeProdutoNaoExistente {

            Produto produtoInexistente;

            @BeforeEach
            void beforeEach() {
                produtoInexistente = new Produto(
                        2L, "PS5", "Console da Sony", new BigDecimal("4000"));
            }

            @Test
            @DisplayName("Então deve lançar exception")
            void deveLancarExceptionAoTentarDiminuirProdutoNaoExistente() {
                assertThrows(RuntimeException.class,
                        () -> carrinhoCompra.diminuirQuantidadeProduto(produtoInexistente));
            }

            @Test
            @DisplayName("E não deve atualizar carrinho")
            void naoDeveAtualizarCarrinho() {
                try {
                    carrinhoCompra.diminuirQuantidadeProduto(produtoInexistente);
                } catch (RuntimeException e) {
                    assertEquals(2, carrinhoCompra.getItens().get(0).getQuantidade());
                }
            }

        }

        @Nested
        @DisplayName("Quando remover um produto existente")
        class RemoverProdutoExistente {

            @Test
            @DisplayName("Então não deve lançar Exception")
            void deveRemoverProdutoExistenteSemLancarRuntimeException() {
                assertDoesNotThrow(() -> carrinhoCompra.removerProduto(produto));
            }

            @Test
            @DisplayName("E deve atualizar carrinho corretamente")
            void deveAtualizarCarrinhoCorretamente() {
                carrinhoCompra.removerProduto(produto);
                assertTrue(carrinhoCompra.getItens().isEmpty());
            }

        }

    }

    @Nested
    @DisplayName("Dado um carrinho de um cliente com duas unidades de um produto e três de outro")
    class CarrinhoCompraItensProdutoComDuasUnidadesOutroComTres {

        private CarrinhoCompra carrinhoCompra;
        private Produto iphoneX;
        private Produto ps5;

        @BeforeEach
        void beforeEach() {
            Cliente cliente = new Cliente(1L, "Antonio Pires");
            carrinhoCompra = new CarrinhoCompra(cliente);
            iphoneX = new Produto(
                    1L, "Iphone X", "Smartphone da Apple", new BigDecimal("3000"));
            ps5 = new Produto(
                    2L, "PS5", "Console da Sony", new BigDecimal("4000"));
            carrinhoCompra.adicionarProduto(iphoneX, 2);
            carrinhoCompra.adicionarProduto(ps5, 3);
        }

        @Test
        @DisplayName("E Deve Calcular Valor total da Compra")
        void deveCalcularValorTotal() {
            assertEquals(new BigDecimal("18000"), carrinhoCompra.getValorTotal());
        }

        @Test
        @DisplayName("E Deve Calcular Quantidade total de Produtos")
        void deveCalcularValorTotalDeProdutos() {
            assertEquals(5, carrinhoCompra.getQuantidadeTotalDeProdutos());
        }

        @Test
        @DisplayName("E Deve Esvaziar Carrinho")
        void deveEsvaziarCarrinho() {
            carrinhoCompra.esvaziar();
            assertTrue(carrinhoCompra.getItens().isEmpty());
        }

        @Test
        @DisplayName("E deve retornar uma nova instância da lista de itens")
        void eDeveRetornarUmaNovaLista() {
            carrinhoCompra.getItens().clear(); //Get Itens, retorna uma nova lista
            assertEquals(2, carrinhoCompra.getItens().size()); //Lista permaneceu intacta
        }

        @Test
        @DisplayName("Então deve conter apenas produtos adicionados")
        void entaoDeveConterApenasProdutosAdicionados() {
            Produto xboxSeriesX;
            xboxSeriesX = new Produto(
                    3L, "XBOX SERIES X", "Console da Microsoft", new BigDecimal("4000"));

            Assertions.assertThat(carrinhoCompra.getItens()).flatMap(ItemCarrinhoCompra::getProduto)
                    .contains(iphoneX, ps5)
                    .doesNotContain(xboxSeriesX);
        }

    }

}
