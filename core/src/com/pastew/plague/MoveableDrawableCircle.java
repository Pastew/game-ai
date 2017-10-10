package com.pastew.plague;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.awt.Shape;

public class MoveableDrawableCircle extends DrawableCircle {

    protected float speedMultiplier;
    protected Vector2 velocity;


    public MoveableDrawableCircle(Vector2 position, float size, Color color, float speedMultiplier) {
        super(position, size, color);
        velocity = new Vector2(0,0);
        this.speedMultiplier = speedMultiplier;
    }

    public void update(){
        this.position.add(velocity);
    }

    public void setXSpeed(int xSpeed) {
        this.velocity.x = xSpeed * speedMultiplier;
    }

    public void setYSpeed(int ySpeed) {
        this.velocity.y = ySpeed * speedMultiplier;
    }


}
