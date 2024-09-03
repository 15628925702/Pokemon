package org.example.pokemon.battle;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class BattleTool {
    //属性
    private String toolName;    //道具名
    private int toolEffect; //道具效果 0为加血 1为加技能次数
    private int toolLevel;  //道具等级
    private int toolNumber; //道具数量

    //构造方法
    public BattleTool(String toolName, int toolEffect, int toolLevel) {}

    //getter setter
    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public int getToolEffect() {
        return toolEffect;
    }

    public void setToolEffect(int toolEffect) {
        this.toolEffect = toolEffect;
    }

    public int getToolLevel() {
        return toolLevel;
    }

    public void setToolLevel(int toolLevel) {
        this.toolLevel = toolLevel;
    }

    public int getToolNumber() {
        return toolNumber;
    }

    public void setToolNumber(int toolNumber) {
        this.toolNumber = toolNumber;
    }

    //消耗道具
    public void minusToolNumber() {
        this.toolNumber--;
    }

    //获取道具信息
    public void getToolDataFromDb(String toolName) throws IOException {
        File toolDb = new File("src/main/java/org/example/pokemon/battle/ToolDatabase.txt");
        Scanner input = new Scanner(toolDb);
        while(input.hasNext()){
            if(input.next().equals(toolName)){
                this.toolName = input.next();
                this.toolEffect = input.nextInt();
                this.toolLevel = input.nextInt();
                this.toolNumber = input.nextInt();
                break;
            }
        }
    }

    //使用道具
    public void useTool(){

    }
}
