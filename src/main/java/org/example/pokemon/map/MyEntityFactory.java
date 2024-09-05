package org.example.pokemon.map;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import org.example.pokemon.map.Components.EnemyComponent;
import org.example.pokemon.map.Components.PlayerComponent;


public class MyEntityFactory implements EntityFactory {

    @Spawns("player")
    public Entity createPlayer(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(GameType.PLAYER)
                .collidable()
                //不支持使用物理的实体
                .with(new KeepOnScreenComponent())
                .with(new PlayerComponent())
                .bbox(BoundingShape.box(32,50))
                .zIndex(1)
                .build();
    }

    @Spawns("enemy")
    public Entity createEnemy(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(GameType.ENEMY)
                .collidable()
                .with(new KeepOnScreenComponent())
                .bbox(BoundingShape.box(32,50))
                .with(new EnemyComponent())
                .build();
    }

    @Spawns("block")
    public Entity createTree1(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(GameType.BLOCK)
                .collidable()
//                .bbox(BoundingShape.box(32,32))
                .bbox(BoundingShape.box(32,32))
                //不刷新，节省性能
                .neverUpdated()
                .build();
    }

    @Spawns("sea")
    public Entity createSea(SpawnData data) {
        AnimationChannel ac = new AnimationChannel(FXGL.image("sea_anim2.png"), Duration.seconds(1),2);
        AnimatedTexture at = new AnimatedTexture(ac);
        return FXGL.entityBuilder(data)
                .type(GameType.SEA)
                .viewWithBBox(at.loop())
                .build();
    }


}
