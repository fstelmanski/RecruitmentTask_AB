package com.filipst.task.controller;

import com.filipst.task.model.Offer;
import com.filipst.task.model.UserInfo;
import com.filipst.task.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/calculate")
    public Optional<List<Offer>> calculateCreditOffers(@RequestParam("employmentPeriod") int employmentPeriod,
                                                       @RequestParam("income") BigDecimal income,
                                                       @RequestParam("livingCosts") BigDecimal livingCosts,
                                                       @RequestParam("creditObligations") BigDecimal creditObligations,
                                                       @RequestParam("installmentCreditBalance") BigDecimal installmentCreditBalance) {

        return offerService.calculateCreditOffersForGivenPeriod(
                new UserInfo(employmentPeriod, income, livingCosts, creditObligations, installmentCreditBalance));
    }
}
