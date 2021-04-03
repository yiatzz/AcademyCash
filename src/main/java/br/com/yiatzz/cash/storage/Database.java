package br.com.yiatzz.cash.storage;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.config.GeneralConfig;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Database {

    protected final Logger logger;

    protected final Map<String, Double> cache = new ConcurrentHashMap<>();
    protected final DataSource dataSource;

    protected final String tableName;

    public Database(Logger logger, String tableName) throws SQLException {
        this.logger = logger;
        this.tableName = tableName;

        String jdbcConnection = buildJDBCUrl();
        this.dataSource = Sponge.getServiceManager().provideUnchecked(SqlService.class).getDataSource(jdbcConnection);
    }

    private String buildJDBCUrl() {
        String user = GeneralConfig.config.mySQLUser;
        String pass = GeneralConfig.config.mySQLPass;
        String host = GeneralConfig.config.mySQLHost;
        String database = GeneralConfig.config.mySQLDatabase;

        return "jdbc:mysql://" + host + ":3306/" + database + "?user=" + user + "&password=" + pass;
    }

    public void addCache(String userName, Double cash) {
        cache.put(userName, cash);
    }

    public void removeCache(String userName) {
        cache.remove(userName);
    }

    public void close() {
        cache.clear();
    }

    public void createTable(CashPlugin plugin) throws IOException, SQLException {
    }
}
