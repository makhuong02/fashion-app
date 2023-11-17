package com.group25.ecommercefashionapp.data;

import java.util.ArrayList;
import java.util.List;

public class ActionItem extends Item {

    public ActionItem(String name) {
        super(name);
    }

    public static List<ActionItem> getActionItems() {
        List<ActionItem> items = new ArrayList<>();
        items.add(new ActionItem("Store Locator"));
        items.add(new ActionItem("Getting Started"));
        items.add(new ActionItem("FAQs"));
        items.add(new ActionItem("Terms of use"));
        items.add(new ActionItem("Privacy policy"));
        return items;
    }
}
