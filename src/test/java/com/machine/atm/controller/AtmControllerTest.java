package com.machine.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.machine.atm.model.Atm;
import com.machine.atm.model.CurrencyDenominations;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AtmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private Map<CurrencyDenominations, Integer> currencyDenominationsMap;

    @BeforeEach
    public void init() {

        mapper = new ObjectMapper();

        currencyDenominationsMap = new TreeMap<>(new CurrencyDenominationsComparator());
        currencyDenominationsMap.put(CurrencyDenominations.FIFTY, 10);
        currencyDenominationsMap.put(CurrencyDenominations.TEN, 40);
        currencyDenominationsMap.put(CurrencyDenominations.TWENTY, 5);
    }

    @Test
    @DisplayName("Load ATM with currency")
    public void testLoadAtmWithCurrency() throws Exception {

        Atm a123 = Atm.builder().atmId("A123").totalAmount(1000).currencyDenominationsInAtm(currencyDenominationsMap).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/loadcurrency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(a123))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get ATM State with AtmId")
    public void testGetATMState() throws Exception {

        Atm a123 = Atm.builder().atmId("A123").totalAmount(1000).currencyDenominationsInAtm(currencyDenominationsMap).build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1/atmstate/atmid/A123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(a123))
        ).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


}