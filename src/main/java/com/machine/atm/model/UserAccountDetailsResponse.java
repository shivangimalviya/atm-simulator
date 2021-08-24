package com.machine.atm.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAccountDetailsResponse {

    private String accountNumber;
    private double balance;
    private double overdraft;
    private double withdrawableBalance;
}
