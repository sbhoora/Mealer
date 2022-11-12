package com.example.app;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * This class is an abstraction of a physical menu.
 * It holds menu items to be used by a {@link Cook}.
 */
public class Menu {

    private String title;
    private ArrayList<MenuItem> items;
    private DatabaseReference db;

    /**
     * Constructs a Menu object.
     * @param title
     * @param items
     */
    public Menu(String title, ArrayList<MenuItem> items) {
        this.title = title;
        this.items = items;
    }

    /**
     * Adds items to Menu.
     * Note that the items are not saved in to the database under the cook's field until
     * {@link #save(Cook) save()} is called.
     * @see Cook
     * @param item
     */
    public void addItem(MenuItem item) {
        items.add(item);
    }

    /**
     * Uses {@link DatabaseReference} to save the Menu to the respective cook.
     * Any changes to the menu are only saved to the database once this method has been called.
     * @param cook
     */
    public void save(Cook cook) {
        String key = cook.getEmail();

        // Left to be implemented still
    }

    /**
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return items
     */
    public ArrayList<MenuItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "title='" + title + '\'' +
                ", items=" + items +
                ", db=" + db +
                '}';
    }
}
