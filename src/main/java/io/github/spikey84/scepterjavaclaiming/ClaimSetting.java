package io.github.spikey84.scepterjavaclaiming;

public enum ClaimSetting {
    PVP((byte) 0),
    MOB_SPAWNING((byte) 1),
    ANIMAL_SPAWNING((byte) 2),
    MOB_GRIEFING((byte) 3),
    ENDER_PEARLS((byte) 4),
    ITEM_PICKUP((byte) 5),
    ITEM_DROP((byte) 6),
    FIRE_SPREAD((byte) 7),
    FIRE_DAMAGE((byte) 8),
    PUBLIC_USE_DOORS((byte) 9),
    PUBLIC_USE_GATES((byte) 10),
    PUBLIC_USE_CRAFTING_TABLE((byte) 11),
    PUBLIC_USE_PORTALS((byte) 12),
    PUBLIC_USE_ANVIL((byte) 13),
    PUBLIC_USE_ENCHANTTABLE((byte) 14),
    PUBLIC_USE_CHEST((byte) 15),
    PUBLIC_USE_ENDERCHEST((byte) 16);

    private byte id;

    ClaimSetting(byte i) {
        this.id = i;
    }

    public byte getId() {
        return id;
    }

    public static ClaimSetting getFromID(byte id) {
        for (ClaimSetting claimSetting : ClaimSetting.values()) {
            if (claimSetting.id == id) return claimSetting;
        }
        return null;
    }
}
