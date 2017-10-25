package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BaseGameEntity {

    protected Vector2D position;
    protected Color color;
    protected ShapeRenderer shapeRenderer;


    public BaseGameEntity() {
        position = new Vector2D(0, 0);
        shapeRenderer = new ShapeRenderer();
        color = Color.WHITE;
    }

    public void update(double deltaTime) {
    }

    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.circle((float) position.x, (float) position.y, 10);
        shapeRenderer.end();
    }
}