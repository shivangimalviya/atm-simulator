package com.machine.atm.service.impl;

import com.machine.atm.entity.AtmEntity;
import com.machine.atm.exception.OutOfServiceException;
import com.machine.atm.mapper.AtmEntityToAtm;
import com.machine.atm.model.Atm;
import com.machine.atm.model.CurrencyDenominations;
import com.machine.atm.repository.AtmRepository;
import com.machine.atm.utility.CurrencyDenominationsComparator;
import com.machine.atm.utility.CurrencyDenominationsUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

class AtmServiceImplTest {

    @InjectMocks
    private AtmServiceImpl atmService;

    @Mock
    private AtmRepository atmRepository;

    @Mock
    private AtmEntityToAtm atmEntityToAtm;

    @Mock
    private CurrencyDenominationsUtility currencyDenominationsUtility;

    Map<CurrencyDenominations, Integer> map = new TreeMap<>(new CurrencyDenominationsComparator());
    Atm atm;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        map.put(CurrencyDenominations.FIFTY, 1);
        map.put(CurrencyDenominations.TEN, 5);

        AtmEntity x123 = AtmEntity.builder().atmId("X123").totalAmount(100).currencyDenominationsInAtm(map).build();
        atm = Atm.builder().atmId("X123").totalAmount(100).currencyDenominationsInAtm(map).build();

        Mockito.when(atmRepository.save(x123)).thenReturn(x123);
        Mockito.when(atmRepository.findById("X123")).thenReturn(Optional.of(x123));
        Mockito.when(atmEntityToAtm.convert(x123)).thenReturn(atm);

        Mockito.when(currencyDenominationsUtility.currencyDenominations(map, 100)).thenReturn(map);

    }

    @Test
    @DisplayName("Initializing ATM with 100 Euros")
    public void testInitializeAmount() {
        double x123 = atmService.initializeAmount("X123", 100, map);
        Assertions.assertEquals(100, x123);
    }

    @Test
    @DisplayName("Adding 200 Euros to ATM")
    public void testAddAmount() {
        boolean x123 = atmService.addAmount("X123", 200, map);
        Assertions.assertTrue(x123);
    }

    @Test
    @DisplayName("Deducting 200 Euros from ATM")
    public void testDeductAmount() {
        boolean x123 = atmService.deductAmount("X123", 200, map);
        Assertions.assertTrue(x123);
    }

    @Test
    @DisplayName("Checking current ATM state(X123)")
    public void testCurrentAtmState() {
        Atm x123 = atmService.currentAtmState("X123");
        Assertions.assertEquals("X123", x123.getAtmId());
    }

    @Test
    @DisplayName("Checking total amount in ATM")
    public void testTotalAmountInAtm() {
        double x123 = atmService.totalAmountInAtm("X123");
        Assertions.assertEquals(100, x123);
    }

    @Test
    @DisplayName("Checking if ATM is able to dispense money")
    public void testDispenseMoney() {
        Map<CurrencyDenominations, Integer> dispenseMoney = atmService.dispenseMoney("X123", "123456789", 1234, 100.0);
        Assertions.assertEquals(map.get(CurrencyDenominations.FIFTY), dispenseMoney.get(CurrencyDenominations.FIFTY));
    }

    @Test
    @DisplayName("Trying to check state of invalid atm")
    public void testStateForInvalidAtm() {
        Assertions.assertThrows(OutOfServiceException.class,
                () -> {
                    atmService.currentAtmState("XX");
                }
        );
    }


}