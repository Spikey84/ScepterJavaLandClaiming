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
            CREATE TABLE IF NOT EXISTS claims (
            `id` INT,
            `owner` BINARY(16) NOT NULL,
            `settings` BYTE[],
            `x_length` INT NOT NULL,
            `z_length` INT NOT NULL,
            `origin_x` INT NOT NULL,
            `origin_z` INT NOT NULL,
            `world_name` TEXT NOT NULL,
            `server_name` TEXT NOT NULL,
            PRIMARY KEY (`id`)
            );
            """;
    private String query2 = """
            CREATE TABLE IF NOT EXISTS claim_members (
            `uuid` BINARY(16) NOT NULL,
            `claim_id` INT,
            );
            """;

    public DatabaseManager(Plugin plugin) {
        this.plugin = plugin;
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

        this.databaseFile = databaseFolder;

        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
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
