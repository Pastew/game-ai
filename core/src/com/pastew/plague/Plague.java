package com.pastew.plague;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Plague extends ApplicationAdapter {

    GameWorld gameWorld;

    @Override
    public void create() {
        gameWorld = new GameWorld();
    }


    @Override
    public void render() {
        gameWorld.update();
        gameWorld.render();
    }


    @Override
    public void dispose() {
        gameWorld.dispose();
    }
}
