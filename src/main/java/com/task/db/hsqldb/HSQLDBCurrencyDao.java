package com.task.db.hsqldb;

import com.task.db.core.AbstractEntityDao;
import com.task.db.core.CurrencyDao;
import com.task.model.Currency;
import com.task.model.CurrencyName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class HSQLDBCurrencyDao extends AbstractEntityDao<Currency> implements CurrencyDao {

    public HSQLDBCurrencyDao(Connection connection) {
        this.connection = connection;
        this.tableName = "Currency";
        this.databaseName = HSQLDBDaoFactory.databaseName;
    }

    @Override
    protected String createInsertSqlQuery(Currency currency) {
        return String.format("INSERT INTO %s.Currency (id, currency_name, cost_in_rub) values ('%s', '%s', %s)",
                HSQLDBDaoFactory.databaseName,
                currency.getId(),
                currency.getCurrencyName(),
                currency.getCostInRUB());
    }

    @Override
    protected String createUpdateSqlQuery(Currency currency) {
        return String.format("UPDATE %s.Currency SET currency_name = '%s', cost_in_rub = %s where id = '%s'",
                HSQLDBDaoFactory.databaseName,
                currency.getCurrencyName(),
                currency.getCostInRUB(),
                currency.getId());
    }

    @Override
    protected Currency createEntityFromDb(ResultSet rs) throws SQLException {
        Currency currency = new Currency();
        currency.setId(UUID.fromString(rs.getString("id")));
        currency.setCurrencyName(CurrencyName.fromId(rs.getString("currency_name")));
        currency.setCostInRUB(rs.getDouble("cost_in_rub"));
        return currency;
    }

    @Override
    public Currency getByCurrencyName(CurrencyName currencyName) {
        String sql = String.format("SELECT * FROM %s.Currency where currency_name = '%s'",
                HSQLDBDaoFactory.databaseName, currencyName.getId());
        Currency currency = null;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                currency = createEntityFromDb(rs);
            }
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return currency;
    }
}
