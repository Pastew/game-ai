package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

import javax.swing.text.html.parser.Entity;

public class BaseGameEntity {

    Vector2D position;
    Color color;
    ShapeRenderer shapeRenderer;
    protected float size = 20;

    boolean tag = false;

    BaseGameEntity() {
        position = new Vector2D(0, 0);
        shapeRenderer = new ShapeRenderer();
        color = Color.WHITE;
    }

    public void update(double deltaTime) {
    }

    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.circle((float) position.x, (float) position.y, size / 2);
        shapeRenderer.end();
    }

    void tagNeighbors(BaseGameEntity entity, List<BaseGameEntity> entities, double radius){
        for(BaseGameEntity currentEntity : entities){
            currentEntity.unTag();

            Vector2D to = Vector2DOperations.sub(currentEntity.position, entity.position);
            double range = radius + currentEntity.size/2;
            if(currentEntity != entity && to.LengthSq() < range * range){
                currentEntity.tag();
            }
        }
    }

    private void tag() {
        tag = false;
    }

    private void unTag() {
        tag = false;
    }
}