package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.modelo.Ganhos;
import com.algaworks.junit.blog.modelo.Post;
import org.mockito.Mockito;

import java.math.BigDecimal;

public class PostTestData {

    private  PostTestData() {}

    public static Post.Builder umPostNovo() {
        return Post.builder()
                .comTitulo( "Olá mundo Java")
                .comConteudo("Olá Java com System.out.println")
                .comEditor(Mockito.spy(EditorTestData.umEditorComIdInexistente().build()))
                .comPago(true)
                .comPublicado(true);
    }

    public static Post.Builder umPostExistente() {
        return umPostNovo()
                .comId(1L)
                .comSlug("ola-mundo-java")
                .comGanhos(new Ganhos(BigDecimal.TEN, 4, BigDecimal.valueOf(10)));
    }

}
