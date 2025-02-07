package com.mycraft.domain;

public class Chunk {
    public static final int SIZE = 16; // Dimensão do chunk (16x16x16)
    private final int[][][] blocks = new int[SIZE][SIZE][SIZE]; // Matriz de blocos

    public Chunk() {
        // Gerando um chão simples (nível 5 de altura)
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                blocks[x][5][z] = 1; // Define blocos como "terra"
            }
        }
    }

    public int getBlock(int x, int y, int z) {
        return blocks[x][y][z]; // Retorna o tipo de bloco
    }

    public void setBlock(int x, int y, int z, int type) {
        blocks[x][y][z] = type; // Define um bloco no mundo
    }
}

