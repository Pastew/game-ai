package com.pastew.plague;

import com.badlogic.gdx.math.Vector2;

public class MovingEntity extends BaseGameEntity{

    boolean tag;

    Vector2 velocity;
    //a normalized vector pointing in the direction the entity is heading.
    Vector2 heading;
    //a vector perpendicular to the heading vector
    Vector2 side;

    float mass;
    //the maximum speed at which this entity may travel.
    float maxSpeed;
    //the maximum force this entity can produce to power itself
    //(think rockets and thrust)
    float maxForce;
    //the maximum rate (radians per second) at which this vehicle can rotate
    float maxTurnRate;

}
