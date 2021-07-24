package io.github.spikey84.scepterjavaclaiming.commands;

import com.google.common.collect.Lists;
import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.ConfigManager;
import io.github.spikey84.scepterjavaclaiming.blocks.ClaimBlocksManager;
import io.github.spikey84.scepterjavaclaiming.homes.Home;
import io.github.spikey84.scepterjavaclaiming.homes.HomeManager;
import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import io.github.spikey84.scepterjavaclaiming.utils.ItemUtils;
import io.github.spikey84.scepterjavaclaiming.utils.Rectangle;
import jdk.jshell.spi.ExecutionControl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ClaimCommand implements CommandExecutor {
    private ConfigManager configManager;
    private ClaimManager claimManager;
    private HomeManager homeManager;
    private ClaimBlocksManager claimBlocksManager;

    public ClaimCommand(ConfigManager configManager, ClaimManager claimManager, ClaimBlocksManager claimBlocksManager, HomeManager homeManager) {
        this.configManager = configManager;
        this.claimManager = claimManager;
        this.claimBlocksManager = claimBlocksManager;
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run as a player!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("scepter.default")) {
            ChatUtil.message(player, "You do not have permission to run this command.");
            return true;
        }

        if (args.length < 1) {
            ChatUtil.message(player, "Please enter a sub command.");
            return true;
        }

        switch (args[0]) {
            case "help": help(player, args); break;
            case "tools": tools(player, args); break;
            case "settings": settings(player, args); break;
            case "sethome": setHome(player, args); break;
            case "home": home(player, args); break;
            case "add": add(player, args); break;
            case "remove": remove(player, args); break;
            case "blacklist": blacklist(player, args); break;
            case "unblacklist": unblacklist(player, args); break;
            case "transfer": transfer(player, args); break;
            case "claim": claim(player, args); break;
            default: claim(player, args); break;
        }

        return true;
    }

    public void help(Player player, String... args) {
        for (String line : configManager.getHelp()) {
            player.sendMessage(line);
        }
    }

    public void tools(Player player, String... args) {
        ChatUtil.message(player, "Claim tools given.");
        // TODO: COOLDOWN
        ItemUtils.giveItem(player, configManager.getClaimTool().clone());
        ItemUtils.giveItem(player, configManager.getClaimChecker().clone());
    }

    public void settings(Player player, String... args) {
        // TODO: GUI
        ChatUtil.message(player, "will open gui");
    }

    public void setHome(Player player, String... args) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) {
                continue;
            }
            if (!claim.getMembers().contains(player.getUniqueId())) {
                ChatUtil.message(player, "Home must be set in a claim that you are a member of.");
                continue;
            }

            homeManager.addHome(new Home(player.getLocation(), claim.getId()));
            ChatUtil.message(player, "Claim home has been set.");
            return;
        }
        ChatUtil.message(player, "Home must be set in a claim that you are a member of.");
    }

    public void home(Player player, String... args) {
        //TODO: DETRMINE WHICH HOME TO GO TO (MAYBE GUI)
        for (Claim claim : claimManager.getClaims()) {
            if (!claim.getOwner().equals(player.getUniqueId())) continue;

            if (!homeManager.hasHome(claim.getId())) continue;
            homeManager.getHome(claim.getId()).teleportToHome(player);
            ChatUtil.message(player, "You have been teleported to your claim home.");
            return;
        }
    }

    public void add(Player player, String... args) {
        if (args.length < 2) {
            ChatUtil.message(player, "Please enter a player to add to this claim.");
            return;
        }

        if (Bukkit.getPlayer(args[1]) == null) {
            ChatUtil.message(player, "This player is not online.");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) continue;
            if (claim.getMembers().contains(target.getUniqueId())) {
                ChatUtil.message(player, String.format("%s is already a member of this claim.", args[1]));
                return;
            }


            claim.addMember(target.getUniqueId());
            ChatUtil.message(player, String.format("%s has been added to this claim.", args[1]));
            return;
        }
        ChatUtil.message(player, "You must be in a claim to use this command");
    }

    public void remove(Player player, String... args) {
        if (args.length < 2) {
            ChatUtil.message(player, "Please enter a player to remove from this claim.");
            return;
        }

        if (Bukkit.getOfflinePlayerIfCached(args[1]) == null) {
            ChatUtil.message(player, "This player has never logged on.");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) continue;
            if (!claim.getMembers().contains(target.getUniqueId())) {
                ChatUtil.message(player, String.format("%s is not a member of this claim.", args[1]));
                return;
            }

            claim.removeMember(target.getUniqueId());
            ChatUtil.message(player, String.format("%s has been removed from your claim.", args[1]));
            return;
        }
        ChatUtil.message(player, "You must be in a claim to use this command");
    }

    public void blacklist(Player player, String... args) {
        if (args.length < 2) {
            ChatUtil.message(player, "Please enter a player to blacklist from this claim.");
            return;
        }

        if (Bukkit.getOfflinePlayerIfCached(args[1]) == null) {
            ChatUtil.message(player, "This player has never logged on.");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) continue;
            if (!claim.getOwner().equals(player.getUniqueId())) {
                ChatUtil.message(player, "You must be the owner of this claim to blacklist someone from it.");
                return;
            }
            if (claim.getBlackList().contains(target.getUniqueId())) {
                ChatUtil.message(player, String.format("%s is already blacklisted from this claim.", args[1]));
                return;
            }


            claim.addBlacklist(target.getUniqueId());
            ChatUtil.message(player, String.format("%s has been blacklisted from this claim.", args[1]));
            return;
        }
        ChatUtil.message(player, "You must be in a claim to use this command");
    }

    public void unblacklist(Player player, String... args) {
        if (args.length < 2) {
            ChatUtil.message(player, "Please enter a player to unblacklist from this claim.");
            return;
        }

        if (Bukkit.getOfflinePlayerIfCached(args[1]) == null) {
            ChatUtil.message(player, "This player has never logged on.");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) continue;
            if (!claim.getOwner().equals(player.getUniqueId())) {
                ChatUtil.message(player, "You must be the owner of this claim to unblacklist someone from it.");
                return;
            }
            if (!claim.getBlackList().contains(target.getUniqueId())) {
                ChatUtil.message(player, String.format("%s is not blacklisted from this claim.", args[1]));
                return;
            }

            claim.removeBlackList(target.getUniqueId());
            ChatUtil.message(player, String.format("%s has been removed from the blacklist of this claim.", args[1]));
            return;
        }
        ChatUtil.message(player, "You must be in a claim to use this command");
    }

    public void transfer(Player player, String... args) {
        if (args.length < 2) {
            ChatUtil.message(player, "Please enter a player to transfer this claim too.");
            return;
        }

        if (Bukkit.getOfflinePlayerIfCached(args[1]) == null) {
            ChatUtil.message(player, "This player has never logged on.");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) continue;
            if (!claim.getOwner().equals(player.getUniqueId())) {
                ChatUtil.message(player, "You must be the owner of this claim to transfer its ownership.");
                return;
            }

            claim.setOwner(target.getUniqueId());
            ChatUtil.message(player, String.format("%s has been given ownership of this claim.", args[1]));
            return;
        }
        ChatUtil.message(player, "You must be in a claim to use this command");
    }

    public void claim(Player player, String... args) {
        if (!claimManager.getTempClaiming().get(player.getUniqueId()).notNullLocations()) {
            ChatUtil.message(player, "Set a first and second location in order to claim.");
            return;
        }

        Rectangle rectangle = claimManager.getTempClaiming().get(player.getUniqueId());

        Claim claim = new Claim(rectangle.getLeftLocation(), rectangle.getXLength(), rectangle.getZLength(), player.getUniqueId(), Lists.newArrayList(),configManager.getDefaultSettings(), Lists.newArrayList());

//        for (Claim otherClaim : claimManager.getClaims()) {
//            if (claim.overlaps(otherClaim)) {
//                ChatUtil.message(player, "This claim overlaps an existing claim.");
//                return;
//            }
//        }

        if (rectangle.getSize() > claimBlocksManager.getBlockCount(player.getUniqueId())) {
            ChatUtil.message(player, "You do not have enough claim blocks. (%s required)".formatted(rectangle.getSize()));
            return;
        }

        if (configManager.getDisabledWorlds().contains(claim.getOrigin().getWorld().getName())) {
            ChatUtil.message(player, "Claiming is not allowed in this world.");
            return;
        }

        claimManager.addClaim(claim);
        claimManager.addMember(player.getUniqueId(), claim);
        ChatUtil.message(player, "Area claimed!");
    }
}
