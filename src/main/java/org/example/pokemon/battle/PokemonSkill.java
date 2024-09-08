package org.example.pokemon.battle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class PokemonSkill {
    //技能参数
    private String skillName;   //技能名
    private int skillType;  //技能种类
    private int skillAttribute; //技能属性
    private int skillTimes; //技能次数
    private int skillHitRate;   //技能命中率
    private int skillPower; //技能威力
    private int skillBuff;  //技能效果
    private int buffRound;  //buff持续轮数

    //构造函数
    public PokemonSkill() {
    }

    public PokemonSkill(String skillName, int skillType, int skillAttribute, int skillTimes, int skillHitRate, int skillPower) {
        this.skillName = skillName;
        this.skillType = skillType;
        this.skillAttribute = skillAttribute;
        this.skillTimes = skillTimes;
        this.skillHitRate = skillHitRate;
        this.skillPower = skillPower;
    }

    //getter setter
    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getSkillType() {
        return skillType;
    }

    public void setSkillType(int skillType) {
        this.skillType = skillType;
    }

    public int getSkillTimes() {
        return skillTimes;
    }

    public void setSkillTimes(int skillTimes) {
        this.skillTimes = skillTimes;
    }

    public int getSkillAttribute() {
        return skillAttribute;
    }

    public void setSkillAttribute(int skillAttribute) {
        this.skillAttribute = skillAttribute;
    }

    public int getSkillHitRate() {
        return skillHitRate;
    }

    public void setSkillHitRate(int skillHitRate) {
        this.skillHitRate = skillHitRate;
    }

    public int getSkillPower() {
        return skillPower;
    }

    public void setSkillPower(int skillPower) {
        this.skillPower = skillPower;
    }

    public int getSkillBuff() {
        return skillBuff;
    }

    public void setSkillBuff(int skillBuff) {
        this.skillBuff = skillBuff;
    }

    public int getBuffRound() {
        return buffRound;
    }

    public void setBuffRound(int buffRound) {
        this.buffRound = buffRound;
    }

    //读取技能文件
    public void getPokeSkillInfoFromDb(String skillName) throws IOException {
        // 使用类路径访问文件
        InputStream inputStream = getClass().getResourceAsStream("/PokeSkillDatabase.txt");
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: /PokeSkillDatabase.txt");
        }
        Scanner input = new Scanner(inputStream);
        while (input.hasNext()) {
            if (input.next().equals(skillName)) {
                this.skillName = skillName;
                this.skillType = input.nextInt();
                this.skillAttribute = input.nextInt();
                this.skillTimes = input.nextInt();
                this.skillHitRate = input.nextInt();
                this.skillPower = input.nextInt();
                break;
            }
        }
        input.close();
    }

    //显示技能信息
    public void showPokeSkillInfo() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "PokemonSkill{" +
                "skillName='" + skillName + '\'' +
                ", skillType=" + skillType +
                ", skillAttribute=" + skillAttribute +
                ", skillTimes=" + skillTimes +
                ", skillHitRate=" + skillHitRate +
                ", skillPower=" + skillPower +
                ", skillBuff=" + skillBuff +
                ", buffRound=" + buffRound +
                '}';
    }

    //技能是否命中
    public boolean isHit() {
        double tempNum = this.skillHitRate * 0.01;
        double randomNum = Math.random();
        return randomNum < tempNum;
    }

    //技能次数减少
    public void skillTimesMinus() {
        this.skillTimes -= 1;
    }

}