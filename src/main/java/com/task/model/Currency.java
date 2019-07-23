package com.task.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Currency extends Entity {
    private CurrencyName currencyName;
    private double costInRUB;

    public Currency(CurrencyName currencyName, double costInRUB) {
        super();
        this.currencyName = currencyName;
        this.costInRUB = costInRUB;
    }
}
