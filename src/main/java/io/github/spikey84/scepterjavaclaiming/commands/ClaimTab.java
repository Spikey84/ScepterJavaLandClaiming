package io.github.spikey84.scepterjavaclaiming.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ClaimTab implements TabCompleter {
    String[] commands = new String[]{"help", "tools", "settings", "sethome", "home", "add", "remove", "blacklist", "unblacklist", "transfer", "claim"};

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
//        if (strings.length == 0) return Arrays.stream(commands).toList();
//        return null;
        return Arrays.stream(commands).toList();
    }
}
