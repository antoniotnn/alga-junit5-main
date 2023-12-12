package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class SimuladorEsperaTest {

    /*
        Ignorando execução dos Testes:
        @Disabled : desativa o teste sem comentar

        Ignorando execução condicionalmennte com Assumptions:
        1- Ir em Edit Configurations (com a Classe que está o Teste, selecionada na config do IntelliJ, posteriormente abrirá config do JUNit.
        2- Ir em Environment variables e passar uma variável de ambiente (ENV=PROD) por exemplo.
     */


    @Test
    //@Disabled("Não é mais aplicável") /* Para desabilitar um teste sem comentar */
    @EnabledIfEnvironmentVariable(named = "ENV", matches = "DEV") /* outra forma de desabilitar, que não é tão comum */
    public void deveEsperarENaoDarTimeout() {
        //Assumptions , para desabilitar condicionalmente:
        //Assumptions.assumeTrue("PRDO".equals(System.getenv("ENV")), () -> "Abortando teste. Não deve ser executado em PROD.");


        // Garante que o método será executado em até 1 segundo (primeiro argumento). Segundo Argumento: ação que será executado. (expressão)
//        Assertions.assertTimeout(Duration.ofSeconds(1), () -> SimuladorEspera.esperar(Duration.ofSeconds(10)));

        // Este outro não espera que passe o tempo completo declarado de execução. Já falha assim que ultrapassar o tempo esperado limite.
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> SimuladorEspera.esperar(Duration.ofMillis(10)));

    }
}
