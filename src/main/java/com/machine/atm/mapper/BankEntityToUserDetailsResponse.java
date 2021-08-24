package com.machine.atm.mapper;

import com.machine.atm.entity.BankEntity;
import com.machine.atm.model.UserAccountDetailsResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BankEntityToUserDetailsResponse implements Converter<BankEntity, UserAccountDetailsResponse> {
    @Override
    public UserAccountDetailsResponse convert(BankEntity bankEntity) {
        return UserAccountDetailsResponse.builder().accountNumber(bankEntity.getAccountNumber())
                .balance(bankEntity.getBalance())
                .overdraft(bankEntity.getOverdraft())
                .withdrawableBalance(bankEntity.getBalance() + bankEntity.getOverdraft())
                .build();
    }
}
