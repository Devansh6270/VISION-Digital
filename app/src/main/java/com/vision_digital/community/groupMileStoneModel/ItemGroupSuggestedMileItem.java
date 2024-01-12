package com.vision_digital.community.groupMileStoneModel;

import com.vision_digital.model.milestone.ItemMileStone;

import java.util.ArrayList;

public class ItemGroupSuggestedMileItem {
    long startedAt;
    long endAt;
    ArrayList<ItemMileStone> mileStoneArrayList = new ArrayList<>();


    public ItemGroupSuggestedMileItem(long startedAt, long endAt, ArrayList<ItemMileStone> mileStoneArrayList) {
        this.startedAt = startedAt;
        this.endAt = endAt;
        this.mileStoneArrayList = mileStoneArrayList;
    }

    public ItemGroupSuggestedMileItem() {
    }

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
}
