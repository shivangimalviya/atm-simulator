package com.machine.atm.utility;

import com.machine.atm.model.CurrencyDenominations;

import java.util.Comparator;

public class CurrencyDenominationsComparator implements Comparator<CurrencyDenominations> {
    @Override
    public int compare(CurrencyDenominations o1, CurrencyDenominations o2) {

        if (o1.getNumericValue() > o2.getNumericValue()) {
            return -1;
        } else if (o1.getNumericValue() < o2.getNumericValue()) {
            return 1;
        } else {
            return 0;
        }

    }
}
