package org.example.pokemon.model;

import java.util.ArrayList;
import java.util.List;

public class BackPack {

    private List<Item> items;
    private int capacity;
    private Item item;

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
        item = new Item();

        item.setName("妙蛙种子");
        item.setType("草");
        item.setDescription("它的身影。曾有一段时期，人们认为它已经灭绝了。\n在出生后的一段时间内，\n它会吸收背上种子里储存着的营养成长。");
        item.setImageUrl("/spirit_pic/妙蛙种子.png");
        items.add(item);

        item = new Item();
        item.setName("杰尼龟");
        item.setType("水");
        item.setDescription("当它把长长的脖子缩回壳里时，会顺势发射出力道强劲的水枪。\n当它遇到危险的时候，会将四肢收回龟壳里保护自己，同时从嘴里喷出水来。");
        item.setImageUrl("/spirit_pic/杰尼龟.png");
        items.add(item);

        item = new Item();
        item.setName("皮卡丘");
        item.setType("电");
        item.setDescription("有积存电力的特质。在皮卡丘群居的森林里会落雷不断，十分危险。\n皮卡丘们把尾巴贴在一起交换电流，其实是在互相打招呼。");
        item.setImageUrl("/spirit_pic/皮卡丘.png");
        items.add(item);

        item = new Item();
        item.setName("小火龙");
        item.setType("火");
        item.setDescription("如果它在战斗中亢奋起来，就会喷出灼热的火焰，把周围的东西烧得一干二净。");
        item.setImageUrl("/spirit_pic/小火龙.png");
        items.add(item);

        return items;
    }

}