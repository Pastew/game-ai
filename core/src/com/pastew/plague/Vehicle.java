package com.pastew.plague;


import com.badlogic.gdx.math.Vector2;

public class Vehicle extends MovingEntity {

    //a pointer to the world data enabling a vehicle to access any obstacle
    //path, wall, or agent data
    private GameWorld gameWorld;

    SteeringBehaviors steeringBehaviors;

    public void Update(float timeElapsed) {
        super.Update();
        //calculate the combined force from each steering behavior in the
        //vehicle’s list
        Vector2 steeringForce = steeringBehaviors.calculate();

        //Acceleration = Force/Mass
        Vector2 acceleration = steeringForce / mass;
        acceleration.scl(timeElapsed);
        velocity.add(acceleration);
        velocity.clamp(0, maxSpeed);

        //update the heading if the vehicle has a velocity greater than a very small value
        if (velocity.len() > 0.00000001) {
            Vector2 v = new Vector2(velocity);
            heading = v.nor();
            side.x = heading.y;
            side.y = -heading.x;
        }
    }
}
