package com.machine.atm.model;

public enum CurrencyDenominations {

    FIFTY(50), TWENTY(20), TEN(10), FIVE(5);
    private final int numericValue;

    CurrencyDenominations(int numericValue) {
        this.numericValue = numericValue;
    }

    public int getNumericValue() {
        return numericValue;
    }
}
