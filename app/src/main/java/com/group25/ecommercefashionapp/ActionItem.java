package com.group25.ecommercefashionapp;
import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActionItem {
    private final String name;

    public ActionItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<ActionItem> getActionItems(){
        List<ActionItem> items = new ArrayList<>();
        items.add(new ActionItem("Store Locator"));
        items.add(new ActionItem("Getting Started"));
        items.add(new ActionItem("FAQs"));
        items.add(new ActionItem("Terms of use"));
        items.add(new ActionItem("Privacy policy"));
        return items;
    }
}
