package br.com.yiatzz.cash.storage;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.config.Settings;
import br.com.yiatzz.cash.config.node.SQLConfig;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class Database {

    protected final Logger logger;
    protected final SQLConfig.StorageType storageType;

    protected final Map<String, Double> cache = new HashMap<>();
    protected final DataSource dataSource;

    protected final String tableName;

    public Database(Logger logger, Settings settings, String tableName) throws SQLException {
        this.logger = logger;
        this.tableName = tableName;

        SQLConfig sqlConfig = settings.getGeneral().getSQL();
        Path configDir = settings.getConfigDir();
        this.storageType = sqlConfig.getType();

        String jdbcConnection = buildJDBCUrl(sqlConfig, configDir);
        this.dataSource = Sponge.getServiceManager().provideUnchecked(SqlService.class).getDataSource(jdbcConnection);
    }

    private String buildJDBCUrl(SQLConfig sqlConfig, Path configDir) {
        String storagePath = sqlConfig.getPath().replace("%DIR%", configDir.toAbsolutePath().toString());

        SQLConfig.StorageType type = sqlConfig.getType();
        StringBuilder urlBuilder = new StringBuilder("jdbc:")
                .append(type.getJDBCId())
                .append(":");
        switch (type) {
            case SQLITE:
                urlBuilder.append(storagePath).append(File.separatorChar).append("database.db");
                break;
            case MARIADB:
            case MYSQL:
                urlBuilder.append("//");
                String username = sqlConfig.getUsername();
                if (!username.isEmpty()) {
                    urlBuilder.append(username);
                    String password = sqlConfig.getPassword();
                    if (!password.isEmpty()) {
                        urlBuilder.append(':').append(password);
                    }

                    urlBuilder.append('@');
                }

                urlBuilder.append(sqlConfig.getPath())
                        .append(':')
                        .append(sqlConfig.getPort())
                        .append('/')
                        .append(sqlConfig.getDatabase())
                        .append("?useSSL").append('=').append(false);
                break;
            case H2:
            default:
                urlBuilder.append(storagePath).append(File.separatorChar).append("database");
                break;
        }

        String jdbcUrl = urlBuilder.toString();
        return jdbcUrl;
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

    public void createTable(CashPlugin plugin, Settings type) throws IOException, SQLException {
    }
}
