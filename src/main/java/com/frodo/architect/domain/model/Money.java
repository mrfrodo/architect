package com.frodo.architect.domain.model;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * A DDD value object. It is immutable. It is a record
 * @param amount
 * @param currency
 */

public record Money(BigDecimal amount, Currency currency) {
}