package br.com.yiatzz.cash.config.node;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class SQLConfig {

    @Setting(comment = "Escolha H2 ou MySQL")
    private StorageType type = StorageType.H2;

    @Setting(comment = "Caminho da database. (host ou caminho de arquivo)")
    private String path = "%DIR%";

    @Setting(comment = "Porta pro MySQL")
    private int port = 3306;

    @Setting(comment = "Nome da database")
    private String database = "academyCash";

    @Setting(comment = "Nome do usuário")
    private String username = "";

    @Setting(comment = "Senha do MySQL")
    private String password = "";

    public StorageType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public enum StorageType {

        MYSQL,

        MARIADB,

        SQLITE,

        H2;

        public String getJDBCId() {
            return name().toLowerCase();
        }
    }
}
