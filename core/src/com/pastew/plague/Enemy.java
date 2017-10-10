package com.pastew.plague;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends MoveableDrawableCircle{

    private MoveableDrawableCircle target;

    public Enemy(Vector2 position, float size, float speedMultiplier, MoveableDrawableCircle target) {
        super(position, size, Color.ORANGE, speedMultiplier);
        this.target = target;
    }

    public void update(){
        Vector2 targetPos = new Vector2(target.position);
        this.velocity = targetPos.sub(this.position).nor().scl(speedMultiplier);
        super.update();
    }
}
