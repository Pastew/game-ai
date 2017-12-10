package com.pastew.plague;



class Parameters {
    // Player
    static float PLAYER_MOVE_FORCE = 10;
    static float PLAYER_MAX_SPEED = 5;

    // Bots
    static int BOTS_NUMBER = 10;

    static float BOT_MAX_SPEED = 10;
    static double BOT_MAX_FORCE = 10;

    // Bots steering behaviours multipliers
    static double WALL_AVOIDANCE_MULTIPLIER = 2;
    static double OBSTACLE_AVOIDANCE_MULTIPLIER = 2;
    static double HIDE_MULTIPLIER = 1;
    static double SEEK_AVOIDANCE_MULTIPLIER = 1;
    static double WANDER_MULTIPLIER = 1;

    // Debug draw
    static final boolean DRAW_BOT_BOUNDING_BOX = false;
}
