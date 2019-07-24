package com.task.db.core;

import com.task.model.Currency;
import com.task.model.CurrencyName;

public interface CurrencyDao extends EntityDao<Currency> {
    Currency getByCurrencyName(CurrencyName currencyName);
}
