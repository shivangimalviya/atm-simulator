package com.machine.atm.utility;

import com.machine.atm.exception.InvalidAmountException;
import com.machine.atm.model.CurrencyDenominations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

// this will give us map of minimum number of currency if the accountNumber and pin is validated.
// we have to provide atmId , then we will get the atm-state and then calculate the minimum number of currency.
@Slf4j
@Component
public class CurrencyDenominationsUtility {

    public Map<CurrencyDenominations, Integer> currencyDenominations(Map<CurrencyDenominations, Integer> currentAtmState, double amtToDeduct) {
        Map<CurrencyDenominations, Integer> deductedAmt = new TreeMap<>(new CurrencyDenominationsComparator());
        Map<CurrencyDenominations, Integer> currentState = new TreeMap<>(new CurrencyDenominationsComparator());
        currentState.putAll(currentAtmState);

        currencyDenominationsChild(currentState, amtToDeduct, deductedAmt);
        return deductedAmt;
    }


    private void currencyDenominationsChild(Map<CurrencyDenominations, Integer> currentAtmState, double amtToDeduct, Map<CurrencyDenominations, Integer> deductedAmt) {

        if (amtToDeduct == 0) {
            return;
        }

        if (amtToDeduct < 0 || (currentAtmState.keySet().stream().map(x -> x.getNumericValue()).min(Comparator.naturalOrder()).get() > amtToDeduct)) {
            String joined = String.join(",", currentAtmState.keySet().toString());
            throw new InvalidAmountException("Unable to dispense amount. Please try in multiples of : " + joined);
        }

        if (amtToDeduct > 0) {
            solveIntermediate(currentAtmState, amtToDeduct, deductedAmt);
        }
    }

    private double solveIntermediate(Map<CurrencyDenominations, Integer> currentAtmState, double amtToDeduct, Map<CurrencyDenominations, Integer> deductedAmtState) {

        for (Map.Entry<CurrencyDenominations, Integer> entry : currentAtmState.entrySet()) {

            if (amtToDeduct >= entry.getKey().getNumericValue()) {
                int denominationRequired = (int) (amtToDeduct / entry.getKey().getNumericValue());
                int maxDenominationsAvailable = currentAtmState.get(entry.getKey());

                if (denominationRequired > maxDenominationsAvailable) {
                    amtToDeduct = amtToDeduct - entry.getKey().getNumericValue() * maxDenominationsAvailable;
                    deductedAmtState.put(entry.getKey(), maxDenominationsAvailable);
                    currentAtmState.put(entry.getKey(), 0);
                } else {
                    amtToDeduct = amtToDeduct - entry.getKey().getNumericValue() * denominationRequired;
                    deductedAmtState.put(entry.getKey(), denominationRequired);
                    currentAtmState.put(entry.getKey(), maxDenominationsAvailable - denominationRequired);
                }
            }
        }

        if (currentAtmState.keySet().stream().map(x -> x.getNumericValue()).min(Comparator.naturalOrder()).get() > amtToDeduct) {
            currencyDenominationsChild(currentAtmState, amtToDeduct, deductedAmtState);
        }

        return amtToDeduct;

    }


    // test utility
    public static void main(String[] args) {

        CurrencyDenominationsUtility utility = new CurrencyDenominationsUtility();
        Map<CurrencyDenominations, Integer> map = new TreeMap<>(new CurrencyDenominationsComparator());
        map.put(CurrencyDenominations.FIFTY, 30);
        map.put(CurrencyDenominations.TWENTY, 10);
        map.put(CurrencyDenominations.TEN, 20);
        map.put(CurrencyDenominations.FIVE, 10);

        System.out.println(utility.currencyDenominations(map, 9));
    }

}
