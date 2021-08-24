package com.machine.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.machine.atm.model.CurrencyDenominations;
import com.machine.atm.model.UserAccountDetails;
import com.machine.atm.utility.CurrencyDenominationsComparator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.TreeMap;

@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private Map<CurrencyDenominations, Integer> currencyDenominationsMap;

    private UserAccountDetails userAccountDetails;

    @BeforeEach
    public void init() {

        mapper = new ObjectMapper();

        currencyDenominationsMap = new TreeMap<>(new CurrencyDenominationsComparator());
        currencyDenominationsMap.put(CurrencyDenominations.FIFTY, 10);
        currencyDenominationsMap.put(CurrencyDenominations.TEN, 40);
        currencyDenominationsMap.put(CurrencyDenominations.TWENTY, 5);

        userAccountDetails = UserAccountDetails.builder().accountNumber("123456789").accountPin(1234).amountToWithdraw(100).build();
    }

    @Test
    @DisplayName("Checking bank balance from the existing account")
    public void testCheckBankBalance() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/bank/check/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(userAccountDetails))
        ).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Checking bank balance from the non-existing account")
    public void testCheckBankBalanceWrongAccount() throws Exception {
        userAccountDetails.setAccountNumber("X123456789");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/bank/check/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(userAccountDetails))
        ).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Withdrawing amount from the existing account")
    public void testWithdrawAmount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/bank/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(userAccountDetails))
        ).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Withdrawing amount from the non-existing account")
    public void testWithdrawAmountWrongAccount() throws Exception {
        userAccountDetails.setAccountNumber("X123456789");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/bank/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(userAccountDetails))
        ).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

}