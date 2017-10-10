package com.pastew.plague;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Plague extends ApplicationAdapter {

    final int ENEMIES_NUMBER = 10;

	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	Player player;
    Enemy[] enemies;

	@Override
	public void create () {
		batch = new SpriteBatch();
        player = new Player(400, 400, 16, 3);
        generateEnemies();
	}

    private void generateEnemies() {
        Random random = new Random();

        enemies = new Enemy[ENEMIES_NUMBER];
        for(int i = 0 ; i < ENEMIES_NUMBER; ++i) {
            float x = -10;
            float y = -10;
            if (random.nextBoolean())
                x = Gdx.graphics.getWidth() + i*50;

            if (random.nextBoolean())
                y = Gdx.graphics.getHeight() + i*50;

            enemies[i] = new Enemy(new Vector2(x,y), 10, 2, player);
        }
    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        player.update();
        for (Enemy enemy : enemies)
            enemy.update();

		batch.begin();
        player.draw();
        batch.end();

        batch.begin();
        for (Enemy enemy : enemies)
            enemy.draw();
		batch.end();
	}

    private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            player.setXSpeed(-1);
        else if(Gdx.input.isKeyPressed(Input.Keys.D))
            player.setXSpeed(1);
        else
            player.setXSpeed(0);

        if(Gdx.input.isKeyPressed(Input.Keys.W))
            player.setYSpeed(1);
        else if(Gdx.input.isKeyPressed(Input.Keys.S))
            player.setYSpeed(-1);
        else
            player.setYSpeed(0);

        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        player.setTarget(mouseX, mouseY);
    }

    @Override
	public void dispose () {
		batch.dispose();
	}
}
