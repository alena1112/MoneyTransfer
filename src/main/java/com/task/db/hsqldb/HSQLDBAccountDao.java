package com.task.db.hsqldb;

import com.task.db.core.AbstractEntityDao;
import com.task.db.core.AccountDao;
import com.task.model.Account;
import com.task.model.Entity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HSQLDBAccountDao extends AbstractEntityDao<Account> implements AccountDao {

    public HSQLDBAccountDao(Connection connection) {
        this.connection = connection;
        this.tableName = "Account";
        this.databaseName = HSQLDBDaoFactory.databaseName;
    }

    public String createInsertSqlQuery(Account account) {
        return String.format("INSERT INTO %s.Account (id, name, amount, card_number) values ('%s', '%s', %s, '%s')",
                HSQLDBDaoFactory.databaseName,
                account.getId(),
                account.getName(),
                account.getAmount(),
                account.getCardNumber());
    }

    public String createUpdateSqlQuery(Account account) {
        return String.format("UPDATE %s.Account SET name = '%s', amount = %s, card_number = '%s' where id = '%s'",
                HSQLDBDaoFactory.databaseName,
                account.getName(),
                account.getAmount(),
                account.getCardNumber(),
                account.getId());
    }

    @Override
    protected Account createEntityFromDb(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(UUID.fromString(rs.getString("id")));
        account.setName(rs.getString("name"));
        account.setAmount(rs.getDouble("amount"));
        account.setCardNumber(rs.getString("card_number"));
        return account;
    }

    @Override
    public Account getByCardNumber(String cardNumber) {
        String sql = String.format("SELECT * FROM %s.Account where card_number = '%s'",
                HSQLDBDaoFactory.databaseName, cardNumber);
        Account account = null;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                account = createEntityFromDb(rs);
            }
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return account;
    }
}
