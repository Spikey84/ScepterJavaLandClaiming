package io.github.spikey84.scepterjavaclaiming.homes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.DatabaseManager;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;

import java.security.spec.ECField;
import java.sql.Connection;
import java.util.*;

public class HomeManager {
    private ArrayList<Home> homes;
    private ClaimManager claimManager;

    public HomeManager(ClaimManager claimManager) {
        this.homes = Lists.newArrayList();
        this.claimManager = claimManager;

        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                homes.addAll(HomesDAO.getHomes(connection));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addHome(Home home) {
        homes.add(home);
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

    public boolean hasHome(UUID uuid) {
        for (Home home : homes) {
            if (home.getUuid().equals(uuid)) return true;
        }
        return false;
    }

    public List<Home> getHomes(UUID uuid) {
        List<Home> list = Lists.newArrayList();
        for (Home home : homes) {
            if (home.getUuid().equals(uuid)) list.add(home);
        }
        return list;
    }
}
