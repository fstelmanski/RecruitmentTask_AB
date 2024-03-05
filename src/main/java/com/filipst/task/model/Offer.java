package com.filipst.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Offer {
    private int creditPeriod;
    private BigDecimal monthlyInstallment;
    private BigDecimal creditAmount;

    @Override
    public String toString() {
        return String.format(
                "Credit Period: %d months\n" +
                        "Monthly Installment: %s PLN\n" +
                        "Credit Amount: %s PLN",
                creditPeriod, monthlyInstallment, creditAmount);
    }
}
