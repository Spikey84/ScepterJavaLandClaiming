package io.github.spikey84.scepterjavaclaiming;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {
    private Plugin plugin;
    private static File databaseFile;
    private String query = """
            CREATE TABLE IF NOT EXISTS claims (\
            id INTEGER PRIMARY KEY AUTOINCREMENT,\
            owner TEXT NOT NULL,\
            settings BYTE[],\
            x_length INT NOT NULL,\
            z_length INT NOT NULL,\
            origin_x INT NOT NULL,\
            origin_z INT NOT NULL,\
            world_name TEXT \
            );
            """;
    private String query2 = """
            CREATE TABLE IF NOT EXISTS claim_members (\
            uuid VARCHAR NOT NULL,\
            claim_id INT NOT NULL\
            );
            """;
    private String query3 = """
            CREATE TABLE IF NOT EXISTS claim_blacklist (\
            uuid VARCHAR NOT NULL,\
            claim_id INT NOT NULL\
            );
            """;
    private String query4 = """
            CREATE TABLE IF NOT EXISTS block_accounts (\
            uuid VARCHAR NOT NULL PRIMARY KEY,\
            blocks BIGINT NOT NULL\
            );
            """;
    private String query5 = """
            CREATE TABLE IF NOT EXISTS homes (\
            id INTEGER PRIMARY KEY AUTOINCREMENT, \
            uuid VARCHAR NOT NULL,\
            x INT NOT NULL,\
            y INT NOT NULL,\
            z INT NOT NULL,\
            world_name TEXT NOT NULL\
            );
            """;
    private String query6 = """
            CREATE TABLE IF NOT EXISTS cooldowns (\
            id INTEGER, \
            uuid VARCHAR NOT NULL,\
            lastused TIMESTAMP NOT NULL\
            );
            """;

    public DatabaseManager(Plugin plugin) {
        this.plugin = plugin;
        createDatabase();
    }

    public void createDatabase() {
        File databaseFolder = new File(plugin.getDataFolder(), "claiming.db");
        if (!databaseFolder.exists()) {
            try {
                databaseFolder.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        databaseFile = databaseFolder;

        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
            statement = connection.createStatement();
            statement.executeUpdate(query2);
            statement.close();
            statement = connection.createStatement();
            statement.executeUpdate(query3);
            statement.close();
            statement = connection.createStatement();
            statement.executeUpdate(query4);
            statement.close();
            statement = connection.createStatement();
            statement.executeUpdate(query5);
            statement.close();
            statement = connection.createStatement();
            statement.executeUpdate(query6);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
