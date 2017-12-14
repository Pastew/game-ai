package com.pastew.plague;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;

class Parameters {

    private static Parameters instance = null;
    private static BitmapFont font;

    private Parameters() {
        font = new BitmapFont();
        font.setColor(GameColors.fontColor);

        H = Gdx.graphics.getHeight();
        parameterToRendersArray = new ArrayList<ParameterToRender>();

        parameterToRendersArray.add(new ParameterToRender(PLAYER_MAX_SPEED, "player max speed"));
        parameterToRendersArray.add(new ParameterToRender(PLAYER_MOVE_FORCE, "player move force"));

        parameterToRendersArray.add(new ParameterToRender(BOTS_NUMBER, "bots number"));
        parameterToRendersArray.add(new ParameterToRender(BOT_MAX_SPEED, "bot max speed"));
        parameterToRendersArray.add(new ParameterToRender(BOT_MAX_FORCE, "bot move force"));

        parameterToRendersArray.add(new ParameterToRender(OBSTACLE_AVOIDANCE_MIN_DETECTION_BOX_LENGTH, "obstacle min box length"));

        parameterToRendersArray.add(new ParameterToRender(WANDER_RADIUS, "wander radius"));
        parameterToRendersArray.add(new ParameterToRender(WANDER_JITTER, "wander jitter"));
        parameterToRendersArray.add(new ParameterToRender(WANDER_DISTANCE, "wander distance"));

        parameterToRendersArray.add(new ParameterToRender(WALL_AVOIDANCE_MULTIPLIER, "wall avoidance multiplier"));
        parameterToRendersArray.add(new ParameterToRender(OBSTACLE_AVOIDANCE_MULTIPLIER, "obstacle avoidance multiplier"));
        parameterToRendersArray.add(new ParameterToRender(HIDE_MULTIPLIER, "hide avoidance multiplier"));
        parameterToRendersArray.add(new ParameterToRender(SEEK_AVOIDANCE_MULTIPLIER, "seek avoidance multiplier"));
        parameterToRendersArray.add(new ParameterToRender(WANDER_MULTIPLIER, "wander avoidance multiplier"));
    }

    public static Parameters getInstance() {
        if (instance == null)
            instance = new Parameters();

        return instance;
    }

    // Player
    static Double PLAYER_MOVE_FORCE = 10.0;
    static Double PLAYER_MAX_SPEED = 5.;

    // Bots
    static Integer BOTS_NUMBER = 10;

    static Double BOT_MAX_SPEED = 30.;
    static Double BOT_MAX_FORCE = 10.;

    static Double OBSTACLE_AVOIDANCE_MIN_DETECTION_BOX_LENGTH = 40.;

    static Double WANDER_RADIUS = 1.2;
    static Double WANDER_DISTANCE = 1.;
    static Double WANDER_JITTER = 80.;

    // Bots steering behaviours multipliers
    static Double WALL_AVOIDANCE_MULTIPLIER = 2.;
    static Double OBSTACLE_AVOIDANCE_MULTIPLIER = 2.;
    static Double HIDE_MULTIPLIER = 0.11;
    static Double SEEK_AVOIDANCE_MULTIPLIER = 1.;
    static Double WANDER_MULTIPLIER = 10.;

    // Debug draw
    static final boolean DRAW_BOT_BOUNDING_BOX = true;


    // Render
    static int H;
    static ArrayList<ParameterToRender> parameterToRendersArray;
    static int currentSelect = 0;

    public void render(Batch batch) {
        //batch.setProjectionMatrix(camera.combined); //or your matrix to draw GAME WORLD, not UI
        for (int i = 0; i < parameterToRendersArray.size(); i++) {
            ParameterToRender parameterToRender = parameterToRendersArray.get(i);
            int margin = 5;
            String strToFormat = "   %s: %s";
            if(currentSelect == i )
                strToFormat = "-> %s: %s";

            font.draw(batch, String.format(strToFormat,
                    parameterToRender.label, parameterToRender.valueReference),
                    10, H - 10 * i - margin * i);
        }
    }

    public static void selectNext() {
        if(currentSelect+1 < parameterToRendersArray.size())
            currentSelect++;
    }

    public static void selectPrevious() {
        if(currentSelect-1 >=0)
            currentSelect--;
    }

    private class ParameterToRender {
        Number valueReference;
        String label;

        public ParameterToRender(Number valueRef, String label) {
            this.valueReference = valueRef;
            this.label = label;
        }

    }
}
