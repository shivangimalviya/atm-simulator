package com.machine.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "BANK_ACCOUNT_DETAILS")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankEntity implements Serializable {

    @Id
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name = "ACCOUNT_PIN")
    private String accountPin;
    @Column(name = "ACCOUNT_BALANCE")
    private Double balance;
    @Column(name = "OVERDRAFT_BALANCE")
    private Double overdraft;

}
