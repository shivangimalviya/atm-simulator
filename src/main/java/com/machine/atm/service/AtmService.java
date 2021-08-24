package com.machine.atm.service;

import com.machine.atm.model.Atm;
import com.machine.atm.model.CurrencyDenominations;

import java.util.Map;

public interface AtmService {

    double initializeAmount(String atmId, double amount, Map<CurrencyDenominations, Integer> currencyAmount);

    boolean addAmount(String atmId, double amount, Map<CurrencyDenominations, Integer> currencyAmount);

    boolean deductAmount(String atmId, double amount, Map<CurrencyDenominations, Integer> currencyAmount);

    Atm currentAtmState(String atmId);

    double totalAmountInAtm(String atmId);

    Map<CurrencyDenominations, Integer> dispenseMoney(String atmId, String accountNumber, Integer pin, Double amount);
}
