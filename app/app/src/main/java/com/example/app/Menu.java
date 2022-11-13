package com.example.app;

import java.util.ArrayList;

/**
 * This class is an abstraction of a physical menu.
 * It holds menu items to be used by a {@link Cook}.
 */
public class Menu {

    private String title;
    private ArrayList<MenuItem> items;

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
     * {@link Cook#save(Menu) save()} is called.
     * @see Cook
     * @param item
     */
    public void addItem(MenuItem item) {
        items.add(item);
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
                '}';
    }
}
