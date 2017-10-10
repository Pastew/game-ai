package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class DrawableCircle {
    protected Vector2 position;
    protected float size;
    protected Color color;
    protected ShapeRenderer shapeRenderer;

    public DrawableCircle(Vector2 position, float size, Color color) {
        this.position = position;
        this.size = size;
        this.color = new Color(color);
        this.shapeRenderer = new ShapeRenderer();
    }

    public void draw(){
        shapeRenderer.setColor(color);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(position.x, position.y, size);
        shapeRenderer.end();
    }
}
