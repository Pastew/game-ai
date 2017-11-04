package com.pastew.plague;

public class Agent extends MovingEntity {

    private GameWorld gameWorld;

    private SteeringBehaviors steeringBehaviors;

    Agent(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        steeringBehaviors = new SteeringBehaviors(this);
        steeringBehaviors.turnOnFlee(gameWorld.getPlayerVector2D());
        //steeringBehaviors.turnOnSeek(gameWorld.getColumn(0).position);

        maxSpeed = 5;
    }

    public void update(double deltaTime) {
        //calculate the combined force from each steering behavior in the
        //agent’s list
        Vector2D steeringForce = steeringBehaviors.calculate();

        //Acceleration = Force/Mass
        Vector2D acceleration = steeringForce.div(mass);
        acceleration.mul(deltaTime);
        velocity.add(acceleration);
        velocity.Truncate(maxSpeed);
        position.add(velocity);

        super.update(deltaTime);
    }
}
