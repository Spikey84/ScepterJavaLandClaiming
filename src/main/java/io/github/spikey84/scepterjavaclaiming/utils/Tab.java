package io.github.spikey84.scepterjavaclaiming.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Tab implements TabCompleter {
    private HashMap<String, TabSection> subCommands;
    private StringUtil stringUtil;

    public Tab() {
        subCommands = Maps.newHashMap();
        stringUtil = new StringUtil();
    }

    public void addSubCommand(String string, TabSection.Type type) {
        subCommands.put(string, new TabSection(type));
    }

    public TabSection getTabSection(String string) {
        if (subCommands.containsKey(string)) return null;
        return subCommands.get(string);
    }

    public String[] getSubcommands() {
        return subCommands.keySet().toArray(new String[0]);
    }



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) return StringUtil.copyPartialMatches(args[0], Arrays.asList(getSubcommands().clone()), new ArrayList<>());

//        TabSection tabSection = getTabSection(args[0]);
//
////        for (int x = 1; x < args.length; x++) {
////            tabSection = tabSection.getTabSection(args[x]);
////        }
//
//        return StringUtil.copyPartialMatches(args[args.length-1], Arrays.asList(tabSection.getSubcommands().clone()), new ArrayList<>());
        return null;

    }
}
