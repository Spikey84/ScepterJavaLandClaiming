package io.github.spikey84.scepterjavaclaiming.cooldowns;

public enum CooldownType {
    TOOLS((byte) 0),
    EDIT((byte) 1);

    private byte id;
    CooldownType(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }
}
