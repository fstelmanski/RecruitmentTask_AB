package com.filipst.task.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

public class CreditConstants {
    /** Upper limit for the credit period. */
    public static final int UPPER_CREDIT_PERIOD_LIMIT = 100;

    /** Maximum allowed credit amount. */
    public static final BigDecimal MAX_CREDIT_AMOUNT = new BigDecimal(150000);

    /** Maximum exposure allowed for the user. */
    public static final BigDecimal MAX_CREDIT_EXPOSURE = new BigDecimal(200000);

    /** Minimum credit period allowed. */
    public static final int MIN_CREDIT_PERIOD = 6;

    /** Minimum allowed credit amount. */
    public static final BigDecimal MIN_CREDIT_AMOUNT = new BigDecimal(5000);

    /**
     * Enum representing credit parameters for different periods.
     * Each entry has debt-to-income ratio (DTI) and interest rate values.
     */
    @Getter
    @AllArgsConstructor
    public enum CreditParams {
        DTI_6_12(0.6, 0.02),
        DTI_13_36(0.6, 0.03),
        DTI_37_60(0.5, 0.03),
        DTI_61_100(0.55, 0.03);

        private final double dti;
        private final double interestRate;

    }
}
