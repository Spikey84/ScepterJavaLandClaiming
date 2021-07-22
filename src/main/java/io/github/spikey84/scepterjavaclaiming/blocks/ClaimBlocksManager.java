package io.github.spikey84.scepterjavaclaiming.blocks;

import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.DatabaseManager;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

public class ClaimBlocksManager {
    private HashMap<UUID, Long> blocks;

    public ClaimBlocksManager() {
        blocks = Maps.newHashMap();
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                blocks.putAll(ClaimBlocksDAO.getBlocks(connection));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        SchedulerUtils.runRepeating(() -> {

        }, 100);
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


}
