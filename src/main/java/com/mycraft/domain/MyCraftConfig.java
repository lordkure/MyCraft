package com.mycraft.domain;

import lombok.ToString;

@ToString
public class MyCraftConfig {

    public Video video;

    @ToString
    public static class Video {
        public int width;
        public int height;
    }

}
