package com.vision_classes.model.suggestedMileStone;

import com.vision_classes.model.milestone.ItemMileStone;

import java.util.ArrayList;

public class ItemSuggestedMileStones {

    long startedAt;
    long endAt;
    ArrayList<ItemMileStone> mileStoneArrayList = new ArrayList<>();

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public ArrayList<ItemMileStone> getMileStoneArrayList() {
        return mileStoneArrayList;
    }

    public void setMileStoneArrayList(ArrayList<ItemMileStone> mileStoneArrayList) {
        this.mileStoneArrayList = mileStoneArrayList;
    }

    public ItemSuggestedMileStones() {
    }

    public ItemSuggestedMileStones(long startedAt, long endAt, ArrayList<ItemMileStone> mileStoneArrayList) {
        this.startedAt = startedAt;
        this.endAt = endAt;
        this.mileStoneArrayList = mileStoneArrayList;
    }
}
