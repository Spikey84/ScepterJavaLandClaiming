package io.github.spikey84.scepterjavaclaiming.settings;

import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimSetting;
import io.github.spikey84.scepterjavaclaiming.utils.I;
import io.github.spikey84.scepterjavaclaiming.utils.inventory.BaseInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class SettingsInventory extends BaseInventory {
    private Plugin plugin;
    private final int[] slots = new int[]{11,12,13,14,15};

    private int page;
    private Claim claim;
    private Player player;

    public SettingsInventory(Plugin plugin, Claim claim, Player player) {
        this(plugin, claim, player, 0);
    }

    private SettingsInventory(Plugin plugin, Claim claim, Player player, int page) {
        super(4, plugin);

        this.plugin = plugin;
        this.page = page;
        this.claim = claim;
        this.player = player;

        fillInventory(I.getFiller());

        int slot = 0;
        for (int x = page * slots.length; x < slots.length * (page + 1); x++) {
            if (x > ClaimSetting.values().length) {
                continue;
            }

            ClaimSetting claimSetting = ClaimSetting.getFromID((byte) x);
            boolean settingValue = claim.getClaimSettings().get(claimSetting);

            ItemStack settingItem = new ItemStack(Material.PAPER);
            settingItem = I.setName(settingItem, claimSetting.toString());

            addItem(slots[slot], settingItem);

            ItemStack valueItem;

            if (settingValue)
                valueItem = I.getEnabled();
            else
                valueItem = I.getDisabled();

            addItem(slots[slot]+9, valueItem, ()-> {
                claim.getClaimSettings().put(claimSetting, !settingValue);

                player.closeInventory();
                new SettingsInventory(plugin, claim, player, page).open(player);
            });

            slot++;
        }

        if (page != 0) addItem(28, I.getBack(), () -> {
            player.closeInventory();
            new SettingsInventory(plugin, claim, player, page-1).open(player);
        });

        if (page * slots.length > ClaimSetting.values().length) addItem(34, I.getNext(), () -> {
            player.closeInventory();
            new SettingsInventory(plugin, claim, player, page+1).open(player);
        });
    }

}
