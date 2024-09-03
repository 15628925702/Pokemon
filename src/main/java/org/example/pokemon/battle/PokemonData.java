package org.example.pokemon.battle;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

//定义宝可梦的基本属性
public class PokemonData{
    //名字
    private String pokemonName;
    //属性
    private int pokemonType;
    //面板
    private int hp; //生命值
    private int speed;  //速度
    private int physical_attack;    //攻击力
    private int physical_defense;   //防御力
    private int special_attack; //特攻
    private int special_defense;    //特防
    //技能
    private PokemonSkill[] skillsOfPokes = new PokemonSkill[4];
    //属性克制
    public static int[][] typeRelation = {{0,0,0,0},{0,-1,-1,1},{0,1,-1,-1},{0,-1,1,-1}};

    //构造函数
    public PokemonData() {
    }

    public PokemonData(String pokemonName, int pokemonType, int hp, int speed, int physical_attack, int physical_defense, int special_attack, int special_defense, PokemonSkill[] skillsOfPokes) {
        this.pokemonName = pokemonName;
        this.pokemonType = pokemonType;
        this.hp = hp;
        this.speed = speed;
        this.physical_attack = physical_attack;
        this.physical_defense = physical_defense;
        this.special_attack = special_attack;
        this.special_defense = special_defense;
        this.skillsOfPokes = skillsOfPokes;
    }

    //getter setter
    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public int getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(int pokemonType) {
        this.pokemonType = pokemonType;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPhysical_attack() {
        return physical_attack;
    }

    public void setPhysical_attack(int physical_attack) {
        this.physical_attack = physical_attack;
    }

    public int getPhysical_defense() {
        return physical_defense;
    }

    public void setPhysical_defense(int physical_defense) {
        this.physical_defense = physical_defense;
    }

    public int getSpecial_attack() {
        return special_attack;
    }

    public void setSpecial_attack(int special_attack) {
        this.special_attack = special_attack;
    }

    public int getSpecial_defense() {
        return special_defense;
    }

    public void setSpecial_defense(int special_defense) {
        this.special_defense = special_defense;
    }

    public PokemonSkill[] getSkillsOfPokes() {
        return skillsOfPokes;
    }

    public void setSkillsOfPokes(PokemonSkill[] skillsOfPokes) {
        this.skillsOfPokes = skillsOfPokes;
    }

    public static int[][] getTypeRelation() {
        return typeRelation;
    }

    //深拷贝
    public void clonePokeData(PokemonData poke){
        this.pokemonName = poke.pokemonName;
        this.pokemonType = poke.pokemonType;
        this.hp = poke.hp;
        this.speed = poke.speed;
        this.physical_attack = poke.physical_attack;
        this.physical_defense = poke.physical_defense;
        this.special_attack = poke.special_attack;
        this.special_defense = poke.special_defense;

        //操作新数组
        this.skillsOfPokes = new PokemonSkill[4];
        for(int i = 0; i < 4; i++){
            this.skillsOfPokes[i] = poke.skillsOfPokes[i];
        }
    }

    //读取宝可梦信息
    public void getPokeDataFromDb(String pokemonName) throws IOException {
        File pokeDb = new File("src/main/java/org/example/pokemon/battle/PokeDatabase.txt");
        Scanner input = new Scanner(pokeDb);
        while(input.hasNext()){
            if(input.next().equals(pokemonName)){
                this.pokemonName = pokemonName;
                this.pokemonType = input.nextInt();
                this.hp = input.nextInt();
                this.speed = input.nextInt();
                this.physical_attack=input.nextInt();
                this.physical_defense=input.nextInt();
                this.special_attack=input.nextInt();
                this.special_defense=input.nextInt();
                break;
            }
        }
    }

    //显示宝可梦信息
    public void showPokeInfo(){
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "PokemonData{" +
                "pokemonName='" + pokemonName + '\'' +
                ", pokemonType=" + pokemonType +
                ", hp=" + hp +
                ", speed=" + speed +
                ", physical_attack=" + physical_attack +
                ", physical_defense=" + physical_defense +
                ", special_attack=" + special_attack +
                ", special_defense=" + special_defense +
                ", skillsOfPokes=" + Arrays.toString(skillsOfPokes) +
                '}';
    }

    //设置宝可梦技能
    public void setPokeSkill(String skillName,int position) throws IOException{
        PokemonSkill skill = new PokemonSkill();
        skill.getPokeSkillInfoFromDb(skillName);
        this.skillsOfPokes[position] = skill;
    }

    //获得技能名称
    public String getSkillName(int flag){
        return skillsOfPokes[flag].getSkillName();
    }

    //获得技能类别
    public int getSkillType(int flag){
        return skillsOfPokes[flag].getSkillType();
    }

    //获得技能属性
    public int getSkillAttribute(int flag){
        return skillsOfPokes[flag].getSkillAttribute();
    }

    //获得技能剩余次数
    public int getSkillTimes(int flag){
        return skillsOfPokes[flag].getSkillTimes();
    }

    //使用技能
    public int useSkill(int flag,int phy_defence,int sp_defence){
        //判断是否为空技能
        if(skillsOfPokes[flag]==null){
            return -1;
        }
        int status = triggerSkill(skillsOfPokes[flag],phy_defence,sp_defence);
        return status;
    }

    //触发技能
    public int triggerSkill(PokemonSkill skill,int phy_defence,int sp_defence){
        //判断使用次数是否足够
        if(skill.getSkillTimes()==0){
            return -2;
        }

        //技能开始使用
        skill.skillTimesMinus();//减少次数

        //技能是否命中
        if(!skill.isHit()){
            return -3;
        }

        //技能类型1为造成物理伤害 2为造成属性伤害 3为治愈 4为特殊状态
        switch(skill.getSkillType()){
            case 1:{
                return getPhysicalDamage(skill.getSkillPower(),phy_defence);
            }
            case 2:{
                return getSpecialDamage(skill.getSkillPower(),sp_defence);
            }
            case 3:{
                return getRecoverNumber(skill.getSkillPower());
            }
            case 4:{
                return -3;
            }
            default:{
                return -4;
            }
        }
    }

    //获取物理伤害
    public int getPhysicalDamage(int power,int phy_defence){
        //基础伤害
        double basicDamage = (this.physical_attack-phy_defence)*power*0.01;

        //设置保底伤害
        if(basicDamage<phy_defence*0.05){
            basicDamage = phy_defence*0.05;
        }

        //转为int
        return (int)basicDamage;
    }

    //获取特攻伤害
    public int getSpecialDamage(int power,int sp_defence){
        //基础伤害
        double basicDamage = (this.special_attack-sp_defence)*power*0.01;

        //设置保底伤害
        if(basicDamage<sp_defence*0.05){
            basicDamage = sp_defence*0.05;
        }

        //转为int
        return (int)basicDamage;
    }

    //获取恢复生命量
    public int getRecoverNumber(int power){
        return power;
    }
}
