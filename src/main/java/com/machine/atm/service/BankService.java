package com.machine.atm.service;

import com.machine.atm.model.MoneyWithdrawResponse;
import com.machine.atm.model.UserAccountDetailsResponse;

public interface BankService {

    UserAccountDetailsResponse checkBalance(String accountNumber, Integer pin);

    MoneyWithdrawResponse withdrawAmount(String accountNumber, Integer pin, double amount);

    boolean validateAccountNumberAndPin(String accountNumber, Integer pin);


}
