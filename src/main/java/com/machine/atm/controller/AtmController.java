package com.machine.atm.controller;

import com.machine.atm.model.Atm;
import com.machine.atm.service.AtmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("v1")
public class AtmController {

    private AtmService atmService;

    @Autowired
    public AtmController(AtmService atmService) {
        this.atmService = atmService;
    }

    @PostMapping("/loadcurrency")
    public ResponseEntity<Boolean> loadAtmWithCurrency(@RequestBody Atm atm) {

        boolean response = atmService.addAmount(atm.getAtmId(), atm.getTotalAmount(), atm.getCurrencyDenominationsInAtm());
        if (response) {
            log.info("Amount loaded : {} in ATM with id : {}", atm.getTotalAmount(), atm.getAtmId());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/atmstate/atmid/{atmId}")
    public ResponseEntity<Atm> currentAtmState(@PathVariable("atmId") String atmId) {

        Atm atm = atmService.currentAtmState(atmId);
        log.info("Total amount in ATM with id : {} is {}", atm.getAtmId(), atm.getTotalAmount());
        return ResponseEntity.ok(atm);
    }
}
