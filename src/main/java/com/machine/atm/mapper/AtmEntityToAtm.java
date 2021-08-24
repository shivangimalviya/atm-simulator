package com.machine.atm.mapper;


import com.machine.atm.entity.AtmEntity;
import com.machine.atm.model.Atm;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AtmEntityToAtm implements Converter<AtmEntity, Atm> {
    @Override
    public Atm convert(AtmEntity atmEntity) {
        return Atm.builder().atmId(atmEntity.getAtmId())
                .totalAmount(atmEntity.getTotalAmount())
                .currencyDenominationsInAtm(atmEntity.getCurrencyDenominationsInAtm())
                .build();
    }
}
