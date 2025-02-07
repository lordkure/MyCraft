package com.mycraft.domain;

import static org.lwjgl.opengl.GL30.*;

public class ChunkRenderer {

    private int vao, vbo;

    private static final float[] VERTICES = {
            // Frente
            0, 0, 0,    0,0,
            1, 0, 0,    1,0,
            1, 1, 0,    1,1,
            0, 1, 0,    0,1,
            // Trás
            1, 0, 1,    0,0,
            0, 0, 1,    1,0,
            0, 1, 1,    1,1,
            1, 1, 1,    0,1,
            // Esquerda
            0, 0, 1,    0,0,
            0, 0, 0,    1,0,
            0, 1, 0,    1,1,
            0, 1, 1,    0,1,
            // Direita
            1, 0, 0,    0,0,
            1, 0, 1,    1,0,
            1, 1, 1,    1,1,
            1, 1, 0,    0,1,
            // Cima
            0, 1, 0,    0,0,
            1, 1, 0,    1,0,
            1, 1, 1,    1,1,
            0, 1, 1,    0,1,
            // Baixo
            0, 0, 1,    0,0,
            1, 0, 1,    1,0,
            1, 0, 0,    1,1,
            0, 0, 0,    0,1
    };

    public void create() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, VERTICES, GL_STATIC_DRAW);

        // Configura a posição dos vértices (location = 0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Configura as coordenadas UV (location = 1)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0); // Ativa o atributo de posição
        glEnableVertexAttribArray(1); // Ativa o atributo UV

        glDrawArrays(GL_QUADS, 0, 24); // Desenha as 6 faces do cubo

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }
}

