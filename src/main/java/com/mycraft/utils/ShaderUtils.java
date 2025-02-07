package com.mycraft.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ShaderUtils {
    public static String loadShader(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar shader: " + filePath, e);
        }
    }
}