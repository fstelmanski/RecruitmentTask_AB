package com.filipst.task;

import com.filipst.task.controller.OfferController;
import com.filipst.task.model.Offer;
import com.filipst.task.model.UserInfo;
import com.filipst.task.service.OfferService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OfferController.class)
class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    @InjectMocks
    private OfferController offerController;


    @Test
    void calculateCreditOffers_shouldReturnCreditOffers() throws Exception {
        UserInfo userInfo = new UserInfo(24, BigDecimal.valueOf(5000), BigDecimal.valueOf(2000),
                BigDecimal.valueOf(1000), BigDecimal.valueOf(500));

        List<Offer> offers = Arrays.asList(
                new Offer(12, BigDecimal.valueOf(400), BigDecimal.valueOf(5000)),
                new Offer(24, BigDecimal.valueOf(200), BigDecimal.valueOf(3000))
        );

        when(offerService.calculateCreditOffersForGivenPeriod(userInfo)).thenReturn(Optional.of(offers));

        mockMvc.perform(post("/offers/calculate")
                        .param("employmentPeriod", "24")
                        .param("income", "5000")
                        .param("livingCosts", "2000")
                        .param("creditObligations", "1000")
                        .param("installmentCreditBalance", "500")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].creditPeriod").value(12))
                .andExpect(jsonPath("$[0].monthlyInstallment").value(400))
                .andExpect(jsonPath("$[0].creditAmount").value(5000))
                .andExpect(jsonPath("$[1].creditPeriod").value(24))
                .andExpect(jsonPath("$[1].monthlyInstallment").value(200))
                .andExpect(jsonPath("$[1].creditAmount").value(3000));
    }

    @Test
    void calculateCreditOffers_shouldReturnEmptyList() throws Exception {
        Mockito.when(offerService.calculateCreditOffersForGivenPeriod(Mockito.any(UserInfo.class)))
                .thenReturn(Optional.of(Collections.emptyList()));

        mockMvc.perform(post("/offers/calculate")
                        .param("employmentPeriod", "10")
                        .param("income", "5000")
                        .param("livingCosts", "2000")
                        .param("creditObligations", "1000")
                        .param("installmentCreditBalance", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

