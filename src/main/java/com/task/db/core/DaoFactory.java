package com.task.db.core;

import com.task.model.Currency;
import com.task.model.History;

import java.sql.Connection;

public interface DaoFactory {
    Connection getConnection();
    void initDB();
    AccountDao getAccountDao();
    EntityDao<History> getHistoryDao();
    CurrencyDao getCurrencyDao();
}
