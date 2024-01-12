package com.vision_digital.model.chapters;

import com.vision_digital.model.milestone.ItemMileStone;

import java.util.ArrayList;

public class ItemChapter {

    String id;
    String title;
    String sort_order;
    ItemMileStone itemMileStone;
    String min_month;
    public ArrayList<ItemMileStone> mileStonesList = new ArrayList<>();
    public ArrayList<ItemMileStone> notesMileStone = new ArrayList<>();

    public String getMin_month() {
        return min_month;
    }

    public void setMin_month(String min_month) {
        this.min_month = min_month;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public ItemMileStone getItemMileStone() {
        return itemMileStone;
    }

    public void setItemMileStone(ItemMileStone itemMileStone) {
        this.itemMileStone = itemMileStone;
    }

    public ItemChapter() {
    }

    public ItemChapter(String id, String title, String sort_order, ItemMileStone itemMileStone, String min_month) {
        this.id = id;
        this.title = title;
        this.sort_order = sort_order;
        this.itemMileStone = itemMileStone;
        this.min_month = min_month;
    }


}
