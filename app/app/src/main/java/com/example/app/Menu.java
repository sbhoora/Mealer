package com.example.app;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is an abstraction of a physical menu.
 * It holds menu items to be used by a {@link Cook}.
 */
public class Menu {

    private HashMap<String, MenuItem> offeredMeals;
    private HashMap<String, MenuItem> notOfferedMeals;

    public Menu(){}

    /**
     * <p>By default, the HashMap of items that is passed in is added to the offered meal list.</p>
     * If a menu must be created with certain meals in the not offered meal list, this constructor
     * will have to be called first. Then, {@link #removeFromOfferedMeals(MenuItem)} will have to be
     * called for the meals that wish to be moved.
     * <p></p>
     * @param items
     */
    public Menu(HashMap<String, MenuItem> items) {
        this.notOfferedMeals = items;
    }

    /**
     * <p>Adds a meal to the Menu directly to the offered meal list.</p>
     * <p>Thus, there is no method to add to the Menu without specifying (offered/not offered).</p>
     * <p><b>Note:</b> the items are not saved to the database under the cook's field until
     * {@link Cook#save(Menu) save()} is called.</p>
     * @see Cook
     * @param item
     */
    public void addAsOfferedMeal(MenuItem item) {
        if (!offeredMeals.containsKey(item.getName())) {
            offeredMeals.put(item.getName(), item);
        }
    }

    /**
     * <p>Adds an ArrayList of {@link MenuItem} to the Menu directly to the offered meal list.</p>
     * <p>Thus, there is no method to add to the Menu without specifying (offered/not offered).</p>
     * <p><b>Note:</b> the items are not saved to the database under the cook's field until
     * {@link Cook#save(Menu) save()} is called.</p>
     * @see Cook
     * @see MenuItem
     * @param meals
     */
    public void addAsOfferedMeal(ArrayList<MenuItem> meals) {
        for (MenuItem meal : meals) {
            if (!offeredMeals.containsKey(meal.getName())) {
                offeredMeals.put(meal.getName(), meal);
            }
        }
    }

    /**
     * <p>Adds a meal to the Menu directly to the not offered meal list.</p>
     * <p>Thus, there is no method to add to the Menu without specifying (offered/not offered).</p>
     * <p><b>Note:</b> the items are not saved to the database under the cook's field until
     * {@link Cook#save(Menu) save()} is called.</p>
     * @see Cook
     * @param item
     */
    public void addAsNotOfferedMeal(MenuItem item) {
        if (!notOfferedMeals.containsKey(item.getName())) {
            notOfferedMeals.put(item.getName(), item);
        }
    }

    /**
     * <p>Adds an ArrayList of {@link MenuItem} to the Menu directly to the not offered meal list.</p>
     * <p>Thus, there is no method to add to the Menu without specifying (offered/not offered).</p>
     * <p><b>Note</b> the items are not saved to the database under the cook's field until
     * {@link Cook#save(Menu) save()} is called.</p>
     * @see Cook
     * @see MenuItem
     * @param meals
     */
    public void addAsNotOfferedMeal(ArrayList<MenuItem> meals) {
        for (MenuItem meal : meals) {
            if (!notOfferedMeals.containsKey(meal.getName())) {
                notOfferedMeals.put(meal.getName(), meal);
            }
        }
    }


        /**
         * Moves a MenuItem from the offered meal list to the not offered meal list.
         * <p>Thus, there is no need for a add to (offered/not offered) meal list method.</p>
         * <p><b>Note:</b> the items are not saved to the database under the cook's field until
         * {@link Cook#save(Menu) save()} is called.</p>
         */
    public void removeFromOfferedMeals(MenuItem meal) {
        if (offeredMeals.containsKey(meal.getName())) {
            offeredMeals.remove(meal.getName());
            notOfferedMeals.put(meal.getName(), meal);
        }
    }

    /**
     * Moves a MenuItem from the offered meal list to the not offered meal list.
     * <p>Thus, there is no need for a add to (offered/not offered) meal list method.</p>
     * <p><b>Note:</b> the items are not saved to the database under the cook's field until
     * {@link Cook#save(Menu) save()} is called.</p>
     */
    public void removeFromNotOfferedMeals(MenuItem meal) {
        if (notOfferedMeals.containsKey(meal.getName())) {
            notOfferedMeals.remove(meal.getName());
            offeredMeals.put(meal.getName(), meal);
        }
    }

    /**
     *
     * @return offeredMeals
     */
    public HashMap<String, MenuItem> getOfferedMeals() {
        return offeredMeals;
    }

    /**
     *
     * @return notOfferedMeals
     */
    public HashMap<String, MenuItem> getNotOfferedMeals() {
        return notOfferedMeals;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "offeredMeals=" + offeredMeals +
                ", notOfferedMeals=" + notOfferedMeals +
                '}';
    }
}
