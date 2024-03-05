package com.filipst.task.service;

import com.filipst.task.common.CreditConstants;
import com.filipst.task.common.CreditTerms;
import com.filipst.task.exceptions.IncorrectCreditPeriodException;
import com.filipst.task.model.Offer;
import com.filipst.task.model.UserInfo;
import com.google.common.collect.Range;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class OfferServiceImpl implements OfferService{

    private static final Logger logger = LogManager.getLogger(OfferServiceImpl.class);

    @Override
    public int calculateMaxCreditPeriod(int employmentPeriod) {
        return Math.min(employmentPeriod, CreditConstants.UPPER_CREDIT_PERIOD_LIMIT);
    }

    @Override
    public BigDecimal calculateMaxMonthlyInstallment(UserInfo userInfo, CreditConstants.CreditParams parameters) {
        BigDecimal debtToIncomeRatio = BigDecimal.valueOf(parameters.getDti());

        BigDecimal remainingIncome  = userInfo.getIncome().subtract(userInfo.getLivingCosts()).subtract(userInfo.getCreditObligations());

        BigDecimal maxInstallmentConsideringDti  = debtToIncomeRatio.multiply(userInfo.getIncome()).subtract(userInfo.getCreditObligations());

        return remainingIncome.min(maxInstallmentConsideringDti);
    }

    @Override
    public BigDecimal calculateMaxCreditAmount(UserInfo userInfo, CreditTerms creditTerms, BigDecimal maxMonthlyInstallment, int creditPeriod) {
        double monthlyInterestRate = creditTerms.getCreditParams().getInterestRate() / 12.0;

        BigDecimal maxExposure = CreditConstants.MAX_CREDIT_EXPOSURE.subtract(userInfo.getInstallmentCreditBalance());

        BigDecimal creditAmount = BigDecimal.valueOf(
                maxMonthlyInstallment.doubleValue() * (1 - Math.pow(1 + monthlyInterestRate, - creditPeriod)) / monthlyInterestRate);

        return maxExposure.min(CreditConstants.MAX_CREDIT_AMOUNT).min(creditAmount);

    }

    @Override
    public Optional<List<Offer>> calculateCreditOffersForGivenPeriod(UserInfo userInfo) {
        logger.info("Starting calculation of credit offers for user with {}", userInfo.toString());
        int maxCreditPeriod = calculateMaxCreditPeriod(userInfo.getEmploymentPeriod());

        if (maxCreditPeriod < CreditConstants.MIN_CREDIT_PERIOD) {
            logger.info("Finished calculation of credit offers. Insufficient credit score for credit calculation.");
            return Optional.of(Collections.emptyList());
        }

        List<Offer> calculatedOffers = new ArrayList<>();
        Set<Integer> addedIndexes = new HashSet<>();

        Arrays.stream(CreditTerms.values())
                .map(creditTerm -> Math.min(creditTerm.getMaxPeriod(), maxCreditPeriod))
                .filter(creditPeriod -> creditPeriod >= CreditConstants.MIN_CREDIT_PERIOD)
                .forEach(creditPeriod -> addOffer(userInfo, calculatedOffers, addedIndexes, creditPeriod));

        logger.info("Finished calculation of credit offers");
        return Optional.of(calculatedOffers);
    }

    @Override
    public CreditConstants.CreditParams getCreditParamsForPeriod(int creditPeriod) {
        Map<Range<Integer>, CreditConstants.CreditParams> creditParamsMap = new HashMap<>();

        Arrays.stream(CreditTerms.values()).forEach(creditTerm -> {
            Range<Integer> range = Range.closed(creditTerm.getMinPeriod(), creditTerm.getMaxPeriod());
            creditParamsMap.putIfAbsent(range, creditTerm.getCreditParams());
        });

        return creditParamsMap.entrySet().stream()
                .filter(entry -> entry.getKey().contains(creditPeriod))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new IncorrectCreditPeriodException("Incorrect credit period"));
    }

    private void addOffer(UserInfo userInfo, List<Offer> calculatedOffers, Set<Integer> addedIndexes, int creditPeriod) {
        logger.info("Adding offer for credit period: {}", creditPeriod);
        CreditTerms creditConditions = CreditTerms.getCreditTerms(creditPeriod);
        CreditConstants.CreditParams parameters = getCreditParamsForPeriod(creditPeriod);
        BigDecimal monthlyInstallment = calculateMaxMonthlyInstallment(userInfo, parameters);
        BigDecimal creditAmount = calculateMaxCreditAmount(userInfo, creditConditions, monthlyInstallment, creditPeriod);

        if (creditAmount.compareTo(CreditConstants.MIN_CREDIT_AMOUNT) >= 0) {
            int offerIndex = creditConditions.getOfferIndex();

            if (!addedIndexes.contains(offerIndex)) {
                calculatedOffers.add(new Offer(creditPeriod, monthlyInstallment.setScale(2, RoundingMode.HALF_UP), creditAmount.setScale(2, RoundingMode.HALF_UP)));
                addedIndexes.add(offerIndex);
                logger.info("Offer added successfully");
            }
        }
    }

}
