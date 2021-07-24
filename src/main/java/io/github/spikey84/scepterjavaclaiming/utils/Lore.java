package io.github.spikey84.scepterjavaclaiming.utils;

import java.util.Arrays;
import java.util.List;

public class Lore {
    private List<String> contents;

    public Lore(String... args) {
        contents = Arrays.stream(args).toList();
    }

    public List<String> getContents() {
        return contents;
    }

    public static Lore createLore(String... args) {
        return new Lore(args);
    }

}
