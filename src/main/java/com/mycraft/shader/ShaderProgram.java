package com.mycraft.shader;

import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final int programId;

    public ShaderProgram(String vertexShaderCode, String fragmentShaderCode) {
        // Compila o vertex shader
        int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderId, vertexShaderCode);
        glCompileShader(vertexShaderId);
        if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Erro ao compilar vertex shader: " + glGetShaderInfoLog(vertexShaderId));
        }

        // Compila o fragment shader
        int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderId, fragmentShaderCode);
        glCompileShader(fragmentShaderId);
        if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Erro ao compilar fragment shader: " + glGetShaderInfoLog(fragmentShaderId));
        }

        // Cria o programa e vincula os shaders
        programId = glCreateProgram();
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Erro ao vincular o programa: " + glGetProgramInfoLog(programId));
        }

        // Limpa os shaders intermediários
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
    }

    public void use() {
        glUseProgram(programId);
    }

    public void cleanup() {
        glDeleteProgram(programId);
    }

    public void setUniformMatrix4f(String name, FloatBuffer matrix) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location != -1) {
            GL20.glUniformMatrix4fv(location, false, matrix);
        }
    }
}
