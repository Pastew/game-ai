package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player extends MovingEntity {

    private Vector2D crosshair; // celownik
    private Vector2D forceDirection;
    private float moveForce;
    private float frictionCoeffictient;

    Player(float x, float y) {
        super();
        position = new Vector2D(x,y);
        crosshair = new Vector2D(x,y);
        maxSpeed = 5;
        moveForce = 100;
        frictionCoeffictient = 0.95f;
        forceDirection = new Vector2D(0,0);
        color = Color.GOLD;
    }

    public void render() {
        super.render();

        // Draw rifle beam
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.FIREBRICK);
        Vector2D endOfLine = crosshair.sub(position);
        endOfLine.Normalize();
        endOfLine.mul(5000);
        endOfLine.add(position);
        shapeRenderer.line((float)position.x, (float)position.y, (float)endOfLine.x, (float)endOfLine.y);
        shapeRenderer.end();
    }

    void setCrosshairPosition(int mouseX, int mouseY) {
        this.crosshair.x = mouseX;
        this.crosshair.y = mouseY;
    }

    void setXForceDirection(int i) {
        forceDirection.x = i;
    }

    void setYForceDirection(int i) {
        forceDirection.y = i;
    }

    public void update(double timeElapsed){
        Vector2D force = forceDirection.mul(moveForce);
        //Acceleration = Force/Mass
        Vector2D acceleration = force.div(mass);
        velocity.add(acceleration.mul(timeElapsed));

        velocity.Truncate(maxSpeed);
        position.add(velocity);

        // Pseudo friction to make player slow down and stop when no force is applied
        velocity.mul(frictionCoeffictient);

        //update the heading if the agent has a velocity greater than a very small value
        if (velocity.LengthSq() > 0.00000001) {
            Vector2D v = new Vector2D(velocity);
            heading = Vector2D.Vec2DNormalize(v);
            side = heading.perp();
        }

    }
}
