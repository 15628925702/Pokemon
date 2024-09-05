package org.example.pokemon.map.Components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;
import org.example.pokemon.map.Dir;

public class EnemyCoponent extends Component {
    private PlayerComponent playerComponent;

    @Override
    public void onUpdate(double tpf) {
        Dir moveDir = playerComponent.getDir();
        if(FXGLMath.randomBoolean(0.025)){
            moveDir = FXGLMath.random(Dir.values()).get();
        }
        switch (moveDir){
            case UP -> playerComponent.moveUp();
            case DOWN -> playerComponent.moveDown();
            case LEFT -> playerComponent.moveLeft();
            case RIGHT -> playerComponent.moveRight();
            default -> {

            }
        }
    }
}
