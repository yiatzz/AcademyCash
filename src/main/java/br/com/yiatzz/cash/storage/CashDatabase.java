package br.com.yiatzz.cash.storage;

import br.com.yiatzz.cash.CashPlugin;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CashDatabase extends Database {

    public CashDatabase(Logger logger) throws SQLException {
        super(logger, "cash_users");
    }

    @Override
    public void createTable(CashPlugin plugin) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS `cash_users` (`UserName` VARCHAR(16) NOT NULL PRIMARY KEY, `Cash` DOUBLE NOT NULL DEFAULT 0);";

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();
        }
    }

    public double requestCash(String userName) {
        Double localCash = cache.get(userName);
        if (localCash != null) {
            return localCash;
        }

        return fetchCash(userName);
    }

    public void updateCash(String name, double cash) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO `cash_users` (`UserName`, `Cash`) VALUES (?,?) ON DUPLICATE KEY UPDATE `Cash`=VALUES(Cash);"
             )) {

            preparedStatement.setDouble(2, cash);
            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void deleteUser(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `cash_users` WHERE `UserName` = ? LIMIT 1")) {

            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private double fetchCash(String userName) {
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

        return 0.0;
    }

    @Override
    public void close() {
        super.close();
    }
}
