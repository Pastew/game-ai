package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 position;
    private float speedMultiplier = 3;
    private float xSpeed;
    private float ySpeed;
    private float size;
    private Vector2 target;

    public Player(float x, float y, float size) {
        this.position = new Vector2(x,y);
        this.size = size;
        target = new Vector2(x,y);
    }

    public void update(){
        this.position.x += xSpeed;
        this.position.y += ySpeed;
    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed * speedMultiplier;
    }

    public void setYSpeed(int ySpeed) {
        this.ySpeed = ySpeed * speedMultiplier;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(position.x, position.y, size);
        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Vector2 endOfLine = target.sub(position);
        endOfLine.nor();
        endOfLine.x *= 5000;
        endOfLine.y *= 5000;
        endOfLine.add(position);
        shapeRenderer.line(position.x, position.y, endOfLine.x, endOfLine.y);
        shapeRenderer.setColor(Color.ROYAL);
        shapeRenderer.end();
    }

    public void setTarget(int mouseX, int mouseY) {
        this.target.x = mouseX;
        this.target.y = mouseY;
    }
}
