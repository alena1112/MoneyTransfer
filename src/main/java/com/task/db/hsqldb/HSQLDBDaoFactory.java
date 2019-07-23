package com.task.db.hsqldb;

import com.task.db.core.AccountDao;
import com.task.db.core.DaoFactory;
import com.task.db.core.EntityDao;
import com.task.model.Currency;
import com.task.model.History;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class HSQLDBDaoFactory implements DaoFactory {
    private Connection connection;
    private AccountDao accountDao;
    private EntityDao<History> historyDao;
    private EntityDao<Currency> currencyDao;

    public static final String databaseFilesPath = "hsqldb_files";
    public static final String databaseName = "ts_db";
    public static final String userName = "SA";
    public static final String userPassword = "";
    public static final String initScriptPath = "src/main/resources/db_scripts/init_db.sql";

    private static final Logger log = Logger.getLogger(HSQLDBDaoFactory.class);

    public HSQLDBDaoFactory() {
        this.connection = getConnection();
    }

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            connection = DriverManager.getConnection(String.format("jdbc:hsqldb:file:%s/%s", databaseFilesPath, databaseName),
                    userName, userPassword);
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Exception during database connection", e);
        }
        return connection;
    }

    @Override
    public void initDB() {
        try (Statement statement = connection.createStatement()) {
            List<String> queries = FileUtils.readLines(new File(initScriptPath), Charset.defaultCharset());
            for (String query : queries) {
                statement.execute(query);
            }
            connection.commit();
        } catch (Exception e) {
            log.error("Exception during init database", e);
        }
    }

    @Override
    public AccountDao getAccountDao() {
        if (accountDao == null) {
            accountDao = new HSQLDBAccountDao(connection);
        }
        return accountDao;
    }

    @Override
    public EntityDao<History> getHistoryDao() {
        if (historyDao == null) {
            historyDao = new HSQLDBHistoryDao(connection, accountDao);
        }
        return historyDao;
    }

    @Override
    public EntityDao<Currency> getCurrencyDao() {
        if (currencyDao == null) {
            currencyDao = new HSQLDBCurrencyDao(connection);
        }
        return currencyDao;
    }
}
