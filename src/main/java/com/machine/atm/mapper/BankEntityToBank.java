package com.machine.atm.mapper;

import com.machine.atm.entity.BankEntity;
import com.machine.atm.model.Bank;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BankEntityToBank implements Converter<BankEntity, Bank> {
    @Override
    public Bank convert(BankEntity bankEntity) {
        return Bank.builder().accountNumber(bankEntity.getAccountNumber())
                .accountPin(bankEntity.getAccountPin())
                .balance(bankEntity.getBalance())
                .overdraft(bankEntity.getOverdraft()).build();
    }
}
