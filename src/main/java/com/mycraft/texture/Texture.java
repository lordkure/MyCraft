package com.mycraft.texture;

import com.mycraft.config.MainConfig;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL30.*;

public class Texture {
    private int id;

    public Texture(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            // Carrega a imagem usando STBImage
            var file = new File("build/resources/main" + path);
            var imagePath = file.getAbsolutePath();

            System.out.println(imagePath);

            if (!file.exists())
                throw  new RuntimeException("erro ao carregar o arquivo");

            ByteBuffer image = STBImage.stbi_load(imagePath, width, height, channels, 4); // Força RGBA (4 canais)
            if (image == null) {
                throw new RuntimeException("Erro ao carregar textura: " + STBImage.stbi_failure_reason());
            }

            // Gera um Texture Object
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            // Envia a textura para a GPU
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            // Configura os parâmetros da textura
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            // Libera a memória da imagem na CPU
            STBImage.stbi_image_free(image);
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void cleanup() {
        glDeleteTextures(id);
    }
}

