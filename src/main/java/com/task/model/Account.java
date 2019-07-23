package com.task.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account extends Entity {
    private String name;
    private double amount;
    private CurrencyName currency;
    private String cardNumber;

    public Account(String name, double amount, CurrencyName currency, String cardNumber) {
        super();
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.cardNumber = cardNumber;
    }
}
