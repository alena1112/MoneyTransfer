package com.task.db.core;

import com.task.model.Account;

public interface AccountDao extends EntityDao<Account> {
    Account getByCardNumber(String cardNumber);
}
