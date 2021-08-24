package com.machine.atm.controller;

import com.machine.atm.model.MoneyWithdrawResponse;
import com.machine.atm.model.UserAccountDetails;
import com.machine.atm.model.UserAccountDetailsResponse;
import com.machine.atm.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("v1")
public class BankController {

    private BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/bank/check/balance")
    public ResponseEntity<UserAccountDetailsResponse> checkBankAccountBalance(@RequestBody UserAccountDetails userAccountDetails) {
        log.info("Checking account balance for account : {}", userAccountDetails.getAccountNumber());
        UserAccountDetailsResponse userAccountDetailsResponse = bankService.checkBalance(userAccountDetails.getAccountNumber(), userAccountDetails.getAccountPin());
        return ResponseEntity.ok(userAccountDetailsResponse);

    }

    @PostMapping("/bank/withdraw")
    public ResponseEntity<MoneyWithdrawResponse> withdrawAccountBalance(@RequestBody UserAccountDetails userAccountDetails) {
        log.info("Withdrawing amount : {} from account : {}", userAccountDetails.getAmountToWithdraw(), userAccountDetails.getAccountNumber());
        MoneyWithdrawResponse moneyWithdrawResponse = bankService.withdrawAmount(userAccountDetails.getAccountNumber(), userAccountDetails.getAccountPin(), userAccountDetails.getAmountToWithdraw());
        return ResponseEntity.ok(moneyWithdrawResponse);
    }


}
