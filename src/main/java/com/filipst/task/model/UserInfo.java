package com.filipst.task.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserInfo {
    private int employmentPeriod;
    private BigDecimal income;
    private BigDecimal livingCosts;
    private BigDecimal creditObligations;
    private BigDecimal installmentCreditBalance;

    @Override
    public String toString() {
        return String.format(
                "User Information:\n" +
                        "  Employment Period: %d months\n" +
                        "  Income: %s PLN\n" +
                        "  Living Costs: %s PLN\n" +
                        "  Credit Obligations: %s PLN\n" +
                        "  Installment Credit Balance: %s PLN",
                employmentPeriod, income, livingCosts, creditObligations, installmentCreditBalance);
    }
}
