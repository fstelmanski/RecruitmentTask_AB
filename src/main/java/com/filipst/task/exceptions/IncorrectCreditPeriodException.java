package com.filipst.task.exceptions;

public class IncorrectCreditPeriodException extends IllegalArgumentException {
    public IncorrectCreditPeriodException(String message) {
        super(message);
    }
}