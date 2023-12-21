package com.algaworks.junit.blog.utilidade;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ConversorSlugTest {

    // Usando mock em métodos estáticos (substituir no pom.xml mockito-core por mockito-inline
    @Test
    void deveConverterJuntoComCodigo() {
        try (MockedStatic<GeradorCodigo> mockedStatic = Mockito.mockStatic(GeradorCodigo.class)) {
            mockedStatic.when(GeradorCodigo::gerar).thenReturn("123456");

            String slug = ConversorSlug.converterJuntoComCodigo("olá mundo java");
            assertEquals("ola-mundo-java-123456", slug);
        }
    }

}
