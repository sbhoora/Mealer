package com.example.app;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class MenuTest {

//    private Menu newMenu() {
//        ArrayList<MenuItem> items = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            MenuItem item = new MenuItem("n" + i,"", "a" + i );
//            items.add(item);
//        }
//
//       // Menu menu = new Menu("My Menu", items);
//
//       // return menu;
//    }
    @Test
    public void retrievesMenu() {
        // This cannot be tested as unit tests I found out - Matsuru
        // So I will leave it there just the sake of it, if a solution can be found.
        // The reason it does not work is because of the Firebase calls.
        // They run asynchronously and don't work properly with the tests.
        // It always crashes.
//        Menu menu = newMenu();
//        Cook alfredo = new Cook("alfredolinguini@pastacom", "pasta", false, false);
//
//        alfredo.save(menu);
//
//        assertEquals(menu, alfredo.getMenu());
    }
}
