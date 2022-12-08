package com.example.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

public class DeliverableFourUnitTest {
    Menu menu = new Menu();
    ArrayList list = new ArrayList();
    ArrayList list2 = new ArrayList();

    MenuItem meal = new MenuItem("Pasta", "qw", Type.ENTREE , CuisineType.EUROPEAN, list, list2, 12.3, "WOW");

    @Test
    public void addToMenu() {
        menu.addAsOfferedMeal(meal);
        assertEquals(true, menu.isInOffered(meal));
        assertEquals(false, menu.isInNotOffered(meal));
    }

    @Test
    public void removeWhileOffered() {
        menu.addAsOfferedMeal(meal);
        assertEquals(false, menu.deleteMeal(meal));
    }

    @Test
    public void moveToOffered() {
        menu.addAsOfferedMeal(meal);
        menu.removeFromOfferedMeals(meal);
        assertEquals(true, menu.isInNotOffered(meal));
        assertEquals(false, menu.isInOffered(meal));
    }

    @Test
    public void removeMeal() {
        menu.addAsOfferedMeal(meal);
        menu.removeFromOfferedMeals(meal);
        assertEquals(true, menu.deleteMeal(meal));
    }
}
