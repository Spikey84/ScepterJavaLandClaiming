package io.github.spikey84.scepterjavaclaiming.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.utils.UUIDUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClaimBlocksDAO {
    public static HashMap<UUID, Long> getBlocks(Connection connection) {
        PreparedStatement statement = null;
        HashMap<UUID, Long> accounts = Maps.newHashMap();

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT blocks FROM block_accounts;";

            statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                long blocks = resultSet.getLong("blocks");
                accounts.put(UUIDUtils.build(resultSet.getString("uuid")), blocks);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public static void removeBlocks(Connection connection, int id, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM block_accounts WHERE uuid=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBlocks(Connection connection, UUID uuid, long blocks) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO block_accounts (uuid, blocks) \
                    VALUES\
                    (?, ?) ON DUPLICATE KEY UPDATE uuid=? blocks=?;
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setLong(2, blocks);
            statement.setString(3, UUIDUtils.strip(uuid));
            statement.setLong(4, blocks);

            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
