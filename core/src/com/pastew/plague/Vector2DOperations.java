package com.pastew.plague;


public class Vector2DOperations {

    public static Vector2D sub(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector2D add(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2D mul(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x * v2.x, v1.y * v2.y);
    }

    public static Vector2D mul(Vector2D v1, double scalar) {
        return new Vector2D(v1.x * scalar, v1.y * scalar);
    }
}
