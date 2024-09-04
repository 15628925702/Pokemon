package org.example.pokemon.battle;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.io.*;
import java.util.Scanner;

//战斗系统
public class Battle {
    //属性
    private int round;  //轮数
    private int command;    //指令
    private Object lock = new Object();    //线程锁

    //构造方法
    public Battle() {
        this.round = 1;
    }

    //getter
    public int getRound() {
        return round;
    }

    //初始化对战
    public void initBattle(PokemonData poke1, PokemonData poke2, BattleApplication ui) throws IOException {
        //建立两个线程
        //接收线程
        Thread accept = new Thread() {
            public void run() {
                try {
                    getInfoFromSever();
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                    throw new RuntimeException(e);
                }
            }
        };
        accept.start();

        //操作线程
        Thread action = new Thread(() -> {
            try {
                battleStart(poke1, poke2, ui);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace(); // 适当地处理异常
            }
        });
        action.start();
    }

    //开始游戏
    public void battleStart(PokemonData oriPoke1, PokemonData oriPoke2, BattleApplication ui) throws IOException, InterruptedException {
        //创建对战对象
        PokemonData poke1 = new PokemonData();
        PokemonData poke2 = new PokemonData();
        poke1.clonePokeData(oriPoke1);
        poke2.clonePokeData(oriPoke2);

        boolean isOn = true;    //对战是否进行中
        System.out.println("游戏开始");
        int status = 0;
        int result = 0;
        while (isOn){
            System.out.println("轮数"+round);
            if(compareSpeed(poke1, poke2)){
                status = act(poke1, poke2, ui); //操作

                //判断游戏是否结束
                if(status == -2){
                    result = -1;
                    System.out.println(poke1.getPokemonName()+"逃跑了");
                    break;
                }
                result = checkIsOver(poke1,poke2);
                if(result!=0){
                    break;
                }

                status = act(poke2, poke1, ui); //操作

                //判断游戏是否结束
                if(status == -2){
                    result = -1;
                    System.out.println(poke2.getPokemonName()+"逃跑了");
                    break;
                }
                result = checkIsOver(poke1,poke2);
                if(result!=0){
                    break;
                }
            }
            else{
                status = act(poke2, poke1, ui); //操作

                //判断游戏是否结束
                if(status == -2){
                    result = -1;
                    System.out.println(poke2.getPokemonName()+"逃跑了");
                    break;
                }
                result = checkIsOver(poke1,poke2);
                if(result!=0){
                    break;
                }

                status = act(poke1, poke2, ui); //操作

                //判断游戏是否结束
                if(status == -2){
                    result = -1;
                    System.out.println(poke1.getPokemonName()+"逃跑了");
                    break;
                }
                result = checkIsOver(poke1,poke2);
                if(result!=0){
                    break;
                }
            }
            round++;
        }
    }

    //比较速度 poke1>poke2返回true poke1<poke2返回false poke1=poke2随机返回
    public boolean compareSpeed(PokemonData poke1, PokemonData poke2){
        if(poke1.getSpeed()>poke2.getSpeed()){
            return true;
        }
        else if (poke1.getSpeed()<poke2.getSpeed()) {
            return false;
        }
        else{
            double result = Math.random();
            return result < 0.5;
        }
    }

    //进行操作 参数前为操作的对象 后为不操作的对象
    public int act(PokemonData actor, PokemonData viewer, BattleApplication ui) throws IOException, InterruptedException {
        showPokeStatus(actor,viewer,ui);   //显示宝可梦状态

        //获得执行命令
        synchronized(lock){
            lock.wait();
        }
        int action = command;

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

        //分类 向服务器传输请求
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

    //计算回复量并返回给服务器端
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

    //显示宝可梦信息
    public void showPokeStatus(PokemonData actor,PokemonData viewer,BattleApplication ui) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));
        //fxmlLoader.load();
        //BattleController controller = fxmlLoader.<BattleController>getController();
        Platform.runLater(()->{
            ui.setStatusLabelText(actor.getPokemonName()+"的hp为"+actor.getHp()+'\n'
            +viewer.getPokemonName()+"的hp为"+viewer.getHp()+'\n'
            +"目前是"+actor.getPokemonName()+"的回合"+'\n'
            +"请执行操作 0~3使用技能 4使用道具 5逃跑");
            showSkills(actor,ui);
        });

        /*
        ui.setStatusLabel(actor.getPokemonName()+"的hp为"+actor.getHp());
        System.out.println(actor.getPokemonName()+"的hp为"+actor.getHp());
        System.out.println(viewer.getPokemonName()+"的hp为"+viewer.getHp());
        System.out.println("目前是"+actor.getPokemonName()+"的回合");
        System.out.println("请执行操作 0~3使用技能 4使用道具 5逃跑");
         */

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

    //显示技能信息
    public void showSkills(PokemonData actor,BattleApplication ui){
        PokemonSkill[] skills = actor.getSkillsOfPokes();
        for(int i=0;i<4;i++) {
            if (skills[i] == null) {
                break;
            }
            ui.addStatusLabelText(i + ":" + skills[i].getSkillName() + " 剩余次数:" + skills[i].getSkillTimes());
            //System.out.println(i + ":" + skills[i].getSkillName() + " 剩余次数:" + skills[i].getSkillTimes());
        }
    }

    //响应
    public boolean isFinish(){
        return true;
    }

    //接受信息
    public void getInfoFromSever() throws FileNotFoundException {
        boolean getInfo = false;
        int result = -1;
        // 使用类路径访问文件
        File file = new File("src/main/resources/BattleInfo");

        System.out.println("接受信息线程调用成功");

        //检测文件是否操作
        while (true){
            Scanner input = new Scanner(file);
            while (input.hasNext()){
                int i = input.nextInt();
                if (i == -1){
                    continue;
                }else{
                    result = i;
                    handleResult(i);
                }
            }
        }
    }

    //处理获得的信息
    public void handleResult(int i) throws FileNotFoundException {
        System.out.println("handle success");
        command = i;

        //标记此条消息已经处理
        markFinish();
    }

    //标记任务已完成
    public void markFinish() throws FileNotFoundException {
        File file = new File("src/main/resources/BattleInfo");
        PrintWriter output = new PrintWriter(file);

        synchronized(lock){
            lock.notifyAll();
        }
        //output.print(" ");
        //output.print(-1);
    }
}