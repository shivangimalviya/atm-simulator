package com.machine.atm;

import com.machine.atm.entity.BankEntity;
import com.machine.atm.model.CurrencyDenominations;
import com.machine.atm.repository.BankRepository;
import com.machine.atm.service.AtmService;
import com.machine.atm.utility.CurrencyDenominationsComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.TreeMap;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private AtmService atmService;

    @Autowired
    private BankRepository bankRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initializing data
        Map<CurrencyDenominations, Integer> currencyDenominations = new TreeMap<>(new CurrencyDenominationsComparator());
        currencyDenominations.put(CurrencyDenominations.FIFTY, 10);
        currencyDenominations.put(CurrencyDenominations.TWENTY, 30);
        currencyDenominations.put(CurrencyDenominations.TEN, 30);
        currencyDenominations.put(CurrencyDenominations.FIVE, 20);

        atmService.initializeAmount("A123", 1500, currencyDenominations);

        BankEntity bac1 = BankEntity.builder().accountNumber("123456789").accountPin("1234").balance(800.0).overdraft(200.0).build();
        BankEntity bac2 = BankEntity.builder().accountNumber("987654321").accountPin("4321").balance(1230.0).overdraft(150.0).build();

        bankRepository.save(bac1);
        bankRepository.save(bac2);

    }
}
