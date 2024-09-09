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
import org.example.pokemon.map.Components.NpcComponent;
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
                .bbox(BoundingShape.box(32,44))
                .zIndex(1)
                .build();
    }

    @Spawns("enemy")
    public Entity createEnemy(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(GameType.ENEMY)
                .type(GameType.INTERACTIVE)
                .collidable()
                .with(new KeepOnScreenComponent())
                .bbox(BoundingShape.box(32,50))
                .with(new NpcComponent(GameType.ENEMY))
                .build();
    }

    @Spawns("nurse")
    public Entity createNurse(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(GameType.NURSE)
                .type(GameType.INTERACTIVE)
                .collidable()
                .with(new KeepOnScreenComponent())
                .bbox(BoundingShape.box(32,50))
                .with(new NpcComponent(GameType.NURSE))
                .build();
    }

    @Spawns("doctor")
    public Entity createDoctor(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(GameType.DOCTOR)
                .type(GameType.INTERACTIVE)
                .collidable()
                .with(new KeepOnScreenComponent())
                .bbox(BoundingShape.box(32,50))
                .with(new NpcComponent(GameType.DOCTOR))
                .build();
    }


    @Spawns("block")
    public Entity createBlock(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(GameType.BLOCK)
                .collidable()
//                .bbox(BoundingShape.box(32,32))
                .bbox(BoundingShape.box(32,32))
                //不刷新，节省性能
                .neverUpdated()
                .build();
    }

    @Spawns("sandTree")
    public Entity createSandTree(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(GameType.SANDTREE)
                .collidable()
                .neverUpdated()
                .viewWithBBox("sandTree2.png")
                .build();
    }

    @Spawns("sea")
    public Entity createSea(SpawnData data) {
        AnimationChannel ac = new AnimationChannel(FXGL.image("ocean.png"), Duration.seconds(1),4);
        AnimatedTexture at = new AnimatedTexture(ac);
        return FXGL.entityBuilder(data)
                .type(GameType.SEA)
                .viewWithBBox(at.loop())
                .build();
    }

    @Spawns("interactive")
    public Entity createInteractive(SpawnData data) {
        return FXGL.entityBuilder(data)
                .collidable()
                .type(GameType.INTERACTIVE)
                .bbox(BoundingShape.box(32,32))
                .build();
    }

    @Spawns("hint")
    public Entity createHint(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(GameType.HINT)
                .view("hint.png")
                .zIndex(2)
                .build();
    }

    @Spawns("grass")
    public Entity createGrass(SpawnData data) {
        return FXGL.entityBuilder()
                .collidable()
                .bbox(BoundingShape.box(32,32))
                .type(GameType.GRASS)
                .build();
    }

    @Spawns("flower")
    public Entity createFlower(SpawnData data) {
        AnimationChannel ac = new AnimationChannel(FXGL.image("flower.png"), Duration.seconds(1),4);
        AnimatedTexture at = new AnimatedTexture(ac);
        return FXGL.entityBuilder(data)
                .type(GameType.FLOWER)
                .viewWithBBox(at.loop())
                .build();
    }
}
