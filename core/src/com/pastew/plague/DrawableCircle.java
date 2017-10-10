package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class DrawableCircle {
    protected Vector2 position;
    protected float size;

    public DrawableCircle(Vector2 position, float size) {
        this.position = position;
        this.size = size;
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(position.x, position.y, size);
        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.end();
    }
}
