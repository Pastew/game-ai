package com.pastew.plague;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MovingEntity extends BaseGameEntity {

    protected float frictionCoeffictient;
    Vector2D velocity;
    //a normalized vector pointing in the direction the entity is heading.
    Vector2D heading;
    //a vector perpendicular to the heading vector
    Vector2D side;

    float mass;
    float maxSpeed;

    MovingEntity() {
        super();
        velocity = new Vector2D();
        heading = new Vector2D(1, 0);
        side = heading.perp();
        mass = 1;
        maxSpeed = 100;
        frictionCoeffictient = 0.95f;
    }

    public void render() {
        super.render();

        // Draw heading
        drawLine(position, Vector2D.add(position, Vector2D.mul(heading, 10)), Color.BLACK);

        // Draw side
        //drawLine(position, Vector2D.add(position, Vector2D.mul(side, 10)), Color.BLUE);
    }

    private void drawLine(Vector2D start, Vector2D end, Color color) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        shapeRenderer.line((float) start.x, (float) start.y, (float) end.x, (float) end.y);
        shapeRenderer.end();
    }

    public void update(double deltaTime){
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
