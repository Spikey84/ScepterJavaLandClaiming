package io.github.spikey84.scepterjavaclaiming.commands;

import com.google.common.collect.Lists;
import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.ConfigManager;
import io.github.spikey84.scepterjavaclaiming.EconomyManager;
import io.github.spikey84.scepterjavaclaiming.blocks.ClaimBlocksManager;
import io.github.spikey84.scepterjavaclaiming.cooldowns.CooldownManager;
import io.github.spikey84.scepterjavaclaiming.cooldowns.CooldownType;
import io.github.spikey84.scepterjavaclaiming.homes.HomeManager;
import io.github.spikey84.scepterjavaclaiming.settings.SettingsInventory;
import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import io.github.spikey84.scepterjavaclaiming.utils.ItemUtils;
import io.github.spikey84.scepterjavaclaiming.utils.Rectangle;
import io.github.spikey84.scepterjavaclaiming.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;

public class ClaimCommand implements CommandExecutor {
    private ConfigManager configManager;
    private ClaimManager claimManager;
    private HomeManager homeManager;
    private ClaimBlocksManager claimBlocksManager;
    private Plugin plugin;
    private CooldownManager cooldownManager;
    private EconomyManager economyManager;

    public ClaimCommand(ConfigManager configManager, ClaimManager claimManager, ClaimBlocksManager claimBlocksManager, HomeManager homeManager, Plugin plugin, CooldownManager cooldownManager, EconomyManager economyManager) {
        this.configManager = configManager;
        this.claimManager = claimManager;
        this.claimBlocksManager = claimBlocksManager;
        this.homeManager = homeManager;
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run as a player!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("claiming.default")) {
            ChatUtil.message(player, "You do not have permission to run this command.");
            return true;
        }

        if (args.length < 1) {
            claim(player, args);
            return true;
        }

        switch (args[0]) {
            case "help": help(player, args); break;
            case "tools": tools(player, args); break;
            case "settings": settings(player, args); break;
            case "add": add(player, args); break;
            case "remove": remove(player, args); break;
            case "transfer": transfer(player, args); break;
            case "claim": claim(player, args); break;
            //case "edit": edit(player, args); break;
            default: claim(player, args); break;
        }

        return true;
    }

//    public void edit(Player player, String... args) {
//
//        if (!cooldownManager.isExpired(player.getUniqueId(), CooldownType.EDIT.getId(), configManager.getToolCooldown() * 60)) {
//            ChatUtil.message(player, "You cannot run this command for another %s minute(s).".formatted((int) ((cooldownManager.getRemainingMinutes(player.getUniqueId(), CooldownType.TOOLS.getId(), configManager.getToolCooldown() * 60)))));
//            return;
//        }
//
//        if (!claimManager.getTempClaiming().get(player.getUniqueId()).notNullLocations()) {
//            ChatUtil.message(player, "Set a first and second location in order to claim.");
//            return;
//        }
//
//        Rectangle rectangle = claimManager.getTempClaiming().get(player.getUniqueId());
//
//        Claim claim = new Claim(rectangle.getBottomLeftLocation(), rectangle.getXLength(), rectangle.getZLength(), player.getUniqueId(), Lists.newArrayList(),configManager.getDefaultSettings(), Lists.newArrayList(), rectangle.getLeftLocation().getWorld().toString());
//
//        Claim claimToOverride = null;
//
//        for (Claim otherClaim : claimManager.getClaims()) {
//            Location close = otherClaim.getOrigin().clone();
//            Location far = otherClaim.getOrigin().clone();
//            far.setX(far.getX() + otherClaim.getXLength());
//            far.setZ(far.getZ() + otherClaim.getZLength());
//            Location farX = otherClaim.getOrigin().clone();
//            farX.setX(far.getX() + otherClaim.getXLength());
//            Location farZ = otherClaim.getOrigin().clone();
//            farX.setZ(far.getZ() + otherClaim.getZLength());
//            if (!rectangle.getLocation1().equals(close) && !rectangle.getLocation1().equals(far) && !rectangle.getLocation1().equals(farX) && !rectangle.getLocation1().equals(farZ) && !rectangle.getLocation2().equals(close) && !rectangle.getLocation2().equals(far) && !rectangle.getLocation2().equals(farX) && !rectangle.getLocation2().equals(farZ)) {
//                continue;
//            }
//            claimToOverride = otherClaim;
//
//        }
//
//        if (claimToOverride == null) {
//            ChatUtil.message(player, "Please select the corner of a claim to edit.");
//            return;
//        }
//
//        for (Claim otherClaim : claimManager.getClaims()) {
//            if (claim.overlaps(otherClaim) && !otherClaim.equals(claimToOverride)) {
//                ChatUtil.message(player, "This claim overlaps an existing claim.");
//                return;
//            }
//        }
//
//        if (rectangle.getSize() - (claimToOverride.getXLength() * claimToOverride.getZLength()) > claimBlocksManager.getBlockCount(player.getUniqueId())) {
//            ChatUtil.message(player, "You do not have enough claim blocks. (%s required)".formatted(rectangle.getSize() - (claimToOverride.getXLength() * claimToOverride.getZLength())));
//            return;
//        }
//
//        if (configManager.getDisabledWorlds().contains(claim.getOrigin().getWorld().getName())) {
//            ChatUtil.message(player, "Claiming is not allowed in this world.");
//            return;
//        }
//
//        claimManager.delClaim(claimToOverride);
//        claim = new Claim(rectangle.getBottomLeftLocation(), rectangle.getXLength(), rectangle.getZLength(), player.getUniqueId(),claimToOverride.getMembers(), claimToOverride.getClaimSettings(), claimToOverride.getBlackList(), claim.getWorldName());
//        claimManager.addClaim(claim);
//        cooldownManager.updateCooldown(player.getUniqueId(), CooldownType.EDIT.getId(), Timestamp.from(Instant.now()));
//        claimBlocksManager.setBlockCount(player.getUniqueId(), claimBlocksManager.getBlockCount(player.getUniqueId()) + rectangle.getSize() - (claimToOverride.getXLength() * claimToOverride.getZLength()));
//        ChatUtil.message(player, "Area claimed!");
//    }


    public void help(Player player, String... args) {
        for (String line : configManager.getHelp()) {
            player.sendMessage(StringUtils.formatColors(line));
        }
    }

    public void tools(Player player, String... args) {

        if (!cooldownManager.isExpired(player.getUniqueId(), CooldownType.TOOLS.getId(), configManager.getToolCooldown() * 60)) {
            ChatUtil.message(player, "You cannot run this command for another %s hour(s).".formatted((int) ((cooldownManager.getRemainingMinutes(player.getUniqueId(), CooldownType.TOOLS.getId(), configManager.getToolCooldown() * 60))/60)));
            return;
        }

        ChatUtil.message(player, "Claim tools given.");
        cooldownManager.updateCooldown(player.getUniqueId(), CooldownType.TOOLS.getId(), Timestamp.from(Instant.now()));
        ItemUtils.giveItem(player, configManager.getClaimTool().clone());
        ItemUtils.giveItem(player, configManager.getClaimChecker().clone());
    }

    public void settings(Player player, String... args) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) {
                continue;
            }

            if (!claim.getMembers().contains(player.getUniqueId())) {
                ChatUtil.message(player, "You must be a member of this claim to access settings.");
                return;
            }

            new SettingsInventory(plugin, claimManager, claim, player).open(player);
            return;
        }
        ChatUtil.message(player, "You must be a claim to use this command.");
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

            if (!claim.getOwner().equals(target.getUniqueId())) {
                ChatUtil.message(player, "You cannot remove the owner of this claim.");
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

            if (target.getUniqueId().equals(claim.getOwner())) {
                ChatUtil.message(player, "You cannot blacklist the owner of a claim.");
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
            claim.addMember(target.getUniqueId());
            ChatUtil.message(player, String.format("%s has been given ownership of this claim.", args[1]));
            return;
        }
        ChatUtil.message(player, "You must be in a claim to use this command");
    }

    public void claim(Player player, String... args) {
        claimManager.getTempClaiming().putIfAbsent(player.getUniqueId(), new Rectangle(null, null));
        if (!claimManager.getTempClaiming().get(player.getUniqueId()).notNullLocations()) {
            ChatUtil.message(player, "Set a first and second location in order to claim.");
            return;
        }



        Rectangle rectangle = claimManager.getTempClaiming().get(player.getUniqueId());

        Claim claim = new Claim(rectangle.getBottomLeftLocation(), rectangle.getXLength(), rectangle.getZLength(), player.getUniqueId(), Lists.newArrayList(),configManager.getDefaultSettings(), Lists.newArrayList(), rectangle.getLeftLocation().getWorld().toString());

        for (Claim otherClaim : claimManager.getClaims()) {
            if (claim.overlaps(otherClaim)) {
                ChatUtil.message(player, "This claim overlaps an existing claim.");
                return;
            }
        }

        if (configManager.getDisabledWorlds().contains(claim.getOrigin().getWorld().getName())) {
            ChatUtil.message(player, "Claiming is not allowed in this world.");
            return;
        }

        if (economyManager.chargeMoney(player.getUniqueId(), claim.getXLength() * claim.getZLength() * configManager.getClaimBlockBuyPice())) {
            claimManager.addClaim(claim);
            //claimBlocksManager.setBlockCount(player.getUniqueId(), claimBlocksManager.getBlockCount(player.getUniqueId()) - rectangle.getSize());
            ChatUtil.message(player, "Area claimed!");
            return;
        }
        ChatUtil.message(player, "You do not have enough money to claim this land. You must have %s.".formatted(claim.getXLength() * claim.getZLength() * configManager.getClaimBlockBuyPice()));
    }

    public void blocks(Player player, String... args) {
        ChatUtil.message(player, "You have %s claim blocks.".formatted(ChatColor.YELLOW + "" + claimBlocksManager.getBlockCount(player.getUniqueId()) + "" + ChatColor.WHITE));
    }
}
