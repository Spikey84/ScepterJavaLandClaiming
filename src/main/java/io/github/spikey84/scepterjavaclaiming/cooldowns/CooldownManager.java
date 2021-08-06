package io.github.spikey84.scepterjavaclaiming.cooldowns;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.DatabaseManager;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {
    private HashMap<UUID, HashMap<Byte, Timestamp>> cooldowns;

    public CooldownManager() {
        try (Connection connection = DatabaseManager.getConnection()) {
            this.cooldowns = CooldownDAO.getCooldowns(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateCooldown(UUID uuid, byte id, Timestamp time) {
        cooldowns.putIfAbsent(uuid, Maps.newHashMap());
        cooldowns.get(uuid).put(id, time);

        try (Connection connection = DatabaseManager.getConnection()) {
            CooldownDAO.updateCooldown(connection, uuid, id, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExpired(UUID uuid, byte id, int minutes) {
        if (!cooldowns.containsKey(uuid)) return true;

        if (!cooldowns.get(uuid).containsKey(id)) return true;

        long milDiffrence = Math.abs(cooldowns.get(uuid).get(id).getTime() - Timestamp.from(Instant.now()).getTime());

        if (milDiffrence > minutes * 60000) return true;
        return false;
     }



     public int getRemainingMinutes(UUID uuid, byte id, int minutes) {
         if (!cooldowns.containsKey(uuid)) return -1;

         if (!cooldowns.get(uuid).containsKey(id)) return -1;

         long milDiffrence = Math.abs(cooldowns.get(uuid).get(id).getTime() - Timestamp.from(Instant.now()).getTime());

         if (milDiffrence > minutes * 60000) return -1;
         return (int) (minutes - milDiffrence/60000);
     }
}
