package io.github.spikey84.scepterjavaclaiming.homes;

import com.google.common.collect.Lists;
import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimSetting;
import io.github.spikey84.scepterjavaclaiming.settings.SettingsInventory;
import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import io.github.spikey84.scepterjavaclaiming.utils.I;
import io.github.spikey84.scepterjavaclaiming.utils.Lore;
import io.github.spikey84.scepterjavaclaiming.utils.inventory.BaseInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class HomesInventory extends BaseInventory {
    private Plugin plugin;
    private final int[] slots = new int[]{10,11,12,13,14,15,16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    private int page;
    private Player player;

    public HomesInventory(Plugin plugin, Player player, HomeManager homeManager) {
        this(plugin, player, homeManager, 0);
    }

    private HomesInventory(Plugin plugin, Player player, HomeManager homeManager, int page) {
        super(6, plugin);

        this.plugin = plugin;
        this.page = page;
        this.player = player;

        List<Home> homes = Lists.newArrayList();

        homes = homeManager.getHomes(player.getUniqueId());

        fillInventory(I.getFiller());

        int slot = 0;
        for (int x = page * slots.length; x < slots.length * (page + 1); x++) {
            if (homes == null) {
                continue;
            }
            if (x > homes.size()) {
                continue;
            }

            Home home = homes.get(x);

            ItemStack item = new ItemStack(Material.PAPER);
            item = I.setName(item, ChatColor.BLUE + String.format("Home: %s", home.getClaimId()));
            item = I.setLore(item, Lore.createLore(ChatColor.WHITE + home.getLocation().toString(), ChatColor.GREEN + "[Click to Travel]"));

            addItem(slots[slot], item, () -> {
                player.closeInventory();
                player.teleport(home.getLocation());
                ChatUtil.message(player, "Successfully traveled to home");
            });

            slot++;
        }

        if (page != 0) addItem(28, I.getBack(), () -> {
            player.closeInventory();
            new HomesInventory(plugin, player, homeManager, page-1).open(player);
        });

        if (page * slots.length > homes.size()) addItem(34, I.getNext(), () -> {
            player.closeInventory();
            new HomesInventory(plugin, player, homeManager, page+1).open(player);
        });
    }
}
