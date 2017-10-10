package com.pastew.plague;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Plague extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	Player player;

	@Override
	public void create () {
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        player = new Player(400, 400, 16, 3);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0,0,0 , 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        player.update();

		batch.begin();
        player.draw(shapeRenderer);
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
