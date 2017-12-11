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
        //steeringBehaviors.turnOnArrive(gameWorld.getPlayerVector2D());
        steeringBehaviors.turnOnWander();
        steeringBehaviors.turnOnObstacleAvoidance();
        steeringBehaviors.turnOnWallAvoidance();
        steeringBehaviors.turnOnHide(gameWorld.getPlayerBaseEntity());
    }
    
        //colision line 

    public void render() {
        super.render();

        if(Parameters.DRAW_BOT_BOUNDING_BOX) {
            //show line for collision avoidance
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.CYAN);
            double minBoxSize = steeringBehaviors.boxLength;
            Vector2D linePosition = Vector2D.add(position, Vector2D.mul(heading, minBoxSize + (velocity.Length() / maxSpeed) * minBoxSize * 2));
            shapeRenderer.line((float) position.x, (float) position.y, (float) linePosition.x, (float) linePosition.y);

            shapeRenderer.end();
        }
    }
    
    public GameWorld getWorld(){
        return gameWorld;
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
