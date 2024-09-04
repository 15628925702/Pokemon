package org.example.pokemon.BattleNet;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import org.example.pokemon.battle.PokemonData;

import java.io.*;
import java.util.Scanner;

//战斗系统
public class ServerBattle {

    //进行操作 参数前为操作的对象 后为不操作的对象
    public int act(PokemonData actor, int action, PokemonData viewer) throws IOException, InterruptedException {
        //对命令进行分类

        switch (action){
            //发动技能1 2 3 4
            case 0,1,2,3:{
                useSkill(actor,viewer,action);
            }
            //使用道具
            case 4:{
                return -1;
            }
            //逃跑
            case 5:{
                return -2;
            }
            default:{
                return -3;
            }
        }
    }

    //发动技能
    public int useSkill(PokemonData actor, PokemonData viewer,int flag) throws IOException {
        //获得伤害(治疗量)
        int effect = actor.useSkill(flag,viewer.getPhysical_defense(),viewer.getSpecial_defense());
        //处理技能特殊情况
        if(effect==-1){
            System.out.println("技能为空,发动失败");
            return -1;
        }else if (effect==-2){
            System.out.println("使用次数不足");
            return -2;
        }

        System.out.println(actor.getPokemonName()+"使用了技能"+actor.getSkillName(flag));
        if(effect==-3){
            System.out.println("没有命中");
            return -3;
        }
        //获得技能类型
        int type = actor.getSkillType(flag);

        //计算属性克制

        //分类
        switch (type){
            //造成伤害
            case 1,2:{
                return damageAction(actor,viewer,effect,type);
            }
            //恢复血量
            case 3:{
                return recoverAction(actor,viewer,effect,type);
            }
            //特殊状态
            case 4:{
                return -4;
            }
            //出现错误
            default:{
                return -5;
            }
        }
    }

    //计算伤害并返回给服务器端
    public int damageAction(PokemonData actor,PokemonData viewer,int effect,int type){
        int damage = effect;
        if(effect>viewer.getHp()){
            damage = viewer.getHp();
        }

        beDamaged(actor,viewer,damage);
        return damage;
    }

    //计算回复量并返回
    public int recoverAction(PokemonData actor,PokemonData viewer,int effect,int type) throws IOException {
        int recover = effect;   //获得回复量
        //获得生命上限
        PokemonData temp_actor = new PokemonData();
        temp_actor.clonePokeData(actor);
        temp_actor.getPokeDataFromDb(temp_actor.getPokemonName());
        int hpLimit = temp_actor.getHp();

        //考虑最大生命值情况
        if(recover+actor.getHp()>hpLimit){
            recover = hpLimit-actor.getHp();
        }

        return recover;
    }

    //受到伤害
    public void beDamaged(PokemonData actor,PokemonData viewer,int effect){
        viewer.setHp(viewer.getHp()-effect);    //设置血量
        System.out.println(actor.getPokemonName()+" 对 "+viewer.getPokemonName()+"造成了"+effect+"点伤害,效果拔群");
    }

    //恢复血量
    public void beRecovered(PokemonData actor,PokemonData viewer,int effect){
        viewer.setHp(viewer.getHp()+effect);
        System.out.println(actor.getPokemonName()+"恢复了"+effect+"点生命值");
    }

    //检测游戏是否结束
    public int checkIsOver(PokemonData poke1,PokemonData poke2){
        if(poke1.getHp()==0){
            System.out.println(poke1.getPokemonName()+"晕倒了,"+poke2.getPokemonName()+"胜利");
            return -1;
        }
        else if(poke2.getHp()==0){
            System.out.println(poke2.getPokemonName()+"晕倒了,"+poke1.getPokemonName()+"胜利");
            return 1;
        }
        return 0;
    }


}