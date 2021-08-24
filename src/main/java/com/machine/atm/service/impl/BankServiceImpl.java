package com.machine.atm.service.impl;

import com.machine.atm.entity.BankEntity;
import com.machine.atm.exception.InsufficientBalanceException;
import com.machine.atm.exception.InvalidAccountException;
import com.machine.atm.exception.InvalidPinException;
import com.machine.atm.exception.OutOfMoneyException;
import com.machine.atm.mapper.BankEntityToUserDetailsResponse;
import com.machine.atm.model.CurrencyDenominations;
import com.machine.atm.model.MoneyWithdrawResponse;
import com.machine.atm.model.UserAccountDetailsResponse;
import com.machine.atm.repository.BankRepository;
import com.machine.atm.service.AtmService;
import com.machine.atm.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class BankServiceImpl implements BankService {

    private AtmService atmService;
    private BankRepository bankRepository;
    private BankEntityToUserDetailsResponse bankEntityToUserDetailsResponse;

    @Autowired
    public BankServiceImpl(BankRepository bankRepository, BankEntityToUserDetailsResponse bankEntityToUserDetailsResponse, AtmService atmService) {
        this.bankRepository = bankRepository;
        this.bankEntityToUserDetailsResponse = bankEntityToUserDetailsResponse;
        this.atmService = atmService;
    }

    @Override
    public UserAccountDetailsResponse checkBalance(String accountNumber, Integer pin) {
        Optional<BankEntity> accountById = bankRepository.findById(accountNumber);
        if (!accountById.isPresent()) {
            log.warn("Unable to find the provided account : {}", accountNumber);
            throw new InvalidAccountException("Unable to find the provided account " + accountNumber);
        }

        BankEntity bankEntity = accountById.get();

        if (!validatePin(Integer.valueOf(bankEntity.getAccountPin()), pin)) {
            log.warn("Incorrect pin for provided account : {}", accountNumber);
            throw new InvalidPinException("Incorrect pin for account " + accountNumber);
        }

        UserAccountDetailsResponse userAccountDetailsResponse = bankEntityToUserDetailsResponse.convert(bankEntity);

        return userAccountDetailsResponse;
    }


    @Override
    public MoneyWithdrawResponse withdrawAmount(String accountNumber, Integer pin, double requestedAmount) {
        Optional<BankEntity> accountById = bankRepository.findById(accountNumber);
        if (!accountById.isPresent()) {
            log.warn("Unable to find the provided account : {}", accountNumber);
            throw new InvalidAccountException("Unable to find the provided account " + accountNumber);
        }

        BankEntity bankEntity = accountById.get();

        if (!validatePin(Integer.valueOf(bankEntity.getAccountPin()), pin)) {
            log.warn("Incorrect pin for provided account : {}", accountNumber);
            throw new InvalidPinException("Incorrect pin for provided account " + accountNumber);
        }

        if (!validateFunds(bankEntity.getBalance(), bankEntity.getOverdraft(), requestedAmount)) {
            log.warn("Insufficient balance in provided account : {}", accountNumber);
            throw new InsufficientBalanceException("You dont have sufficient balance in account " + accountNumber);
        }


        Double availableBalanceInBank = bankEntity.getBalance();
        Double newBalance = availableBalanceInBank - requestedAmount;
        bankEntity.setBalance(newBalance);

        Map<CurrencyDenominations, Integer> dispensedMoney = atmService.dispenseMoney("A123", accountNumber, pin, requestedAmount);
        boolean amtDeducted = atmService.deductAmount("A123", requestedAmount, dispensedMoney);
        if (!amtDeducted) {
            log.warn("Unable to deduct money from the ATM.");
            throw new OutOfMoneyException("Unable to deduct money from the ATM.Something went wrong.");
        }

        // update the bank account
        BankEntity newlyUpdatedBankAccountInformation = bankRepository.save(bankEntity);

        // then retreive the information

        return MoneyWithdrawResponse.builder().accountNumber(newlyUpdatedBankAccountInformation.getAccountNumber()).balance(newlyUpdatedBankAccountInformation.getBalance())
                .withdrawnDenominations(dispensedMoney)
                .withdrawableBalance(newlyUpdatedBankAccountInformation.getBalance() + newlyUpdatedBankAccountInformation.getOverdraft())
                .overdraft(newlyUpdatedBankAccountInformation.getOverdraft())
                .build();
    }

    @Override
    public boolean validateAccountNumberAndPin(String accountNumber, Integer pin) {
        Optional<BankEntity> accountById = bankRepository.findById(accountNumber);
        if (!accountById.isPresent()) {
            return false;
        }

        BankEntity bankEntity = accountById.get();

        if (!validatePin(Integer.valueOf(bankEntity.getAccountPin()), pin)) {
            return false;
        }

        return true;
    }

    private boolean validatePin(Integer actualPin, Integer enteredPin) {
        return actualPin.equals(enteredPin);
    }

    private boolean validateFunds(double actualBalance, double actualOverdraftLimit, double requestedAmount) {

        if (((actualBalance + actualOverdraftLimit - requestedAmount) < 0)) {
            return false;
        }

        return true;
    }
}
