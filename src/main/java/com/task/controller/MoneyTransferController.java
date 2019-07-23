package com.task.controller;

import com.task.db.core.AccountDao;
import com.task.db.core.DaoFactory;
import com.task.db.DatabaseType;
import com.task.db.core.EntityDao;
import com.task.db.hsqldb.HSQLDBDaoFactory;
import com.task.model.Account;
import com.task.model.History;
import org.apache.log4j.Logger;

public class MoneyTransferController {

    private DaoFactory daoFactory;

    private static final Logger log = Logger.getLogger(MoneyTransferController.class);

    public MoneyTransferController(DatabaseType databaseType) {
        switch (databaseType) {
            case HSQLDB:
                daoFactory = new HSQLDBDaoFactory();
                break;
//            case MYSQLDB:
//                daoFactory = new MYSQLDBDaoFactory();
//                break;
            default:
                daoFactory = new HSQLDBDaoFactory();
        }
        daoFactory.initDB();
    }

    public boolean transfer(String fromCardNumber, String toCardNumber, double amount) {
        AccountDao accountDao = daoFactory.getAccountDao();
        Account fromAccount = accountDao.getByCardNumber(fromCardNumber);
        Account toAccount = accountDao.getByCardNumber(toCardNumber);

        if (validate(fromAccount, toAccount, fromCardNumber, toCardNumber, amount)) {
            return transfer(fromAccount, toAccount, amount);
        }

        return false;
    }

    private boolean validate(Account fromAccount, Account toAccount, String fromCardNumber,
                             String toCardNumber, double amount) {
        if (fromAccount == null) {
            log.warn(String.format("Account %s does not exist", fromCardNumber));
            return false;
        }

        if (toAccount == null) {
            log.warn(String.format("Account %s does not exist", toCardNumber));
            return false;
        }

        if (fromAccount.getAmount() - amount < 0) {
            log.warn(String.format("Account %s does not have enough money", fromCardNumber));
            return false;
        }

        return true;
    }

    private boolean transfer(Account fromAccount, Account toAccount, double amount) {
        fromAccount.setAmount(fromAccount.getAmount() - amount);
        toAccount.setAmount(toAccount.getAmount() + amount);

        if (daoFactory.getAccountDao().update(fromAccount, toAccount)) {
            daoFactory.getHistoryDao().create(new History(fromAccount, toAccount, amount));
        } else {
            return false;
        }

        log.warn(String.format("Transfer money %s from account %s to account %s", amount, fromAccount.getCardNumber(),
                toAccount.getCardNumber()));
        return true;
    }

}
