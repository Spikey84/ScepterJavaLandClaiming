package io.github.spikey84.scepterjavaclaiming.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class I {
    public static ItemStack getFiller() {
        ItemStack i = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getVisibleFiller() {
        ItemStack i = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getEnabled() {
        ItemStack i = new ItemStack(Material.LIME_DYE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Enabled");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getDisabled() {
        ItemStack i = new ItemStack(Material.RED_DYE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Disabled");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getBack() {
        ItemStack i = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Back");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getNext() {
        ItemStack i = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Next");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getEmpty() {
        ItemStack i = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Empty Slot");
        i.setItemMeta(itemMeta);
        return i;
    }


    public static ItemStack setName(ItemStack i, String s) {
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(s);
        ItemStack itemStack = i.clone();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack setLore(ItemStack i, Lore s) {
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setLore(s.getContents());
        ItemStack itemStack = i.clone();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
