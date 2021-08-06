package io.github.spikey84.scepterjavaclaiming.utils;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;

import java.util.HashMap;

public class TabSection {
    private HashMap<String, TabSection> subCommands;
    private Type type;
    public TabSection(Type type) {
        subCommands = Maps.newHashMap();

        this.type = type;
    }

    public void addSubCommand(String string, Type type) {
        subCommands.put(string, new TabSection(type));
    }

    public TabSection getTabSection(String string) {
        if (subCommands.containsKey(string)) return null;
        return subCommands.get(string);
    }

    public String[] getSubcommands() {
        if (type.equals(Type.SECTION)) return subCommands.keySet().toArray(new String[0]);
        if (type.equals(Type.END)) return new String[]{};
        return null;
    }

    public enum Type {
        SECTION(),
        PLAYERS(),
        END()
    }
}





