package com.machine.atm.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Bank {

    private String accountNumber;
    private String accountPin;
    private Double balance;
    private Double overdraft;
}
