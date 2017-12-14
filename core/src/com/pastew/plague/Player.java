package com.pastew.plague;
//

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player extends MovingEntity {

    private GameWorld gameWorld;

    // Moving
    private Vector2D forceDirection;
    private double moveForce;
    private Vector2D previousPosition;

    // Shooting
    private Vector2D crosshair; // celownik
    private boolean shooting;
    private final float SHOOT_COOLDOWN = 1f;
    private float shootCooldown = SHOOT_COOLDOWN;
    private final float BEAM_DRAWING_TIME = 0.1f;
    private float remainingBeamDrawingTime = 0f;

    Player(GameWorld gameWorld, float x, float y) {
        super();
        this.gameWorld = gameWorld;
        position = new Vector2D(x, y);
        previousPosition = new Vector2D(0, 0);
        crosshair = new Vector2D(x, y);
        maxSpeed = Parameters.PLAYER_MAX_SPEED;
        moveForce = Parameters.PLAYER_MOVE_FORCE;
        forceDirection = new Vector2D(0, 0);
        color = GameColors.playerColor;
        shooting = false;
    }

    public void render() {
        super.render();

        if (shooting & shootCooldown <= 0) {
            shootCooldown = SHOOT_COOLDOWN;
            remainingBeamDrawingTime = BEAM_DRAWING_TIME;
        }
        shootCooldown -= Gdx.graphics.getDeltaTime();

        if (remainingBeamDrawingTime > 0) {
            // Draw rifle beam
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(GameColors.rifleBeamColor);
            Vector2D endOfLine = crosshair.sub(position);
            endOfLine.Normalize();
            endOfLine.mul(5000);
            endOfLine.add(position);

            double x1 = position.x;
            double y1 = position.y;
            double x2 = endOfLine.x;
            double y2 = endOfLine.y;
            Vector2D line = new Vector2D(x2 - x1, y2 - y1);
            Vector2D leftNormal = new Vector2D(-line.y, line.x);

            BaseGameEntity firstEntityShotted = null;
            for(BaseGameEntity entity : gameWorld.getColumnsAndPlayers())
            {
                if (entity instanceof Player)
                    continue;

                Vector2D c1_circle = new Vector2D(entity.position.x - x1, entity.position.y - y1);
                if(!Vector2DOperations.inFrontOf(Vector2DOperations.add(this.heading, this.position),entity.position))
                    continue;
                double c1_circle_onNormal = c1_circle.projectionOn(leftNormal);
                if (Math.abs(c1_circle_onNormal) <= entity.size/2.0) {
                    if(firstEntityShotted == null ||
                            Vector2DOperations.distance(entity.position, position) < Vector2DOperations.distance(firstEntityShotted.position, position))
                    {
                        firstEntityShotted = entity;
                    }
                }
            }

            if(firstEntityShotted != null){
                System.out.println(String.format("Collision with: %s", firstEntityShotted.getClass().getSimpleName()));
                if (firstEntityShotted instanceof Agent) {
                    gameWorld.destroy(firstEntityShotted);
                }
                    //firstEntityShotted.color = Color.CYAN;
                    line.Normalize();
                    line.mul(Vector2DOperations.distance(position, firstEntityShotted.position) - firstEntityShotted.size/2.0);
                    x2 = position.x + line.x;
                    y2 = position.y + line.y;

                //remainingBeamDrawingTime = 0;
            }

            shapeRenderer.line((float) position.x, (float) position.y, (float) x2, (float) y2);
            shapeRenderer.end();

            remainingBeamDrawingTime -= Gdx.graphics.getDeltaTime();
        }
    }

    private BaseGameEntity getFirstEntityShotted() {
        return null;
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

    public void triggerPulled() {
        shooting = true;
    }

    public void triggerReleased() {
        shooting = false;
    }
}
