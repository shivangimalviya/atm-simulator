package com.machine.atm.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MoneyWithdrawResponse {

    private String accountNumber;
    private Map<CurrencyDenominations, Integer> withdrawnDenominations;
    private double balance;
    private double overdraft;
    private double withdrawableBalance;


}
