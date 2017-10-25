package com.pastew.plague;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MovingEntity extends BaseGameEntity{

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
        heading = new Vector2D(1,0);
        side = heading.perp();
        mass = 1;
        maxSpeed = 100;
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
        shapeRenderer.line((float)start.x, (float)start.y, (float)end.x, (float)end.y);
        shapeRenderer.end();
    }
}
