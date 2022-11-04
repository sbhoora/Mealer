package com.example.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreditCardTest {

    CreditCard testCC = new CreditCard(
            "Tom Mater",
            "1234123412341234",
            "123",
            "0812");

    @Test
    public void cardNumLengthIsCorrect() {
        assertEquals(16, testCC.getCardNumber().length());
    }

    @Test
    public void expLengthIsCorrect() {
        assertEquals(4, testCC.getExp().length());
    };

    @Test
    public void ccLengthIsCorrect() {
        assertEquals(3, testCC.getCvv().length());
    }

    @Test
    public void getCardNameIsCorrect() {
        assertEquals("Tom Mater", testCC.getCardName());
    }


}
