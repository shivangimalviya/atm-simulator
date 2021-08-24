package com.machine.atm.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAccountDetails {

    private String accountNumber;
    private Integer accountPin;
    private double amountToWithdraw;
}
