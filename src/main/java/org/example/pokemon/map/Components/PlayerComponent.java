package org.example.pokemon.map.Components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityGroup;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.animation.Animation;
import javafx.util.Duration;
import org.example.pokemon.map.Dir;
import org.example.pokemon.map.GameType;
import org.example.pokemon.map.GameApp;
import org.example.pokemon.map.MyEntityFactory;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;

public class PlayerComponent extends Component {

    private static final double SPEED = 300;
    private double speed;
    private AnimationChannel acUp,acDown,acLeft,acRight;
    private AnimatedTexture at;
    private Dir dir;
    private boolean isStopped;
    private boolean isActive;
    private LazyValue<EntityGroup> entityGroupLazyValue = new LazyValue<>(()->FXGL.getGameWorld().getGroup(GameType.BLOCK,GameType.ENEMY,GameType.SEA,GameType.INTERACTIVE,GameType.SANDTREE));
    private LazyValue<EntityGroup> activeEntityGroupLazyValue = new LazyValue<>(()->FXGL.getGameWorld().getGroup(GameType.INTERACTIVE));
    private LazyValue<EntityGroup> grassLazyValue = new LazyValue<>(()->FXGL.getGameWorld().getGroup(GameType.GRASS));
    private Entity hint;

    public PlayerComponent() {
        acUp = createAc(12,15);
        acDown = createAc(0,3);
        acLeft = createAc(4,7);
        acRight = createAc(8,11);
        at = new AnimatedTexture(acDown);
        dir = Dir.DOWN;

    }

    private AnimationChannel createAc(int i,int j){
        return new AnimationChannel(FXGL.image("buLunDan2.png"),4,128/4,176/4, Duration.seconds(0.75),i,j);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(at);
    }

    //某些Component专门设置几帧调用一次，普通的一帧调用一次，tpf代表一帧的时间；一般来说，60帧一秒
    @Override
    public void onUpdate(double tpf) {
        //System.out.println("isActive = " + isActive);

//        System.out.println("isActive = "+FXGL.getb("isActive"));
//        System.out.println("level = "+FXGL.geti("level"));
//        System.out.println("active = "+FXGL.geti("active"));

        if(hint == null&&isActive){
            if(entity.getX()<985){
                hint = spawn("hint",entity.getX()+32,entity.getY());
            }
            else{
                hint = FXGL.entityBuilder()
                        .type(GameType.HINT)
                        .at(entity.getX()-16,entity.getY())
                        .view("hint2.png")
                        .zIndex(2)
                        .buildAndAttach();
            }
            FXGL.set("isActive",true);
        }
        if(hint != null&&!isActive){
            hint.removeFromWorld();
            hint = null;
            isActive = false;
            FXGL.set("isActive",false);
        }

        if(dir == Dir.UP||dir == Dir.DOWN){
//                entity.translateY(speed*tpf);
//                entity.translateX(0);
            double dis = speed*tpf;
            if(Math.abs(dis)>1.2)dis=speed/Math.abs(speed)*1.2;
            move(0,dis);
        }
        else{
//                entity.translateX(speed*tpf);
//                entity.translateY(0);
            double dis = speed*tpf;
            if(Math.abs(dis)>1.2)dis=speed/Math.abs(speed)*1.2;
            move(dis,0);
        }

        if(dir == Dir.UP){
            if(at.getAnimationChannel()!=acUp||isStopped){
                at.loopAnimationChannel(acUp);
                isStopped = false;
            }
        }
        else if(dir == Dir.DOWN){
            if(at.getAnimationChannel()!=acDown||isStopped){
                at.loopAnimationChannel(acDown);
                isStopped = false;
            }
        }
        else if(dir == Dir.LEFT){
            if(at.getAnimationChannel()!=acLeft||isStopped){
                at.loopAnimationChannel(acLeft);
                isStopped = false;
            }
        }
        else if(dir == Dir.RIGHT){
            if(at.getAnimationChannel()!=acRight||isStopped){
                at.loopAnimationChannel(acRight);
                isStopped = false;
            }
        }

        speed *= 0.8;
        if(Math.abs(speed)<1){
            speed = 0;
            at.stop();
            isStopped = true;
        }

    }

    public void moveUp(){
        dir = Dir.UP;
        speed = -SPEED;
    }
    public void moveDown(){
        dir = Dir.DOWN;
        speed = SPEED;
    }
    public void moveLeft(){
        dir = Dir.LEFT;
        speed = -SPEED;
    }
    public void moveRight(){
        dir = Dir.RIGHT;
        speed = SPEED;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public void move(double x, double y){
        //FXGL.set("active",-1);
        List<Entity> blockList = entityGroupLazyValue.get().getEntitiesCopy();
        int size = blockList.size();
        List<Entity> activeList = activeEntityGroupLazyValue.get().getEntitiesCopy();
        int size2 = activeList.size();
        List<Entity> grassList = grassLazyValue.get().getEntitiesCopy();
        int size3 = grassList.size();
        boolean isCollision = false;
        for(double i = 0;i<Math.abs(x)||i<Math.abs(y);i++){
            entity.translate(x,y);
            for(int m = 0;m<size2;m++){
                if(entity.isColliding(activeList.get(m))){
                    FXGL.set("active",m);
                   // System.out.println(m);
                    isActive = true;
                    break;
                }
                else
                    isActive = false;
            }
            for(int n = 0;n<size3;n++){
                if(entity.isColliding(grassList.get(n))){
                    if(FXGLMath.randomBoolean(0.0006)){
                        FXGL.getDialogService().showMessageBox("草丛中隐藏了一只.....");
                        break;
                    }
                }
            }
            for(int j = 0;j<size;j++){
                if (entity.isColliding(blockList.get(j))) {
                    isCollision = true;
                    break;
                }
            }
            if(isCollision){
                entity.translate(-x,-y);
                break;
            }
        }
    }
}