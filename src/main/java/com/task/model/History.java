package com.task.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
public class History extends Entity {
    private Account from;
    private Account to;
    private double amount;
    private Date transferDate;

    public History(Account from, Account to, double amount) {
        super();
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.transferDate = new Date(System.currentTimeMillis());
    }

    public String getTransferDate(String format) {
        return new SimpleDateFormat(format).format(transferDate);
    }
}
