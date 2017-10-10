package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Player extends MoveableDrawableCircle {
    private Vector2 aim;

    public Player(float x, float y, float size, float speedMultiplier) {
        super(new Vector2(x,y), size, Color.SKY, speedMultiplier);
        aim = new Vector2(x,y);
    }

    public void draw() {
        super.draw();

        // Draw beam
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Vector2 endOfLine = aim.sub(position);
        endOfLine.nor();
        endOfLine.scl(5000);
        endOfLine.add(position);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.line(position.x, position.y, endOfLine.x, endOfLine.y);
        shapeRenderer.end();
    }

    public void setTarget(int mouseX, int mouseY) {
        this.aim.x = mouseX;
        this.aim.y = mouseY;
    }
}
