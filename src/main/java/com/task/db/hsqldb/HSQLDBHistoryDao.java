package com.task.db.hsqldb;

import com.task.db.core.AbstractEntityDao;
import com.task.db.core.AccountDao;
import com.task.model.History;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class HSQLDBHistoryDao extends AbstractEntityDao<History> {
    private AccountDao accountDao;

    private static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public HSQLDBHistoryDao(Connection connection, AccountDao accountDao) {
        this.connection = connection;
        this.tableName = "History";
        this.databaseName = HSQLDBDaoFactory.databaseName;
        this.accountDao = accountDao;
    }

    @Override
    protected String createInsertSqlQuery(History history) {
        return String.format("INSERT INTO %s.History (id, from_account_id, to_account_id, amount, transfer_date) " +
                        "values ('%s', '%s', '%s', %s, '%s')",
                HSQLDBDaoFactory.databaseName,
                history.getId(),
                history.getFrom().getId(),
                history.getTo().getId(),
                history.getAmount(),
                history.getTransferDate(DB_DATE_FORMAT));
    }

    @Override
    protected String createUpdateSqlQuery(History history) {
        return String.format("UPDATE %s.History SET from_account_id = '%s', to_account_id = '%s', " +
                        "amount = %s, transfer_date = %s where id = '%s'",
                HSQLDBDaoFactory.databaseName,
                history.getFrom(),
                history.getTo(),
                history.getAmount(),
                history.getTransferDate(),
                history.getId());
    }

    @Override
    protected History createEntityFromDb(ResultSet rs) throws SQLException {
        History history = new History();
        history.setId(UUID.fromString(rs.getString("id")));
        history.setFrom(accountDao.getById(UUID.fromString(rs.getString("from_account_id"))));
        history.setTo(accountDao.getById(UUID.fromString(rs.getString("to_account_id"))));
        history.setAmount(rs.getDouble("amount"));
        history.setTransferDate(rs.getDate("transfer_date"));
        return history;
    }
}
