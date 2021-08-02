package io.github.spikey84.scepterjavaclaiming;

import org.bukkit.Material;

public enum ClaimSetting {
    PVP((byte) 0, Material.GOLDEN_SWORD, "PvP"),
    MOB_SPAWNING((byte) 1, Material.ZOMBIE_SPAWN_EGG, "Allow Monster Spawning"),
    ANIMAL_SPAWNING((byte) 2, Material.COW_SPAWN_EGG, "Allow Animal Spawning"),
    MOB_GRIEFING((byte) 3, Material.TNT, "Allow Mob Griefing"),
    ENDER_PEARLS((byte) 4, Material.ENDER_PEARL, "Use Ender Pearls"),
    ITEM_PICKUP((byte) 5, Material.BONE, "Public Drop Items"),
    ITEM_DROP((byte) 6, Material.BONE, "Public Drop Items"),
    FIRE_SPREAD((byte) 7, Material.FLINT_AND_STEEL, "Fire Spread"),
    FIRE_DAMAGE((byte) 8, Material.FIRE_CHARGE, "Fire Damage"),
    PUBLIC_USE_DOORS((byte) 9, Material.OAK_DOOR, "Allow Doors"),
    PUBLIC_USE_GATES((byte) 10, Material.OAK_FENCE_GATE, "Allow Gates"),
    PUBLIC_USE_CRAFTING_TABLE((byte) 11, Material.CRAFTING_TABLE, "Allow Crafting Tables"),
    PUBLIC_USE_PORTALS((byte) 12, Material.NETHER_PORTAL, "Allow Entering Portals"),
    PUBLIC_USE_ANVIL((byte) 13, Material.ANVIL, "Allow Anvils"),
    PUBLIC_USE_ENCHANTTABLE((byte) 14, Material.ANVIL, "Allow Enchantment Tables"),
    PUBLIC_USE_CHEST((byte) 15, Material.CHEST, "Allow Chests"),
    PUBLIC_USE_ENDERCHEST((byte) 16, Material.ENDER_CHEST, "Allow Ender Chests");

    private byte id;
    private Material material;
    private String name;

    ClaimSetting(byte i, Material material, String name) {
        this.id = i;
        this.material = material;
        this.name = name;
    }

    public byte getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public static ClaimSetting getFromID(byte id) {
        for (ClaimSetting claimSetting : ClaimSetting.values()) {
            if (claimSetting.id == id) return claimSetting;
        }
        return null;
    }
}
