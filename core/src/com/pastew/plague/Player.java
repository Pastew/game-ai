package com.pastew.plague;
//

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.pastew.plague.SteeringBehaviors.getHidingPosition;

public class Player extends MovingEntity {

    private Vector2D crosshair; // celownik
    private Vector2D forceDirection;
    private float moveForce;
    private Vector2D previousPosition;
    private GameWorld gameWorld;

    Player(GameWorld gameWorld, float x, float y) {
        super();
        this.gameWorld = gameWorld;
        position = new Vector2D(x, y);
        previousPosition = new Vector2D(0, 0);
        crosshair = new Vector2D(x, y);
        maxSpeed = 5;
        moveForce = 100;
        forceDirection = new Vector2D(0, 0);
        color = GameColors.playerColor;
    }

    public void render() {
        super.render();

        // Draw rifle beam
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(GameColors.rifleBeamColor);
        Vector2D endOfLine = crosshair.sub(position);
        endOfLine.Normalize();
        endOfLine.mul(5000);
        endOfLine.add(position);
        shapeRenderer.line((float) position.x, (float) position.y, (float) endOfLine.x, (float) endOfLine.y);
        shapeRenderer.end();

        // Draw hiding spots
        for (BaseGameEntity obstacle :gameWorld.getColumns()) {
            Vector2D hidingSpot = getHidingPosition(obstacle.position, obstacle.size, this.position);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.circle((float) hidingSpot.x, (float) hidingSpot.y, size / 2);
            shapeRenderer.end();
        }
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

    public void setPreviousPositionAndZeroVelocity() {
        this.position.x = previousPosition.x;
        this.position.y = previousPosition.y;

        velocity.x = 0;
        velocity.y = 0;
    }

    public void update(double deltaTime) {
        Vector2D force = forceDirection.mul(moveForce);
        //Acceleration = Force/Mass
        Vector2D acceleration = force.div(mass);
        velocity.add(acceleration.mul(deltaTime));

        velocity.Truncate(maxSpeed);
        //  position.add(velocity);

        //remembering previous position of player
        previousPosition.x = this.position.x;
        previousPosition.y = this.position.y;

        //player cannot move off the map
        double newXPosition = position.x + velocity.x;

        if (newXPosition < (Gdx.graphics.getWidth() - size / 2) && newXPosition > 0f + size / 2) {
            this.position.x = newXPosition;
        }

        double newYPosition = position.y + velocity.y;
        if (newYPosition < (Gdx.graphics.getHeight() - size / 2) && newYPosition > 0f + size / 2) {
            this.position.y = newYPosition;
        }

        super.update(deltaTime);
    }
}
