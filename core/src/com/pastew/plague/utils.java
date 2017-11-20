/*
 * Desc: misc utility functions and constants
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.pastew.plague;

import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

/**
 *
 * @author Petr
 */
final public class utils {
    //a few useful finalants

    static public final int MaxInt = Integer.MAX_VALUE;
    static public final double MaxDouble = Double.MAX_VALUE;
    static public final double MinDouble = Double.MIN_VALUE;
    static public final float MaxFloat = Float.MAX_VALUE;
    static public final float MinFloat = Float.MIN_VALUE;
    static public final double Pi = Math.PI;
    static public final double TwoPi = Math.PI * 2;
    static public final double HalfPi = Math.PI / 2;
    static public final double QuarterPi = Math.PI / 4;
    static public final double EpsilonDouble = Double.MIN_NORMAL;

    /**
     * returns true if the value is a NaN
     */
    public static <T> boolean isNaN(T val) {
        return !(val != null);
    }

    static public double DegsToRads(double degs) {
        return TwoPi * (degs / 360.0);
    }

    //compares two real numbers. Returns true if they are equal
    static public boolean isEqual(float a, float b) {
        if (Math.abs(a - b) < 1E-12) {
            return true;
        }

        return false;
    }
//----------------------------------------------------------------------------
//  some random number functions.
//----------------------------------------------------------------------------
    static private Random rand = new Random();

    static public void setSeed(long seed) {
        rand.setSeed(seed);
    }

//returns a random integer between x and y
    static public int RandInt(int x, int y) {
        assert y >= x : "<RandInt>: y is less than x";
        return rand.nextInt(Integer.MAX_VALUE - x) % (y - x + 1) + x;
    }

//returns a random double between zero and 1
    static public double RandFloat() {
        return rand.nextDouble();
    }

    static public double RandInRange(double x, double y) {
        return x + RandFloat() * (y - x);
    }

//returns a random bool
    static public boolean RandBool() {
        if (RandFloat() > 0.5) {
            return true;
        } else {
            return false;
        }
    }

//returns a random double in the range -1 < n < 1
    static public double RandomClamped() {
        return RandFloat() - RandFloat();
    }

    //returns a random number with a normal distribution. See method at
    //http://www.taygeta.com/random/gaussian.html
    static public double RandGaussian() {
        return RandGaussian(0, 1);
    }
    static private double y2 = 0;
    static private boolean use_last = false;

    static public double RandGaussian(double mean, double standard_deviation) {

        double x1, x2, w, y1;

        if (use_last) /* use value from previous call */ {
            y1 = y2;
            use_last = false;
        } else {
            do {
                x1 = 2.0 * RandFloat() - 1.0;
                x2 = 2.0 * RandFloat() - 1.0;
                w = x1 * x1 + x2 * x2;
            } while (w >= 1.0);

            w = Math.sqrt((-2.0 * Math.log(w)) / w);
            y1 = x1 * w;
            y2 = x2 * w;
            use_last = true;
        }

        return (mean + y1 * standard_deviation);
    }

//-----------------------------------------------------------------------
//  
//  some handy little functions
//-----------------------------------------------------------------------
    public static double Sigmoid(double input) {
        return Sigmoid(input, 1.0);
    }

    public static double Sigmoid(double input, double response) {
        return (1.0 / (1.0 + Math.exp(-input / response)));
    }

//returns the maximum of two values
    public static <T extends Comparable> T MaxOf(T a, T b) {
        if (a.compareTo(b) > 0) {
            return a;
        }
        return b;
    }

//returns the minimum of two values
    public static <T extends Comparable> T MinOf(T a, T b) {
        if (a.compareTo(b) < 0) {
            return a;
        }
        return b;
    }

    /** 
     * clamps the first argument between the second two
     */
    public static <T extends Number> 
            T clamp(final T arg, final T minVal, final T maxVal) {
        assert (minVal.doubleValue() < maxVal.doubleValue()) : "<Clamp>MaxVal < MinVal!";

        if (arg.doubleValue() < minVal.doubleValue()) {
            return  minVal;
        }

        if (arg.doubleValue() > maxVal.doubleValue()) {
            return maxVal;
        }
        return arg;
    }

    static public boolean isEqual(double a, double b) {
        if (Math.abs(a - b) < 1E-12) {
            return true;
        }

        return false;
    }

    /*
    // LINE/CIRCLE
    boolean lineCircle(float x1, float y1, float x2, float y2, float cx, float cy, float r) {

        // is either end INSIDE the circle?
        // if so, return true immediately
        boolean inside1 = pointCircle(x1,y1, cx,cy,r);
        boolean inside2 = pointCircle(x2,y2, cx,cy,r);
        if (inside1 || inside2) return true;

        // get length of the line
        double distX = x1 - x2;
        double distY = y1 - y2;
        double len = sqrt( (distX*distX) + (distY*distY) );

        // get dot product of the line and circle
        double dot = ( ((cx-x1)*(x2-x1)) + ((cy-y1)*(y2-y1)) ) / pow(len,2);

        // find the closest point on the line
        double closestX = x1 + (dot * (x2-x1));
        double closestY = y1 + (dot * (y2-y1));

        // is this point actually on the line segment?
        // if so keep going, but if not, return false
        boolean onSegment = linePoint(x1,y1,x2,y2, closestX,closestY);
        if (!onSegment) return false;


        // get distance to closest point
        distX = closestX - cx;
        distY = closestY - cy;
        double distance = sqrt( (distX*distX) + (distY*distY) );

        if (distance <= r) {
            return true;
        }
        return false;
    }

    boolean pointCircle(float px, float py, float cx, float cy, float r) {

        // get distance between the point and circle's center
        // using the Pythagorean Theorem
        float distX = px - cx;
        float distY = py - cy;
        double distance = sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the circle's
        // radius the point is inside!
        if (distance <= r) {
            return true;
        }
        return false;
    }

    // LINE/POINT
    boolean linePoint(float x1, float y1, float x2, float y2, float px, float py) {

        // get distance from the point to the two ends of the line
        float d1 = (px,py, x1,y1);
        float d2 = dist(px,py, x2,y2);

        // get the length of the line
        float lineLen = dist(x1,y1, x2,y2);

        // since floats are so minutely accurate, add
        // a little buffer zone that will give collision
        double buffer = 0.1;    // higher # = less accurate

        // if the two distances are equal to the line's
        // length, the point is on the line!
        // note we use the buffer here to give a range,
        // rather than one #
        if (d1+d2 >= lineLen-buffer && d1+d2 <= lineLen+buffer) {
            return true;
        }
        return false;
    }
    */
}
