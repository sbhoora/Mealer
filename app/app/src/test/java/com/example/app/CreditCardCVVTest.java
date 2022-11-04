package com.example.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreditCardCVVTest {
    @Test
    public void cvv_is_valid() {
        assertTrue(CreditCard.isValidCVV("564"));
    }
}
