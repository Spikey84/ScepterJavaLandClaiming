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


public class ClaimTab extends Tab {

    public ClaimTab() {
        addSubCommand("help", TabSection.Type.END);
        addSubCommand("tools", TabSection.Type.END);
        addSubCommand("settings", TabSection.Type.END);
        addSubCommand("add", TabSection.Type.PLAYERS);
        addSubCommand("remove", TabSection.Type.PLAYERS);
        addSubCommand("transfer", TabSection.Type.END);
        addSubCommand("claim", TabSection.Type.END);

    }
}
