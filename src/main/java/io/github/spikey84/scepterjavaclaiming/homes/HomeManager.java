package io.github.spikey84.scepterjavaclaiming.homes;

import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.DatabaseManager;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;

import java.security.spec.ECField;
import java.sql.Connection;
import java.util.HashMap;

public class HomeManager {
    private HashMap<Integer, Home> homes;

    public HomeManager() {
        this.homes = Maps.newHashMap();

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
}
