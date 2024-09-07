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

        item.setName("榴石果");
        item.setType("树果");
        item.setDescription("宝可梦会变得非常容易和训练家亲密，但ＨＰ的基础点数会降低。");
        item.setAmount(5);
        item.setImageUrl("/item_pic/榴石果.png");
        items.add(item);

        item = new Item();
        item.setName("大师球");
        item.setType("精灵球");
        item.setDescription("必定能捉到野生宝可梦的，性能最好的球");
        item.setAmount(5);
        item.setImageUrl("/item_pic/大师球.png");
        items.add(item);

        item = new Item();
        item.setName("大胆薄荷");
        item.setType("药物");
        item.setDescription("防御提高");
        item.setAmount(5);
        item.setImageUrl("/item_pic/大胆薄荷.png");
        items.add(item);

        item = new Item();
        item.setName("腐朽的剑");
        item.setType("进化道具");
        item.setDescription("据说很久以前，英雄就是拿着这把剑驱走了灾厄。而现在早已变得锈迹斑斑。");
        item.setAmount(5);
        item.setImageUrl("/item_pic/腐朽的剑.png");
        items.add(item);

        item = new Item();
        item.setName("腐朽的盾");
        item.setType("进化道具");
        item.setDescription("据说很久以前，英雄就是拿着这把剑驱走了灾厄。而现在早已变得锈迹斑斑。");
        item.setAmount(5);
        item.setImageUrl("/item_pic/腐朽的盾.png");
        items.add(item);

        item = new Item();
        item.setName("要害攻击");
        item.setType("战斗道具");
        item.setDescription("击中要害的几率会大幅提高。只能使用１次。离场后，效果便会消失。");
        item.setAmount(5);
        item.setImageUrl("/item_pic/要害攻击.png");
        items.add(item);

        item = new Item();
        item.setName("酸酸苹果");
        item.setType("进化道具");
        item.setDescription("这种神奇的苹果可以使特定的宝可梦进化。吃起来酸酸的。");
        item.setAmount(5);
        item.setImageUrl("/item_pic/酸酸苹果.png");
        items.add(item);

        item = new Item();
        item.setName("HP增强剂");
        item.setType("药物");
        item.setDescription("宝可梦的营养饮料。能提高１只宝可梦的ＨＰ的基础点数。");
        item.setAmount(5);
        item.setImageUrl("/item_pic/HP增强剂.png");
        items.add(item);

        item = new Item();
        item.setName("PP提升剂");
        item.setType("药物");
        item.setDescription("能让宝可梦学会的其中１个招式的ＰＰ最大值少量提高。");
        item.setAmount(5);
        item.setImageUrl("/item_pic/PP提升剂.png");
        items.add(item);

        return items;
    }

}
