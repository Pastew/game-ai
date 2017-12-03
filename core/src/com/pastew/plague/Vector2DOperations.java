package com.pastew.plague;

import static com.pastew.plague.Vector2D.*;



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
    
    public static boolean LineIntersection2D(Vector2D A,
            Vector2D B,
            Vector2D C,
            Vector2D D,
            Double dist,
            Vector2D point) {

        double rTop = (A.y - C.y) * (D.x - C.x) - (A.x - C.x) * (D.y - C.y);
        double rBot = (B.x - A.x) * (D.y - C.y) - (B.y - A.y) * (D.x - C.x);

        double sTop = (A.y - C.y) * (B.x - A.x) - (A.x - C.x) * (B.y - A.y);
        double sBot = (B.x - A.x) * (D.y - C.y) - (B.y - A.y) * (D.x - C.x);

        if ((rBot == 0) || (sBot == 0)) {
            //lines are parallel
            return false;
        }

        double r = rTop / rBot;
        double s = sTop / sBot;

        if ((r > 0) && (r < 1) && (s > 0) && (s < 1)) {
            dist= (Vec2DDistance(A, B) * r);

            point.set(add(A, mul(sub(B, A) , r)));

            return true;
        } else {
            dist=0.0;

            return false;
        }
    }

    public static double distance(Vector2D v1, Vector2D v2) {
        Vector2D fromV1toV2 = Vector2DOperations.sub(v2, v1);
        return fromV1toV2.getMagnitude();
    }
}
