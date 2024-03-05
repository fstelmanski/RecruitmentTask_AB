package com.filipst.task;


import com.filipst.task.common.CreditConstants;
import com.filipst.task.exceptions.IncorrectCreditPeriodException;
import com.filipst.task.model.Offer;
import com.filipst.task.model.UserInfo;
import com.filipst.task.service.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OfferServiceTest {

    private final OfferService offerService;
    private UserInfo creditWorthyUser, woCreditworthinessUser;

    @Autowired
    public OfferServiceTest(OfferService offerService) {
        this.offerService = offerService;
    }

    @BeforeEach
    void setUp() {
        creditWorthyUser = new UserInfo(10, BigDecimal.valueOf(5000), BigDecimal.valueOf(2000), BigDecimal.valueOf(1000), BigDecimal.valueOf(500));
        woCreditworthinessUser = new UserInfo(1, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Test
    void calculateCreditOffersForGivenPeriod_shouldReturnFourOffers() {
        creditWorthyUser.setEmploymentPeriod(78);

        Optional<List<Offer>> result = offerService.calculateCreditOffersForGivenPeriod(creditWorthyUser);

        assertTrue(result.isPresent());
        assertEquals(4, result.get().size());
    }

    @Test
    void calculateCreditOffersForGivenPeriod_shouldReturnThreeOffers() {
        creditWorthyUser.setEmploymentPeriod(54);

        Optional<List<Offer>> result = offerService.calculateCreditOffersForGivenPeriod(creditWorthyUser);

        assertTrue(result.isPresent());
        assertEquals(3, result.get().size());
    }

    @Test
    void calculateCreditOffersForGivenPeriod_shouldReturnTwoOffers() {
        creditWorthyUser.setEmploymentPeriod(22);

        Optional<List<Offer>> result = offerService.calculateCreditOffersForGivenPeriod(creditWorthyUser);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    void calculateCreditOffersForGivenPeriod_shouldReturnOneOffer() {
        creditWorthyUser.setEmploymentPeriod(7);

        Optional<List<Offer>> result = offerService.calculateCreditOffersForGivenPeriod(creditWorthyUser);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    @Test
    void calculateCreditOffersForGivenPeriod_shouldReturnZeroOffers() {

        Optional<List<Offer>> result = offerService.calculateCreditOffersForGivenPeriod(woCreditworthinessUser);

        assertFalse(result.isEmpty());
    }

    @Test
    void maxCreditPeriodForValidEmploymentPeriod() {
        int employmentPeriod = 56;

        int result = offerService.calculateMaxCreditPeriod(employmentPeriod);

        assertEquals(56, result);
    }

    @Test
    void maxCreditPeriodForExcessiveEmploymentPeriod() {
        int employmentPeriod = 220;

        int result = offerService.calculateMaxCreditPeriod(employmentPeriod);

        assertEquals(100, result);
    }

    @Test
    void calculateMaxMonthlyInstallment_shouldReturnCorrectValue() {
        CreditConstants.CreditParams parameters = CreditConstants.CreditParams.DTI_37_60;

        BigDecimal result = offerService.calculateMaxMonthlyInstallment(creditWorthyUser, parameters);

        assertEquals(BigDecimal.valueOf(1500.0), result);
    }

    @Test
    void calculateCreditOffersForGivenPeriod_shouldReturnOfferList() {
        Optional<List<Offer>> result = offerService.calculateCreditOffersForGivenPeriod(creditWorthyUser);

        assertTrue(result.isPresent());
        assertFalse(result.get().isEmpty());
    }

    @Test
    void getCreditParamsForPeriod_withIncorrectCreditPeriod_throwsException() {
        IncorrectCreditPeriodException exception = assertThrows(IncorrectCreditPeriodException.class, () -> {
            CreditConstants.CreditParams result = offerService.getCreditParamsForPeriod(2);
        });

        assertEquals("Incorrect credit period", exception.getMessage());
    }
}