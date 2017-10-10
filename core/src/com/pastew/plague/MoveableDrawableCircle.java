package com.pastew.plague;


import com.badlogic.gdx.math.Vector2;

public class MoveableDrawableCircle extends DrawableCircle {

    protected float speedMultiplier;
    protected float xSpeed;
    protected float ySpeed;

    public MoveableDrawableCircle(Vector2 position, float size, float speedMultiplier) {
        super(position, size);
        this.speedMultiplier = speedMultiplier;
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


}
