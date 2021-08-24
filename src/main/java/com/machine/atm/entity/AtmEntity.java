package com.machine.atm.entity;

import com.machine.atm.model.CurrencyDenominations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "ATM_DETAILS")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtmEntity implements Serializable {

    @Id
    @Column(name = "ATM_ID")
    private String atmId;
    @Column(name = "TOTAL_AMOUNT")
    private double totalAmount;
    @Lob
    @MapKeyColumn(name = "DENOMINATIONS_ID")
    @Column(name = "CURRENCY_DENOMINATIONS")
    @ElementCollection
    private Map<CurrencyDenominations, Integer> currencyDenominationsInAtm;


}
