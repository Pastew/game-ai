package com.pastew.plague;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class GameWorld {

    private final static int ENEMIES_NUMBER = 10;

    private SpriteBatch batch;
    private Player player;
    private List<BaseGameEntity> entities;

    GameWorld() {
        entities = new ArrayList<BaseGameEntity>();
        player = new Player(400, 400);
        entities.add(player);
        generateEnemies();

        batch = new SpriteBatch();
    }

    private void generateEnemies() {
        Random random = new Random();

        for (int i = 0; i < ENEMIES_NUMBER; ++i) {
            float x = 10;
            float y = 10;
            if (random.nextBoolean())
                x = Gdx.graphics.getWidth() - i * 50;

            if (random.nextBoolean())
                y = Gdx.graphics.getHeight() - i * 50;

            Agent enemy = new Agent(this);
            enemy.position.x = x;
            enemy.position.y = y;

            entities.add(enemy);
        }
    }


    void update() {
        handleKeyboardInput();

        // Update each entity
        for (BaseGameEntity entity : entities)
            entity.update(Gdx.graphics.getDeltaTime());
    }

    void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (BaseGameEntity entity : entities)
            entity.render();
        batch.end();
    }

    private void handleKeyboardInput() {
        // Moving left and right
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            player.setXForceDirection(-1);
        else if (Gdx.input.isKeyPressed(Input.Keys.D))
            player.setXForceDirection(1);
        else
            player.setXForceDirection(0);

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            player.setYForceDirection(1);
        else if (Gdx.input.isKeyPressed(Input.Keys.S))
            player.setYForceDirection(-1);
        else
            player.setYForceDirection(0);

        // Crosshair
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        player.setCrosshairPosition(mouseX, mouseY);
    }

    void dispose() {
        batch.dispose();
    }

    Vector2D getPlayerVector2D() {
        return player.position;
    }
}
