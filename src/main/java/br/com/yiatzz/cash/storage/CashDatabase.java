package br.com.yiatzz.cash.storage;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.config.Settings;
import br.com.yiatzz.cash.config.node.SQLConfig;
import me.lucko.helper.promise.Promise;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class CashDatabase extends Database {

    public CashDatabase(Logger logger, Settings settings) throws SQLException {
        super(logger, settings, "cash_users");
    }

    @Override
    public void createTable(CashPlugin plugin, Settings type) throws SQLException, IOException {
        StringBuilder builder = new StringBuilder();

        try (InputStream resourceStream = getClass().getResourceAsStream("/create.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
             Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line.replace("{TABLE_NAME}", tableName));
                if (line.endsWith(";")) {
                    String sql = builder.toString();
                    if (type.getGeneral().getSQL().getType() == SQLConfig.StorageType.SQLITE) {
                        sql = sql.replace("AUTO_INCREMENT", "AUTOINCREMENT");
                    }

                    stmt.addBatch(sql);
                    builder = new StringBuilder();
                }
            }

            stmt.executeBatch();
        }
    }

    public Promise<Double> requestCash(String userName) {
        Double localCash = cache.get(userName);
        if (localCash != null) {
            return Promise.completed(localCash);
        }

        Promise<Double> promise = Promise.start()
                .thenApplyAsync(cash -> fetchCash(userName));

        promise.thenAcceptSync(cash -> addCache(userName, cash));

        return promise;
    }

    public Promise<Void> updateCash(String userName, Double cash) {
        return Promise.start()
                .thenApplyAsync($ -> {
                    updateCashRaw(userName, cash);
                    return null;
                })
                .thenAcceptSync($ -> cache.put(userName, cash));
    }

    private void updateCashRaw(String name, double cash) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `cash_users` SET `Cash` = ? WHERE `UserName` = ? LIMIT 1;")) {

            preparedStatement.setDouble(1, cash);
            preparedStatement.setString(2, name);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    private Double fetchCash(String userName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT `Cash` FROM `cash_users` WHERE `UserName` = ? LIMIT 1;")) {

            preparedStatement.setString(1, userName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("Cash");
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public void close() {
        super.close();
    }
}
