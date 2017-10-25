package com.pastew.plague;

import com.badlogic.gdx.ApplicationAdapter;

public class Plague extends ApplicationAdapter {

    private GameWorld gameWorld;

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
