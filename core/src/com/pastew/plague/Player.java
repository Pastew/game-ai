package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Player extends MoveableDrawableCircle {
    private Vector2 target;

    public Player(float x, float y, float size, float speedMultiplier) {
        super(new Vector2(x,y), size, speedMultiplier);
        target = new Vector2(x,y);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        super.draw(shapeRenderer);

        // Draw beam
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
