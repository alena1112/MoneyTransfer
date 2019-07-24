package com.task.controller;

import com.task.db.core.DaoFactory;
import com.task.model.Currency;
import com.task.model.CurrencyName;
import org.apache.log4j.Logger;

public class CurrencyConverter {
    private DaoFactory daoFactory;

    private static final Logger log = Logger.getLogger(CurrencyConverter.class);

    public CurrencyConverter(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public double convert(CurrencyName from, double amount, CurrencyName to) {
        if (from == to) {
            return amount;
        }
        if (from == CurrencyName.RUB) {
            Currency currencyTo = daoFactory.getCurrencyDao().getByCurrencyName(to);
            if (currencyTo != null) {
                return amount / currencyTo.getCostInRUB();
            } else {
                log.error(String.format("Currency %s does not exist in database", to.getId()));
                return amount;
            }
        } else if (to == CurrencyName.RUB) {
            Currency currencyFrom = daoFactory.getCurrencyDao().getByCurrencyName(from);
            if (currencyFrom != null) {
                return amount * currencyFrom.getCostInRUB();
            } else {
                log.error(String.format("Currency %s does not exist in database", to.getId()));
                return amount;
            }
        } else {
            Currency currencyTo = daoFactory.getCurrencyDao().getByCurrencyName(to);
            Currency currencyFrom = daoFactory.getCurrencyDao().getByCurrencyName(from);
            if (currencyTo != null && currencyFrom != null) {
                return (amount * currencyTo.getCostInRUB()) / currencyFrom.getCostInRUB();
            } else {
                log.error(String.format("Currencies %s, %s do not exist in database", from.getId(), to.getId()));
                return amount;
            }
        }
    }
}
