package org.example.pokemon.battle;

import javafx.application.Platform;
import javafx.scene.image.Image;
import org.example.pokemon.BattleNet.PokemonBattleClient;

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
    public void initBattle(PokemonData poke1, PokemonData poke2, BattleScene ui, PokemonBattleClient client) throws IOException {
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
                battleStart(poke1, poke2, ui,client);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace(); // 适当地处理异常
            }
        });
        action.start();
    }

    //开始游戏
    public void battleStart(PokemonData oriPoke1, PokemonData oriPoke2, BattleScene ui, PokemonBattleClient client) throws IOException, InterruptedException {
        //创建对战对象
        PokemonData poke1 = new PokemonData();
        PokemonData poke2 = new PokemonData();
        poke1.clonePokeData(oriPoke1);
        poke2.clonePokeData(oriPoke2);
        //showPet(poke1,poke2,ui);
        showPokeName(poke1,poke2,ui,client);    //显示宝可梦名字
        showHp(poke1,poke2,ui,client);   //显示宝可梦状态

        boolean isOn = true;    //对战是否进行中
        showStatus("游戏开始",ui);
        int status = 0;
        int result = 0;
        while (isOn){
            showStatus("轮数"+round,ui);

            if(compareSpeed(poke1, poke2)){
                status = act(poke1, poke2, ui, client); //操作

                //判断游戏是否结束
                if(status == -2){
                    result = -1;
                    showNotice(poke1.getPokemonName()+"逃跑了",ui);
                    break;
                }
                result = checkIsOver(poke1,poke2,ui);
                if(result!=0){
                    break;
                }
                status = act(poke2, poke1, ui, client); //操作

                //判断游戏是否结束
                if(status == -2){
                    result = -1;
                    showNotice(poke2.getPokemonName()+"逃跑了",ui);
                    break;
                }
                result = checkIsOver(poke1,poke2,ui);
                if(result!=0){
                    break;
                }
            }
            else{
                status = act(poke2, poke1, ui, client); //操作

                //判断游戏是否结束
                if(status == -2){
                    result = -1;
                    showNotice(poke2.getPokemonName()+"逃跑了",ui);
                    break;
                }
                result = checkIsOver(poke1,poke2,ui);
                if(result!=0){
                    break;
                }

                status = act(poke1, poke2, ui, client); //操作

                //判断游戏是否结束
                if(status == -2){
                    result = -1;
                    showNotice(poke1.getPokemonName()+"逃跑了",ui);
                    break;
                }
                result = checkIsOver(poke1,poke2,ui);
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
    public int act(PokemonData actor, PokemonData viewer, BattleScene ui, PokemonBattleClient client) throws IOException, InterruptedException {
        showPokeStatus(actor,viewer,ui,client);   //显示宝可梦状态
        showPet(actor,viewer,ui);

        //获得执行命令
        synchronized(lock){
            lock.wait();
        }
        int action = command;

        //判断是否命中


        //对命令进行分类
        switch (action){
            //发动技能1 2 3 4
            case 0,1,2,3:{
                useSkill(actor,viewer,action,ui,client);
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
    public int useSkill(PokemonData actor, PokemonData viewer, int flag, BattleScene ui,PokemonBattleClient client) throws IOException {
        int effect = 0;

        while(true){
            //获得伤害(治疗量)
            effect = actor.useSkill(flag,viewer.getPhysical_defense(),viewer.getSpecial_defense());
            if(effect != -3){
                break;
            }
            PokemonSkill temp = actor.getSkill(flag);
            temp.setSkillTimes(temp.getSkillTimes()+1);
        }



        //处理技能特殊情况
        if(effect==-1){
            showReturn("技能为空,发动失败",ui);
            return -1;
        }else if (effect==-2){
            showReturn("使用次数不足",ui);
            return -2;
        }

        showStatus(actor.getPokemonName()+"使用了技能"+actor.getSkillName(flag),ui);
        if(client.ifMiss==true){
            showReturn("技能未命中",ui);
            client.ifMiss=false;
            return -3;
        }
        //获得技能类型
        int type = actor.getSkillType(flag);

        //计算属性克制

        //分类 向服务器传输请求
        switch (type){
            //造成伤害
            case 1,2:{
                return damageAction(actor,viewer,effect,type,ui);
            }
            //恢复血量
            case 3:{
                return recoverAction(actor,viewer,effect,type,ui);
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
    public int damageAction(PokemonData actor, PokemonData viewer, int effect, int type, BattleScene ui){
        int damage = effect;
        if(effect>viewer.getHp()){
            damage = viewer.getHp();
        }

        beDamaged(actor,viewer,damage,ui);
        return damage;
    }

    //计算回复量并返回给服务器端
    public int recoverAction(PokemonData actor,PokemonData viewer,int effect,int type, BattleScene ui) throws IOException {
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

        beRecovered(actor,viewer,recover,ui);
        return recover;
    }



    //受到伤害
    public void beDamaged(PokemonData actor, PokemonData viewer, int effect, BattleScene ui) {
        viewer.setHp(viewer.getHp()-effect);    //设置血量
        String showContent = actor.getPokemonName()+" 对 "+viewer.getPokemonName()+"造成了"+effect+"点伤害,效果拔群";
        ui.showDamageAnimation(1);
        showReturn(showContent,ui); //反馈给前端
    }

    //恢复血量
    public void beRecovered(PokemonData actor,PokemonData viewer,int effect, BattleScene ui){
        viewer.setHp(viewer.getHp()+effect);
        String showContent = actor.getPokemonName()+"恢复了"+effect+"点生命值";
        showReturn(showContent,ui);
    }

    //显示宝可梦信息
    public void showPokeStatus(PokemonData actor, PokemonData viewer, BattleScene ui, PokemonBattleClient client) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));
        //fxmlLoader.load();
        //BattleController controller = fxmlLoader.<BattleController>getController();
        Platform.runLater(()->{
            try {
                showHp(actor,viewer,ui,client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            /*
            ui.setStatusLabelText(actor.getPokemonName()+"的hp为"+actor.getHp()+'\n'
                    +viewer.getPokemonName()+"的hp为"+viewer.getHp()+'\n'
                    +"目前是"+actor.getPokemonName()+"的回合"+'\n'
                    +"请执行操作 0~3使用技能 4使用道具 5逃跑");
             */

            try {
                showSkills(actor,ui);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
    public int checkIsOver(PokemonData poke1,PokemonData poke2, BattleScene ui){
        if(poke1.getHp()==0){
            showNotice(poke1.getPokemonName()+"晕倒了,"+poke2.getPokemonName()+"胜利",ui);
            return -1;
        }
        else if(poke2.getHp()==0){
            showNotice(poke2.getPokemonName()+"晕倒了,"+poke1.getPokemonName()+"胜利",ui);
            return 1;
        }
        return 0;
    }

    //显示技能信息
    public void showSkills(PokemonData actor, BattleScene ui) throws IOException {
        PokemonSkill[] skills = actor.getSkillsOfPokes();//获得技能
        String[] skillNames = new String[skills.length];//技能名
        String[] skillTimes = new String[skills.length];//技能次数
        for(int i=0;i<4;i++) {
            if (skills[i] == null) {
                for(int j=i;j<4;j++){
                    skillNames[j] = "-";
                    skillTimes[j] = "0/0";
                }
                break;
            }

            skillNames[i] = skills[i].getSkillName();//获得技能名
            int temp_time = actor.getSkillTimes(i);//获得技能现在的次数
            //获取原技能信息
            PokemonSkill temp_skill = new PokemonSkill();
            temp_skill.getPokeSkillInfoFromDb(skills[i].getSkillName());
            int lim_time = temp_skill.getSkillTimes();//获得次数上限

            skillTimes[i] = temp_time + "/" + lim_time;//修改字符串

            //ui.addStatusLabelText(i + ":" + skills[i].getSkillName() + " 剩余次数:" + skills[i].getSkillTimes());
            //System.out.println(i + ":" + skills[i].getSkillName() + " 剩余次数:" + skills[i].getSkillTimes());
        }
        ui.getSkillInfo(skillNames[0],skillNames[1],skillNames[2],skillNames[3],
                skillTimes[0],skillTimes[1],skillTimes[2],skillTimes[3]);//设置技能信息
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

    //设置血量显示
    //设置血量显示
    public void showHp(PokemonData actor, PokemonData viewer, BattleScene ui, PokemonBattleClient client) throws IOException {
        Platform.runLater(()->{
            int actor_lim_hp,viewer_lim_hp;
            //获得actor的目前血量以及血量上限
            int actor_cur_hp = client.healthMe;
            PokemonData temp_actor = new PokemonData();
            try {
                temp_actor.getPokeDataFromDb(actor.getPokemonName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //int actor_lim_hp = temp_actor.getHp();

            //获得viewer的目前血量以及血量上限
            int viewer_cur_hp = client.healthEn;
            PokemonData temp_viewer = new PokemonData();
            try {
                temp_viewer.getPokeDataFromDb(viewer.getPokemonName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //System.out.println(temp_viewer.toString());
            //int viewer_lim_hp = temp_viewer.getHp();
            if(client.isMyTurn){
                actor_lim_hp = temp_actor.getHp();
                viewer_lim_hp = temp_viewer.getHp();
            }else{
                actor_lim_hp = temp_viewer.getHp();
                viewer_lim_hp = temp_actor.getHp();
            }
            System.out.println("攻击方现在血量:"+actor_cur_hp);
            System.out.println("攻击方血量上限: "+actor_lim_hp);
            System.out.println("防守方现在血量:"+viewer_cur_hp);
            System.out.println("防守方血量上限: "+viewer_lim_hp);

            ui.updateHpStatus(actor_cur_hp,viewer_cur_hp,actor_lim_hp,viewer_lim_hp);
        });

    }

    //显示技能使用
    public void setStatus(String text, BattleScene ui){
        Platform.runLater(()->{
            ui.setStatusLabelText(text);
        });
    }
    public void showStatus(String text, BattleScene ui){
        Platform.runLater(()->{
            ui.addStatusLabelText(text);
        });
    }

    //显示状态反馈
    public void showReturn(String text, BattleScene ui){
        Platform.runLater(()->{
            ui.setReturnButtonText(text);
        });
    }

    //显示宠物图像
    public void showPet(PokemonData actor, PokemonData viewer, BattleScene ui) {
        int actor_id = actor.getPokemonID();
        System.out.println(actor_id);
        int viewer_id = viewer.getPokemonID();

        String actorImagePath = "/img/battle/Poke" + actor_id + "/back.png";
        Image actorImg = new Image(getClass().getResourceAsStream(actorImagePath));
        String viewerImagePath = "/img/battle/Poke" + viewer_id + "/front.png";
        Image viewerImg = new Image(getClass().getResourceAsStream(viewerImagePath));
        ui.showPet(actorImg,viewerImg);
    }

    //显示通知
    public void showNotice(String text, BattleScene ui){
        Platform.runLater(()->{
            ui.showNotice(text);
        });
    }

    //显示宝可梦名
    public void showPokeName(PokemonData poke1,PokemonData poke2, BattleScene ui, PokemonBattleClient client){
        Platform.runLater(()->{
            String name1 = poke1.getPokemonName();
            String name2 = poke2.getPokemonName();
            if(client.ifIamA){
                ui.showPokeName(name1,name2);
            }else{
                ui.showPokeName(name2,name1);
            }
        });
    }

}