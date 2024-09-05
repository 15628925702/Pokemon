package org.example.pokemon.model;

import java.util.ArrayList;
import java.util.List;

public class BackPack {

    private List<Item> items;
    private int capacity;

    public BackPack(int capacity) {
        items = new ArrayList<Item>(capacity + 1);
        this.capacity = capacity;
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public void addItem(Item item) {
        if (items.size() < capacity) {
            items.add(item);
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Item> initData () {
        Item item = new Item();
        item.setName("回复药");
        item.setType("药草和果树");
        item.setDescription("回复200点血量");
        item.setAmount(5);
        item.setImageUrl("https://pokemon-1326430649.cos.ap-chongqing.myqcloud.com/item%2F%E7%9F%BF%E6%B3%89%E6%B0%B4.png");
        items.add(item);
        for (int i = 0; i < 19; i++) {
            item = new Item();
            item.setName("矿泉水");
            item.setType("药草和果树");
            item.setDescription("回复500点血量");
            item.setAmount(1);
            item.setImageUrl("https://pokemon-1326430649.cos.ap-chongqing.myqcloud.com/item%2F%E5%9B%9E%E5%A4%8D%E8%8D%AF.png");
            items.add(item);
        }

        return items;
    }

}
