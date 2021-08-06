package io.github.spikey84.scepterjavaclaiming.commands;

import io.github.spikey84.scepterjavaclaiming.utils.Tab;
import io.github.spikey84.scepterjavaclaiming.utils.TabSection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

//public class ClaimTab implements TabCompleter {
//    String[] commands = new String[]{"help", "tools", "settings", "add", "remove", "blacklist", "unblacklist", "transfer", "claim", "blocks"};
//
//    @Override
//    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
////        if (strings.length == 0) return Arrays.stream(commands).toList();
////        return null;
//        return Arrays.stream(commands).toList();
//    }
//}

public class ClaimTab extends Tab {

    public ClaimTab() {
        addSubCommand("help", TabSection.Type.END);
        addSubCommand("tools", TabSection.Type.END);
        addSubCommand("settings", TabSection.Type.END);
        addSubCommand("add", TabSection.Type.PLAYERS);
        addSubCommand("remove", TabSection.Type.PLAYERS);
        addSubCommand("blacklist", TabSection.Type.PLAYERS);
        addSubCommand("unblacklist", TabSection.Type.PLAYERS);
        addSubCommand("transfer", TabSection.Type.END);
        addSubCommand("claim", TabSection.Type.END);
        addSubCommand("blocks", TabSection.Type.END);

    }
}
