package com.machine.atm.service.impl;

import com.machine.atm.entity.AtmEntity;
import com.machine.atm.exception.OutOfServiceException;
import com.machine.atm.mapper.AtmEntityToAtm;
import com.machine.atm.model.Atm;
import com.machine.atm.model.CurrencyDenominations;
import com.machine.atm.repository.AtmRepository;
import com.machine.atm.service.AtmService;
import com.machine.atm.utility.CurrencyDenominationsComparator;
import com.machine.atm.utility.CurrencyDenominationsUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Slf4j
@Service
public class AtmServiceImpl implements AtmService {

    private AtmRepository atmRepository;
    private AtmEntityToAtm atmEntityToAtm;
    private CurrencyDenominationsUtility currencyDenominationsUtility;

    @Autowired
    public AtmServiceImpl(AtmRepository atmRepository, AtmEntityToAtm atmEntityToAtm, CurrencyDenominationsUtility currencyDenominationsUtility) {
        this.atmRepository = atmRepository;
        this.atmEntityToAtm = atmEntityToAtm;
        this.currencyDenominationsUtility = currencyDenominationsUtility;
    }

    @Override
    public double initializeAmount(String atmId, double amount, Map<CurrencyDenominations, Integer> currencyAmount) {
        log.info("Initializing ATM : {} with amount : {}", atmId, amount);
        AtmEntity atmEntity = AtmEntity.builder().atmId(atmId).totalAmount(amount).currencyDenominationsInAtm(currencyAmount).build();
        AtmEntity save = atmRepository.save(atmEntity);
        return save.getTotalAmount();
    }

    @Override
    public boolean addAmount(String atmId, double amount, Map<CurrencyDenominations, Integer> currencyAmount) {
        Optional<AtmEntity> atmById = atmRepository.findById(atmId);
        if (!atmById.isPresent()) {
            log.warn("Unable to find the ATM with provided id : {}", atmId);
            throw new OutOfServiceException("Unable to find the ATM with provided id : " + atmId);
        }

        final AtmEntity atmEntity = atmById.get();

        Map<CurrencyDenominations, Integer> existingCurrencyDenominationsInAtm = atmEntity.getCurrencyDenominationsInAtm();
        Map<CurrencyDenominations, Integer> newCurrencyDenominationsInAtm = new TreeMap<>(new CurrencyDenominationsComparator());
        newCurrencyDenominationsInAtm.putAll(currencyAmount);

        newCurrencyDenominationsInAtm.forEach(
                (key, value) -> existingCurrencyDenominationsInAtm.merge(
                        key, value, (v1, v2) -> v1 + v2
                ));


        double totalAmountInAtmFromCurrency = findTotalAmountInAtmFromCurrency(existingCurrencyDenominationsInAtm);
        atmEntity.setTotalAmount(totalAmountInAtmFromCurrency);
        atmEntity.setAtmId(atmId);
        atmEntity.setCurrencyDenominationsInAtm(existingCurrencyDenominationsInAtm);
        atmRepository.save(atmEntity);

        return true;
    }

    @Override
    public boolean deductAmount(String atmId, double amount, Map<CurrencyDenominations, Integer> currencyAmount) {
        Optional<AtmEntity> atmById = atmRepository.findById(atmId);
        if (!atmById.isPresent()) {
            log.warn("Unable to find the ATM with provided id : {}", atmId);
            throw new OutOfServiceException("Unable to find the ATM with provided id : " + atmId);
        }

        final AtmEntity atmEntity = atmById.get();

        Map<CurrencyDenominations, Integer> existingCurrencyDenominationsInAtm = atmEntity.getCurrencyDenominationsInAtm();
        Map<CurrencyDenominations, Integer> newCurrencyDenominationsInAtm = new TreeMap<>(new CurrencyDenominationsComparator());
        newCurrencyDenominationsInAtm.putAll(currencyAmount);

        //check this logic.doesnt seem to be working.
        newCurrencyDenominationsInAtm.forEach(
                (key, value) -> existingCurrencyDenominationsInAtm.merge(
                        key, value, (v1, v2) -> v1 - v2
                ));


        double totalAmountInAtmFromCurrency = findTotalAmountInAtmFromCurrency(existingCurrencyDenominationsInAtm);
        atmEntity.setTotalAmount(totalAmountInAtmFromCurrency);
        atmEntity.setAtmId(atmId);
        atmEntity.setCurrencyDenominationsInAtm(existingCurrencyDenominationsInAtm);
        atmRepository.save(atmEntity);

        return true;
    }

    @Override
    public Atm currentAtmState(String atmId) {
        Optional<AtmEntity> atmById = atmRepository.findById(atmId);
        if (!atmById.isPresent()) {
            throw new OutOfServiceException("Unable to find the ATM with id : " + atmId);
        }
        return atmEntityToAtm.convert(atmById.get());
    }

    @Override
    public double totalAmountInAtm(String atmId) {

        Optional<AtmEntity> atmById = atmRepository.findById(atmId);
        if (!atmById.isPresent()) {
            // throw some valid exception
        }

        AtmEntity atmEntity = atmById.get();
        double sum = findTotalAmountInAtmFromCurrency(atmEntity.getCurrencyDenominationsInAtm());
        return sum;
    }

    @Override
    public Map<CurrencyDenominations, Integer> dispenseMoney(String atmId, String accountNumber, Integer pin, Double amount) {
        // we have to use the logic here to dispense the minimum number of currency from the ATM
        log.info("Dispense money request for ATM-ID : {} from accountNumber : {} with Amount : {} received at : {}", atmId, accountNumber, amount, LocalDateTime.now());
        Atm atm = currentAtmState(atmId);
        Map<CurrencyDenominations, Integer> currencyDenominationsIntegerMap = currencyDenominationsUtility.currencyDenominations(atm.getCurrencyDenominationsInAtm(), amount);
        return currencyDenominationsIntegerMap;
    }

    private double findTotalAmountInAtmFromCurrency(Map<CurrencyDenominations, Integer> currencyDenominations) {
        return currencyDenominations.entrySet().stream().mapToDouble(x -> x.getKey().getNumericValue() * x.getValue()).sum();
    }
}
