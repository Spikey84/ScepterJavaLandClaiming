package io.github.spikey84.scepterjavaclaiming.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    public static void giveItem(Player player, ItemStack itemStack) {
        if (player.getInventory().firstEmpty() != -1)
            player.getInventory().setItem(player.getInventory().firstEmpty(), itemStack);
        else
            player.getLocation().getWorld().dropItem(player.getLocation(), itemStack);
    }
}
