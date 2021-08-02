package io.github.spikey84.scepterjavaclaiming.homes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.DatabaseManager;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;

import java.security.spec.ECField;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeManager {
    private HashMap<Integer, Home> homes;
    private ClaimManager claimManager;

    public HomeManager(ClaimManager claimManager) {
        this.homes = Maps.newHashMap();
        this.claimManager = claimManager;

        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                for (Home home : HomesDAO.getHomes(connection)) {
                    this.homes.put(home.getClaimId(), home);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addHome(Home home) {
        homes.put(home.getClaimId(), home);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                HomesDAO.addHome(connection, home);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Home getHome(int id) {
        return homes.get(id);
    }

    public boolean hasHome(int id) {
        return homes.containsKey(id);
    }

    public List<Home> getHomes(UUID uuid) {
        List<Home> list = Lists.newArrayList();
        for (Map.Entry<Integer, Home> entry : homes.entrySet()) {
            if (claimManager.getClaimByID(entry.getKey()).getMembers().contains(uuid)) list.add(entry.getValue());
        }
        return list;
    }
}
