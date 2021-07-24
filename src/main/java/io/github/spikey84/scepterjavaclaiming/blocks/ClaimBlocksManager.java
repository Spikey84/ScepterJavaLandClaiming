package io.github.spikey84.scepterjavaclaiming.blocks;

import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.ConfigManager;
import io.github.spikey84.scepterjavaclaiming.DatabaseManager;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

public class ClaimBlocksManager {
    private HashMap<UUID, Long> blocks;
    private ConfigManager configManager;

    public ClaimBlocksManager(ConfigManager configManager) {
        this.configManager = configManager;


        blocks = Maps.newHashMap();
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                blocks.putAll(ClaimBlocksDAO.getBlocks(connection));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        SchedulerUtils.runRepeating(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                blocks.putIfAbsent(player.getUniqueId(), configManager.getDefaultClaims());
                blocks.put(player.getUniqueId(), blocks.get(player.getUniqueId()) + configManager.getClaimsPerCycle());
            }
        }, configManager.getTimeInSeconds() * 20);
    }

    public void setBlockCount(UUID uuid, long numBlocks) {
        blocks.put(uuid, numBlocks);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimBlocksDAO.setBlocks(connection, uuid, numBlocks);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public long getBlockCount(UUID uuid) {
        if (!blocks.containsKey(uuid)) {
            blocks.put(uuid, configManager.getDefaultClaims());
            SchedulerUtils.runAsync(() -> {
                try (Connection connection = DatabaseManager.getConnection()) {
                    ClaimBlocksDAO.setBlocks(connection, uuid, configManager.getDefaultClaims());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return blocks.get(uuid);
    }


}
