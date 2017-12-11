package com.pastew.plague;



class Parameters {
    // Player
    static float PLAYER_MOVE_FORCE = 10;
    static float PLAYER_MAX_SPEED = 5;

    // Bots
    static int BOTS_NUMBER = 10;

    static float BOT_MAX_SPEED = 30;
    static double BOT_MAX_FORCE = 10;

    static double OBSTACLE_AVOIDANCE_MIN_DETECTION_BOX_LENGTH = 40;

    static double WANDER_RADIUS = 1.2;
    static double WANDER_DISTANCE = 1;
    static double WANDER_JITTER = 80;

    // Bots steering behaviours multipliers
    static double WALL_AVOIDANCE_MULTIPLIER = 2;
    static double OBSTACLE_AVOIDANCE_MULTIPLIER = 2;
    static double HIDE_MULTIPLIER = 0.11;
    static double SEEK_AVOIDANCE_MULTIPLIER = 1;
    static double WANDER_MULTIPLIER = 10;

    // Debug draw
    static final boolean DRAW_BOT_BOUNDING_BOX = true;
}
