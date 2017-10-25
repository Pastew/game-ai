package com.pastew.plague;

public class Agent extends MovingEntity {

    private GameWorld gameWorld;

    SteeringBehaviors steeringBehaviors;

    public Agent(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        steeringBehaviors = new SteeringBehaviors(this);
        steeringBehaviors.turnOnSeek(gameWorld.getPlayerVector2D());

        maxSpeed = 5;
    }

    public void update(double deltaTime) {
        super.update(deltaTime);
        //calculate the combined force from each steering behavior in the
        //agent’s list
        Vector2D steeringForce = steeringBehaviors.calculate();

        //Acceleration = Force/Mass
        Vector2D acceleration = steeringForce.div(mass);
        acceleration.mul(deltaTime);
        velocity.add(acceleration);
        velocity.Truncate(maxSpeed);
        position.add(velocity);

        //update the heading if the agent has a velocity greater than a very small value
        if (velocity.LengthSq() > 0.00000001) {
            Vector2D v = new Vector2D(velocity);
            heading = Vector2D.Vec2DNormalize(v);
            side = heading.perp();
        }
    }
}
