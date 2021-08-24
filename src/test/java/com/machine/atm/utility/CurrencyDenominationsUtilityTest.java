package com.machine.atm.utility;

import com.machine.atm.exception.InvalidAmountException;
import com.machine.atm.model.CurrencyDenominations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Map;
import java.util.TreeMap;

class CurrencyDenominationsUtilityTest {

    @InjectMocks
    private CurrencyDenominationsUtility currencyDenominationsUtility;

    private Map<CurrencyDenominations, Integer> currentAtmState;

    @BeforeEach
    public void init() {
        currencyDenominationsUtility = new CurrencyDenominationsUtility();
        currentAtmState = new TreeMap<>(new CurrencyDenominationsComparator());

        currentAtmState.put(CurrencyDenominations.FIFTY, 10);
        currentAtmState.put(CurrencyDenominations.TWENTY, 10);
        currentAtmState.put(CurrencyDenominations.TEN, 10);
        currentAtmState.put(CurrencyDenominations.FIVE, 10);
    }

    @Test
    @DisplayName("Deduct requestedMoney in the form of currency")
    public void testCurrencyDenominationsWithValidAmount() {
        Assertions.assertEquals(10, currencyDenominationsUtility.currencyDenominations(currentAtmState, 1500).get(CurrencyDenominations.FIFTY));

    }

    @Test
    @DisplayName("Throw Exception when invalid amount is provided")
    public void testCurrencyDenominationsWithInValidAmount() {

        Assertions.assertThrows(InvalidAmountException.class, () -> {
            currencyDenominationsUtility.currencyDenominations(currentAtmState, 9);
        });

    }

}