package com.task.model;

import org.apache.commons.lang3.StringUtils;

public enum CurrencyName {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR");

    private String id;

    CurrencyName(String id) {
        this.id = id;
    }

    public static CurrencyName fromId(String id) {
        if (StringUtils.isNotBlank(id)) {
            for (CurrencyName currencyName : values()) {
                if (currencyName.id.equals(id)) {
                    return currencyName;
                }
            }
        }
        return null;
    }
}
