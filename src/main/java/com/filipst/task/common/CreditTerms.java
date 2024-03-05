package com.filipst.task.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Enum representing credit terms for different periods.
 */
@Getter
@AllArgsConstructor
public enum CreditTerms {
    PERIOD_6_12(6, 12, 0, CreditConstants.CreditParams.DTI_6_12),
    PERIOD_13_36(13, 36, 1, CreditConstants.CreditParams.DTI_13_36),
    PERIOD_37_60(37, 60, 2, CreditConstants.CreditParams.DTI_37_60),
    PERIOD_61_100(61, 100, 3, CreditConstants.CreditParams.DTI_61_100);

    private final int minPeriod;
    private final int maxPeriod;
    private final int offerIndex;
    private final CreditConstants.CreditParams creditParams;

    /**
     * Get the CreditTerms for a given maxCreditPeriod.
     * @param maxCreditPeriod Maximum credit period.
     * @return Matching CreditTerms enum for the specified period.
     * @throws IllegalArgumentException if no offer is available for the selected period.
     */
    public static CreditTerms getCreditTerms(int maxCreditPeriod) {
        return Arrays.stream(values())
                .filter(terms -> terms.isPeriodInRange(maxCreditPeriod))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No offer for selected period"));
    }

    /**
     * Check if the specified period is within the range of this CreditTerm.
     * @param maxCreditPeriod Maximum credit period to check.
     * @return True if the period is within the range, false otherwise.
     */
    private boolean isPeriodInRange(int maxCreditPeriod) {
        return maxCreditPeriod >= minPeriod && maxCreditPeriod <= maxPeriod;
    }
}
