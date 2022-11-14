package com.example.app;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class MenuTest {

    private Menu newMenu() {
        ArrayList<MenuItem> items = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            MenuItem item = new MenuItem("n" + i,"", "a" + i );
            items.add(item);
        }

        Menu menu = new Menu("My Menu", items);

        return menu;
    }
    @Test
    public void retrievesMenu() {
        Menu menu = newMenu();
        Cook alfredo = new Cook("alfredolinguini@pastacom", "pasta", false, false);

        alfredo.save(menu);

        assertEquals(menu, alfredo.getMenu());
    }
}
