package com.machine.atm.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Atm {

    private String atmId;
    private double totalAmount;
    private Map<CurrencyDenominations, Integer> currencyDenominationsInAtm;
}
