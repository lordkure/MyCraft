package com.mycraft.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;

public final class MainConfig {

    public static MyCraftConfig load() {

        var yaml = new Yaml();
        try (var inputStream = MainConfig.class.getClassLoader().getResourceAsStream("config.yaml")) {

            var config = yaml.loadAs(inputStream, MyCraftConfig.class);

            System.out.println(config);

            return config;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
