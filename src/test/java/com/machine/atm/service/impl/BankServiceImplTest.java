package com.machine.atm.service.impl;

import com.machine.atm.entity.AtmEntity;
import com.machine.atm.entity.BankEntity;
import com.machine.atm.exception.InsufficientBalanceException;
import com.machine.atm.exception.InvalidAccountException;
import com.machine.atm.exception.InvalidPinException;
import com.machine.atm.mapper.BankEntityToUserDetailsResponse;
import com.machine.atm.model.CurrencyDenominations;
import com.machine.atm.model.UserAccountDetailsResponse;
import com.machine.atm.repository.AtmRepository;
import com.machine.atm.repository.BankRepository;
import com.machine.atm.utility.CurrencyDenominationsComparator;
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

class BankServiceImplTest {

    @InjectMocks
    private BankServiceImpl bankService;

    @Mock
    private AtmServiceImpl atmService;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private AtmRepository atmRepository;

    @Mock
    private BankEntityToUserDetailsResponse bankEntityToUserDetailsResponse;

    private BankEntity bankEntity;
    private UserAccountDetailsResponse userAccountDetailsResponse;
    private Map<CurrencyDenominations, Integer> map = new TreeMap<>(new CurrencyDenominationsComparator());
    private AtmEntity atmEntity;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        map.put(CurrencyDenominations.TEN, 10);
        bankEntity = BankEntity.builder().accountNumber("X123456789").accountPin("1234").balance(500.0).overdraft(200.0).build();
        userAccountDetailsResponse = UserAccountDetailsResponse.builder().accountNumber("X123456789").balance(500.0).overdraft(200.0).withdrawableBalance(700.0).build();
        atmEntity = AtmEntity.builder().atmId("X123").currencyDenominationsInAtm(map).totalAmount(100).build();

        Mockito.when(bankRepository.findById("X123456789")).thenReturn(Optional.of(bankEntity));
        Mockito.when(bankRepository.save(bankEntity)).thenReturn(bankEntity);
        Mockito.when(bankEntityToUserDetailsResponse.convert(bankEntity)).thenReturn(userAccountDetailsResponse);
        Mockito.when(atmService.dispenseMoney("X123", "X123456789", 1234, 100.0)).thenReturn(map);
        Mockito.when(atmService.deductAmount("X123", 100.0, map)).thenReturn(true);

        Mockito.when(atmRepository.findById("X123")).thenReturn(Optional.of(atmEntity));


    }

    @Test
    @DisplayName("Checking balance in account X123456789")
    public void testCheckBalance() {

        UserAccountDetailsResponse userAccountDetailsResponse = bankService.checkBalance("X123456789", 1234);
        Assertions.assertEquals("X123456789", userAccountDetailsResponse.getAccountNumber());
    }


    @Test
    @DisplayName("Withdrawing more money than present from account X123456789")
    public void testWithdrawAmount_InsufficientBalance() {

        Assertions.assertThrows(InsufficientBalanceException.class,
                () -> {
                    bankService.withdrawAmount("X123456789", 1234, 10000);
                }
        );
    }

    @Test
    @DisplayName("Checking with Invalid Pin for account X123456789")
    public void testWithdrawAmount_IncorrectPin() {

        Assertions.assertThrows(InvalidPinException.class,
                () -> {
                    bankService.withdrawAmount("X123456789", 4321, 10000);
                }
        );
    }

    @Test
    @DisplayName("Checking balance in invalid account")
    public void testWithdrawAmount_InvalidAccount() {

        Assertions.assertThrows(InvalidAccountException.class,
                () -> {
                    bankService.withdrawAmount("XX", 1234, 10000);
                }
        );


    }

    @Test
    @DisplayName("Validating account number and Pin combination")
    public void validateAccountNumberAndPinTest() {
        boolean x123456789 = bankService.validateAccountNumberAndPin("X123456789", 1234);
        Assertions.assertTrue(x123456789);
    }
}