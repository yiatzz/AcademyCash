package br.com.yiatzz.cash.storage;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementConsumer {

    void accept(PreparedStatement preparedStatement) throws SQLException;
}
