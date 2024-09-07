package org.example.pokemon.map.Components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityGroup;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import org.example.pokemon.map.Dir;
import org.example.pokemon.map.GameType;

import java.util.List;

public class NpcComponent extends Component {
    private static final double SPEED = 30;
    private double speed;
    private AnimationChannel acUp,acDown,acLeft,acRight;
    private AnimatedTexture at;
    private Dir dir;
    private boolean isStopped;
    private LazyValue<EntityGroup> entityGroupLazyValue = new LazyValue<>(()->FXGL.getGameWorld().getGroup(GameType.BLOCK,GameType.PLAYER,GameType.SEA,GameType.SANDTREE,GameType.NURSE));


    public NpcComponent(GameType type) {
        acUp = createAc(12,15,type);
        acDown = createAc(0,3,type);
        acLeft = createAc(4,7,type);
        acRight = createAc(8,11,type);
        at = new AnimatedTexture(acDown);
        dir = Dir.DOWN;

    }

    private AnimationChannel createAc(int i,int j,GameType type) {
        if(type ==GameType.ENEMY)
            return new AnimationChannel(FXGL.image("RacketTeam.png"),4,128/4,200/4, Duration.seconds(0.75),i,j);
        else if (type ==GameType.DOCTOR) {
            return new AnimationChannel(FXGL.image("bigWood.png"),4,128/4,200/4, Duration.seconds(0.75),i,j);
        } else
            return new AnimationChannel(FXGL.image("Nurse2.png"),4,128/4,200/4, Duration.seconds(0.75),i,j);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(at);
    }

    //某些Component专门设置几帧调用一次，普通的一帧调用一次，tpf代表一帧的时间；一般来说，60帧一秒
    @Override
    public void onUpdate(double tpf) {


        if(FXGLMath.randomBoolean(0.02)){
            dir = FXGLMath.random(Dir.values()).get();
        }
        switch (dir){
            case UP -> moveUp();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
            default -> {}
        }
        if(speed != 0){
            if(dir == Dir.UP||dir == Dir.DOWN){
//                entity.translateY(speed*tpf);
//                entity.translateX(0);
                double dis = speed*tpf;
                if(Math.abs(dis)>0.5)dis=speed/Math.abs(speed)*0.5;
                move(0,dis);
            }
            else{
//                entity.translateX(speed*tpf);
//                entity.translateY(0);
                double dis = speed*tpf;
                if(Math.abs(dis)>0.5)dis=speed/Math.abs(speed)*0.5;
                move(dis,0);
            }
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

    public void move(double x, double y){
        List<Entity> blockList = entityGroupLazyValue.get().getEntitiesCopy();
        blockList.remove(entity);
        int size = blockList.size();
        boolean isCollision = false;
        for(double i = 0;i<Math.abs(x)||i<Math.abs(y);i++){
            entity.translate(x,y);
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
