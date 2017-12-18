package com.pastew.plague;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;

class Parameters {

    private static Parameters instance = null;
    private static BitmapFont font;

    // Player
    static MutableDouble PLAYER_MOVE_FORCE = new MutableDouble(10.0);
    static MutableDouble PLAYER_MAX_SPEED = new MutableDouble(5.);

    // Columns
    static int COLUMN_NUMBER = 8;

    // Bots
    static int BOTS_NUMBER = 10;

    static MutableDouble BOT_MAX_SPEED = new MutableDouble(30.);
    static MutableDouble BOT_MAX_FORCE = new MutableDouble(10.);


    static MutableDouble ARRIVE_DISTANCE_WHEN_STOP = new MutableDouble(50);

    static MutableDouble OBSTACLE_AVOIDANCE_MIN_DETECTION_BOX_LENGTH = new MutableDouble(40.);

    static MutableDouble WANDER_RADIUS = new MutableDouble(30.);
    static MutableDouble WANDER_DISTANCE = new MutableDouble(80.);
    static MutableDouble WANDER_JITTER = new MutableDouble(10.);

    static MutableDouble SEPARATION_RADIUS = new MutableDouble(30.);

    // Bots steering behaviours multipliers
    static MutableDouble WALL_AVOIDANCE_MULTIPLIER = new MutableDouble(2.);
    static MutableDouble OBSTACLE_AVOIDANCE_MULTIPLIER = new MutableDouble(2.);
    static MutableDouble HIDE_MULTIPLIER = new MutableDouble(0.11);
    static MutableDouble SEEK_AVOIDANCE_MULTIPLIER = new MutableDouble(1.);
    static MutableDouble WANDER_MULTIPLIER = new MutableDouble(10.);
    static MutableDouble SEPARATION_MULTIPLIER = new MutableDouble(1);
    static MutableDouble ARRIVE_MULTIPLIER = new MutableDouble(1);


    // Debug draw
    static boolean DRAW_DEBUG = true;

    private Parameters() {
        font = new BitmapFont();
        font.setColor(GameColors.fontColor);

        H = Gdx.graphics.getHeight();
        parameterToRendersArray = new ArrayList<Parameter>();

        parameterToRendersArray.add(new Parameter(PLAYER_MAX_SPEED, "player max speed"));
        parameterToRendersArray.add(new Parameter(PLAYER_MOVE_FORCE, "player move force"));

        //parameterToRendersArray.add(new Parameter(BOTS_NUMBER, "bots number"));
        parameterToRendersArray.add(new Parameter(BOT_MAX_SPEED, "bot max speed"));

        parameterToRendersArray.add(new Parameter(BOT_MAX_FORCE, "bot move force"));
        parameterToRendersArray.add(new Parameter(ARRIVE_DISTANCE_WHEN_STOP, "arrive - distance when stop"));

        parameterToRendersArray.add(new Parameter(OBSTACLE_AVOIDANCE_MIN_DETECTION_BOX_LENGTH, "obstacle min box length"));

        parameterToRendersArray.add(new Parameter(WANDER_RADIUS, "wander radius"));
        parameterToRendersArray.add(new Parameter(WANDER_JITTER, "wander jitter"));
        parameterToRendersArray.add(new Parameter(WANDER_DISTANCE, "wander distance"));

        parameterToRendersArray.add(new Parameter(SEPARATION_RADIUS, "separation radius"));

        parameterToRendersArray.add(new Parameter(ARRIVE_MULTIPLIER, "arrive multiplier", 0.1));
        parameterToRendersArray.add(new Parameter(WALL_AVOIDANCE_MULTIPLIER, "wall avoidance multiplier",0.1));
        parameterToRendersArray.add(new Parameter(OBSTACLE_AVOIDANCE_MULTIPLIER, "obstacle avoidance multiplier",0.1));
        parameterToRendersArray.add(new Parameter(HIDE_MULTIPLIER, "hide multiplier", 0.1));
        parameterToRendersArray.add(new Parameter(SEEK_AVOIDANCE_MULTIPLIER, "seek multiplier", 0.1));
        parameterToRendersArray.add(new Parameter(WANDER_MULTIPLIER, "wander multiplier", 0.1));
        parameterToRendersArray.add(new Parameter(SEPARATION_MULTIPLIER, "separation multiplier", 0.1));
    }

    public static Parameters getInstance() {
        if (instance == null)
            instance = new Parameters();

        return instance;
    }


    // Render
    static int H;
    static ArrayList<Parameter> parameterToRendersArray;
    static int currentSelect = 0;

    public void render(Batch batch) {
        //batch.setProjectionMatrix(camera.combined); //or your matrix to draw GAME WORLD, not UI
        for (int i = 0; i < parameterToRendersArray.size(); i++) {
            Parameter parameter = parameterToRendersArray.get(i);
            int margin = 5;
            String strToFormat = "   %s: %.1f";
            if (currentSelect == i)
                strToFormat = "-> %s: %.1f";

            font.draw(batch, String.format(strToFormat,
                    parameter.label, parameter.valueReference.getValue()),
                    10, H - 10 * i - margin * i);
        }
    }

    public static void selectNext() {
        if (currentSelect + 1 < parameterToRendersArray.size())
            currentSelect++;
    }

    public static void selectPrevious() {
        if (currentSelect - 1 >= 0)
            currentSelect--;
    }

    public static void increaseCurrentParameter() {
        parameterToRendersArray.get(currentSelect).increase();
    }

    public static void decreaseCurrentParameter() {
        parameterToRendersArray.get(currentSelect).decrease();
    }

    public static void drawDebugSwitch() {
        DRAW_DEBUG = !DRAW_DEBUG;
    }

    private class Parameter {
        MutableDouble valueReference;
        String label;
        double valueDiff;

        public Parameter(MutableDouble val, String label) {
            this(val, label, 1);
        }

        public Parameter(MutableDouble valueRef, String label, double valueDiff) {
            this.valueReference = valueRef;
            this.label = label;
            this.valueDiff = valueDiff;
        }

        public void decrease() {
            valueReference.subtract(valueDiff);
            if(valueReference.getValue() <=0)
                valueReference.setValue(0);
        }

        public void increase() {
            valueReference.add(valueDiff);
        }
    }
}
