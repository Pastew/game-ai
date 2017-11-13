package com.pastew.plague;

public class Agent extends MovingEntity {

    private GameWorld gameWorld;

    private SteeringBehaviors steeringBehaviors;
    
//AGENT JEST BOTEM
    
    Agent(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        maxSpeed = 5;
        color = GameColors.enemyColor;

        steeringBehaviors = new SteeringBehaviors(this, gameWorld);

        
        //steeringBehaviors.turnOnFlee(gameWorld.getPlayerVector2D());
        //steeringBehaviors.turnOnSeek(gameWorld.getPlayerVector2D());
        //steeringBehaviors.turnOnSeek(gameWorld.getColumn(0).position);
        //steeringBehaviors.turnOnArrive(gameWorld.getPlayerVector2D());
        //steeringBehaviors.turnOnWander();
        steeringBehaviors.turnOnObstacleAvoidance();
        steeringBehaviors.turnOnHide(gameWorld.getPlayerBaseEntity());
        
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
