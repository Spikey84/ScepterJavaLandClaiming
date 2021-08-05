package io.github.spikey84.scepterjavaclaiming.settings;

import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.ClaimSetting;
import io.github.spikey84.scepterjavaclaiming.utils.I;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;
import io.github.spikey84.scepterjavaclaiming.utils.StringUtils;
import io.github.spikey84.scepterjavaclaiming.utils.inventory.BaseInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class SettingsInventory extends BaseInventory {
    private Plugin plugin;
    private final int[] slots = new int[]{1,2,3,4,5,6,7};

    private int page;
    private Claim claim;
    private Player player;

    private ClaimManager claimManager;

    public SettingsInventory(Plugin plugin, ClaimManager claimManager, Claim claim, Player player) {
        this(plugin, claimManager, claim, player, 0);
    }

    private SettingsInventory(Plugin plugin, ClaimManager claimManager, Claim claim, Player player, int page) {
        super(3, plugin, StringUtils.centerText("&e&lSettings"));

        this.plugin = plugin;
        this.page = page;
        this.claim = claim;
        this.player = player;

        this.claimManager = claimManager;

        fillInventory(I.getVisibleFiller());

        int slot = 0;
        for (int x = page * slots.length; x < slots.length * (page + 1); x++) {
            if (x >= ClaimSetting.values().length) break;


            ClaimSetting claimSetting = ClaimSetting.getFromID((byte) x);
            boolean settingValue = claim.getClaimSettings().get(claimSetting);

            ItemStack settingItem = new ItemStack(claimSetting.getMaterial());
            settingItem = I.setName(settingItem, ChatColor.BLUE + "" + claimSetting.getName());

            addItem(slots[slot], settingItem);

            ItemStack valueItem;

            if (settingValue)
                valueItem = I.getEnabled();
            else
                valueItem = I.getDisabled();

            addItem(slots[slot]+9, valueItem, ()-> {
                claim.getClaimSettings().put(claimSetting, !settingValue);
                SchedulerUtils.runAsync(() -> {
                    claimManager.addClaim(claim);
                });

                new SettingsInventory(plugin, claimManager,claim, player, page).open(player);
            });

            slot++;
        }

        if (page != 0) addItem(20, I.getBack(), () -> {
            new SettingsInventory(plugin, claimManager, claim, player, page-1).open(player);
        });

        if ((page + 1) * slots.length <= ClaimSetting.values().length) addItem(24, I.getNext(), () -> {
            new SettingsInventory(plugin, claimManager, claim, player, page+1).open(player);
        });
    }

}
