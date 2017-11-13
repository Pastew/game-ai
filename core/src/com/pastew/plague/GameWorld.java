package com.pastew.plague;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class GameWorld {

    private final static int ENEMIES_NUMBER = 1;
    private final static int COLUMN_NUMBER = 8;
    private SpriteBatch batch;
    private Player player;
    private List<BaseGameEntity> entities;
    private List<Column> columns;

    GameWorld() {
        entities = new ArrayList<BaseGameEntity>();
        player = new Player(this, 400, 400);
        entities.add(player);
        generateColumns();
        generateEnemies();

        batch = new SpriteBatch();
    }


    private void generateColumns() {
        columns = new ArrayList<Column>();

        Random random = new Random();

        float startX = 100;
        float startY = 100;
        float endX = Gdx.graphics.getWidth() - 100;
        float endY = Gdx.graphics.getHeight() - 100;

        while (columns.size() < COLUMN_NUMBER) {

            float x = (float) ((int) (Math.random() * (endX - startX)) + startX);
            float y = (float) ((int) (Math.random() * (endY - startY)) + startY);

            Column column = new Column(x, y);
            boolean found = false;
            for (Column otherColumn : columns) {
                if (column.position.Distance(otherColumn.position) <= column.size) {
                    found = true;
                    break;
                }

            }
            if (!found && player.position.Distance(column.position) >= (player.size / 2 + column.size / 2)) {
                columns.add(column);
            }
        }
    }

    private void generateEnemies() {
        Random random = new Random();

        for (int i = 0; i < ENEMIES_NUMBER; ++i) {
            float x = 10;
            float y = 10;
            if (random.nextBoolean())
                x = Gdx.graphics.getWidth() - i * 200;

            if (random.nextBoolean())
                y = Gdx.graphics.getHeight() - i * 200;

            Agent enemy = new Agent(this);
            enemy.position.x = 500;
            enemy.position.y = 500;

            entities.add(enemy);
        }
    }


    void update() {
        handleKeyboardInput();

        // Update each entity
        for (BaseGameEntity entity : entities)
            entity.update(Gdx.graphics.getDeltaTime());

        checkPlayerCollisions();
    }

    //checking player collisions with columns
    void checkPlayerCollisions() {
        boolean playerColliding = false;
        for (Column column : columns) {
            if (column.position.Distance(player.position) <= (column.size / 2f + player.size / 2f)) {
                playerColliding = true;
                break;
            }
        }
        if (playerColliding) {
            player.setPreviousPositionAndZeroVelocity();
        }
    }

    void render() {
        Gdx.gl.glClearColor(GameColors.bgColor.r,
                GameColors.bgColor.g,
                GameColors.bgColor.b,
                GameColors.bgColor.a);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (BaseGameEntity entity : entities)
            entity.render();
        //columns
        for (Column column : columns)
            column.render();

        batch.end();
    }

    private void handleKeyboardInput() {
        // Moving left and right
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            player.setXForceDirection(-1);
        else if (Gdx.input.isKeyPressed(Input.Keys.D))
            player.setXForceDirection(1);
        else
            player.setXForceDirection(0);

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            player.setYForceDirection(1);
        else if (Gdx.input.isKeyPressed(Input.Keys.S))
            player.setYForceDirection(-1);
        else
            player.setYForceDirection(0);

        // Crosshair
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        player.setCrosshairPosition(mouseX, mouseY);
    }

    void dispose() {
        batch.dispose();
    }

    Vector2D getPlayerVector2D() {
        return player.position;
    }

    public Column getColumn(int index) {
        return columns.get(index);
    }
    
    public List<BaseGameEntity> getObstaclesList(){
    
        List<BaseGameEntity> obstaclesList = new ArrayList<BaseGameEntity>();
        obstaclesList.addAll(columns);
        for (BaseGameEntity entity  : entities){
            if( entity != player){
            obstaclesList.add(entity);
            
            }
        }
     return obstaclesList;
    }

    public BaseGameEntity getPlayerBaseEntity() {
        return player;
    }
}
