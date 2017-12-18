package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Agent extends MovingEntity {

    private GameWorld gameWorld;

    private SteeringBehaviors steeringBehaviors;

//AGENT JEST BOTEM

    Agent(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        maxSpeed = Parameters.BOT_MAX_SPEED;
        maxForce = Parameters.BOT_MAX_FORCE;

        color = GameColors.enemyColor;


        steeringBehaviors = new SteeringBehaviors(this, gameWorld);


        //steeringBehaviors.turnOnFlee(gameWorld.getPlayerVector2D());
        //steeringBehaviors.turnOnSeek(gameWorld.getPlayerVector2D());
        //steeringBehaviors.turnOnSeek(gameWorld.getColumn(0).position);
        steeringBehaviors.turnOnArrive(gameWorld.getPlayerVector2D());
        steeringBehaviors.turnOnWander();
        steeringBehaviors.turnOnObstacleAvoidance();
        steeringBehaviors.turnOnWallAvoidance();
        steeringBehaviors.turnOnHide(gameWorld.getPlayerBaseEntity());
        steeringBehaviors.turnOnSeparation();
    }

    //colision line

    public void render() {
        super.render();

        if (Parameters.DRAW_DEBUG) {
            // === Obstacle avoidance ===
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.CYAN);
            MutableDouble boxLength = steeringBehaviors.boxLength;
            Vector2D linePosition = Vector2D.add(position, Vector2D.mul(heading,
                    boxLength.getValue() + (velocity.Length() / maxSpeed.getValue()) * boxLength.getValue() * 2));
            shapeRenderer.line((float) position.x, (float) position.y, (float) linePosition.x, (float) linePosition.y);

            shapeRenderer.end();

            // === Wander ===
            // Wander circle
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Vector2D wanderCircleCenter = heading.getCopy();
            wanderCircleCenter.mul(steeringBehaviors.wanderDistance.getValue());
            wanderCircleCenter.add(position);
            shapeRenderer.circle((int) wanderCircleCenter.x, (int) wanderCircleCenter.y, (int) steeringBehaviors.wanderRadius.getValue());
            shapeRenderer.end();

            // Wander target spot
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(
                    (int) steeringBehaviors.wanderTargetWorld.x,
                    (int) steeringBehaviors.wanderTargetWorld.y,
                    5);
            shapeRenderer.end();

            // Hide spots
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(
                    (int) steeringBehaviors.wanderTargetWorld.x,
                    (int) steeringBehaviors.wanderTargetWorld.y,
                    3);
            shapeRenderer.end();

        }
    }


    public void update(double deltaTime) {
        //calculate the combined force from each steering behavior in the
        //agent’s list
        tagNeighbors(this, gameWorld.getEnemies(), Parameters.SEPARATION_RADIUS.getValue());
        Vector2D steeringForce = steeringBehaviors.calculate();

        //Acceleration = Force/Mass
        Vector2D acceleration = steeringForce.div(mass);
        acceleration.mul(deltaTime);
        velocity.add(acceleration);
        velocity.Truncate(maxSpeed.getValue());
        position.add(velocity);

        super.update(deltaTime);


    }
}
