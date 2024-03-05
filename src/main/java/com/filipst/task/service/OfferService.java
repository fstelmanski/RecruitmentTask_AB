package com.filipst.task.service;

import com.filipst.task.common.CreditConstants;
import com.filipst.task.common.CreditTerms;
import com.filipst.task.model.Offer;
import com.filipst.task.model.UserInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OfferService {

    int calculateMaxCreditPeriod(int employmentPeriod);

    BigDecimal calculateMaxMonthlyInstallment(UserInfo userInfo, CreditConstants.CreditParams parameters);

    BigDecimal calculateMaxCreditAmount(UserInfo userInfo, CreditTerms creditTerms, BigDecimal maxMonthlyInstallment, int creditPeriod);

    Optional<List<Offer>> calculateCreditOffersForGivenPeriod(UserInfo userInfo);

    CreditConstants.CreditParams getCreditParamsForPeriod(int creditPeriod);

}
