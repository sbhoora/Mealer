package com.example.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

public class DeliverableThreeUnitTests {
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    MenuItem meal1 = new MenuItem("Pasta", "qw", Type.ENTREE , CuisineType.EUROPEAN, list, list2, 12.3, "WOW");


    @Test
    public void mealCreation() {

        assertEquals("Pasta", meal1.getName());
        assertEquals("ENTREE", meal1.getType());
        assertEquals("EUROPEAN", meal1.getCuisineType());
    }

    @Test
    public void addAnIngredient() {
        meal1.addIngredient("Tomatoes");
        assertEquals("Tomatoes", meal1.getIngredients().get(0));
    }

    @Test
    public void addAnAllergen() {
        meal1.addAllergen("Peanuts");
        assertEquals("Peanuts", meal1.getAllergens().get(0));
    }

    @Test
    public void changeMealType() {
        meal1.setType(Type.APPETIZER);
        assertEquals("APPETIZER", meal1.getType());
    }

    // Deliverable 2 test cases:
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
    }
    @Test
    public void ccLengthIsCorrect() {
        assertEquals(3, testCC.getCvv().length());
    }

    @Test
    public void getCardNameIsCorrect() {
        assertEquals("Tom Mater", testCC.getCardName());
    }



}
