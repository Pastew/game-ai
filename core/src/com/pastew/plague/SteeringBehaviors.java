package com.pastew.plague;

import static com.pastew.plague.Transformation.PointToWorldSpace;
import static com.pastew.plague.Transformation.Vec2DRotateAroundOrigin;
import static com.pastew.plague.Vector2D.Vec2DDistanceSq;
import static com.pastew.plague.Vector2D.Vec2DNormalize;
import static com.pastew.plague.Vector2D.mul;
import static com.pastew.plague.Vector2D.sub;
import static com.pastew.plague.Vector2DOperations.LineIntersection2D;
import static com.pastew.plague.utils.RandFloat;
import static com.pastew.plague.utils.RandomClamped;
import static com.pastew.plague.utils.TwoPi;
import static java.lang.Math.min;

import java.util.ArrayList;

import java.util.List;

public class SteeringBehaviors {

    private Agent agent;

    private GameWorld gameworld;

    private Vector2D seekTarget;
    private Vector2D fleeTarget;
    private Vector2D arriveTarget;

    //wander
    private boolean wandering = false;
    double wanderRadius = Parameters.WANDER_RADIUS; // radius of the constraining circle
    double wanderDistance = Parameters.WANDER_DISTANCE; // distance the wander circle is projected in front of the agent
    double wanderJitter = Parameters.WANDER_JITTER; // maximum amount of random displacement that can be added to the target each second.
    double theta = RandFloat() * TwoPi;
    private Vector2D wanderTarget = new Vector2D(wanderRadius * Math.cos(theta),
            wanderRadius * Math.sin(theta));

    //obstacleAvoidance
    double minDetectionBoxLength = Parameters.OBSTACLE_AVOIDANCE_MIN_DETECTION_BOX_LENGTH;
    private boolean obstacleAvoidance = false;
    private BaseGameEntity hideTarget; // Przed kim ma sie chowac

    //wallAvoidance
    private List<Vector2D> m_Feelers = new ArrayList<Vector2D>();
    private double m_dWallDetectionFeelerLength = 40.0;
    private boolean wallAvoidance;
    public double boxLength = 0;

    SteeringBehaviors(Agent agent, GameWorld gameworld) {
        this.agent = agent;
        this.gameworld = gameworld;
    }

    Vector2D calculate() {
        Vector2D steeringForce = new Vector2D(0, 0);
        Vector2D force = new Vector2D(0, 0);

        // Zachowania są posortowane od tych najważniejszych do mniej ważnych.

        if (wallAvoidance) {
            force = WallAvoidance();
            force.mul(Parameters.WALL_AVOIDANCE_MULTIPLIER);
            if (!AccumulateForce(steeringForce, force)){
                System.out.println("Ending with wall avoidance");
                return steeringForce;
            }
        }

        if (obstacleAvoidance) {
            force = obstacleAvoidance();
            force.mul(Parameters.OBSTACLE_AVOIDANCE_MULTIPLIER);
            if (!AccumulateForce(steeringForce, force)) {
                System.out.println("Ending with obstacle avoidance");
                return steeringForce;
            }

        }

        if (hideTarget != null) {
            force = hide(hideTarget, gameworld.getColumns());
            force.mul(Parameters.HIDE_MULTIPLIER);
            if (!AccumulateForce(steeringForce, force)) {
                System.out.println("Ending with hide ");
                return steeringForce;
            }
        }

        if (seekTarget != null) {
            force = seek(seekTarget);
            force.mul(Parameters.SEEK_AVOIDANCE_MULTIPLIER);
            if (!AccumulateForce(steeringForce, force)){
                System.out.println("Ending with seek ");
                return steeringForce;
            }
        }

        if (wandering) {
            force = wander();
            force.mul(Parameters.WANDER_MULTIPLIER);
            if (!AccumulateForce(steeringForce, force)){
                System.out.println("Ending with wander ");
                return steeringForce;
            }
        }

//        if (fleeTarget != null) {
//            steeringForce.add(flee(fleeTarget));
//        }

//        if (arriveTarget != null) {
//            steeringForce.add(arrive(arriveTarget, Deceleration.fast));
//        }

        return steeringForce;
    }

    private boolean AccumulateForce(Vector2D steeringForce, Vector2D forceToAdd) {
        double magnitudeSoFar = steeringForce.Length();
        double magnitudeRemaining = agent.maxForce - magnitudeSoFar;
        if (magnitudeRemaining <= 0.0) return false;
        double magnitudeToAdd = forceToAdd.Length();
        if (magnitudeToAdd < magnitudeRemaining) {
            steeringForce.add(forceToAdd);
        } else {
            steeringForce.add(forceToAdd.normalise().mul(magnitudeRemaining));
        }

        return true;
    }

    // ======== seek ========
    private Vector2D seek(Vector2D targetPosition) {
        Vector2D targetPositionCopy = new Vector2D(targetPosition);
        Vector2D desiredVelocity = Vec2DNormalize(targetPositionCopy.sub(agent.position)).mul(agent.maxSpeed);

        return (desiredVelocity.sub(agent.velocity));
    }

    void turnOnSeek(Vector2D target) {
        this.seekTarget = target;
    }

    public void turnOffSeek() {
        this.seekTarget = null;
    }

    // ========= flee ========
    private Vector2D flee(Vector2D targetPosition) {

        final double PanicDistanceSq = 100.0 * 100.0;
        if (Vec2DDistanceSq(agent.position, targetPosition) > PanicDistanceSq) {
            return new Vector2D(0, 0);
        }

        Vector2D agentPositionCopy = new Vector2D(agent.position);
        Vector2D desiredVelocity = Vec2DNormalize(agentPositionCopy.sub(targetPosition)).mul(agent.maxSpeed);

        return (desiredVelocity.sub(agent.velocity));
    }

    void turnOnFlee(Vector2D target) {
        this.fleeTarget = target;
    }

    public void turnOffFlee() {
        this.fleeTarget = null;
    }

    // ========= arrive ========
    public Vector2D arrive(Vector2D targetPos, Deceleration deceleration) {
        Vector2D toTarget = Vector2DOperations.sub(targetPos, agent.position);

        //calculate the distance to the target position
        double dist = toTarget.Length();
        final double DISTANCE_WHEN_STOP = 100;

        if (dist > DISTANCE_WHEN_STOP) {
            //because Deceleration is enumerated as an int, this value is required
            //to provide fine tweaking of the deceleration.
            final double DecelerationTweaker = 1.3;

            //calculate the speed required to reach the target given the desired
            //deceleration
            double speed = dist / ((double) deceleration.getValue() * DecelerationTweaker);

            //make sure the velocity does not exceed the max
            speed = min(speed, agent.maxSpeed);

            //from here proceed just like Seek except we don't need to normalize
            //the ToTarget vector because we have already gone to the trouble
            //of calculating its length: dist.
            Vector2D DesiredVelocity = toTarget.mul(speed).div(dist);
            return (DesiredVelocity.sub(agent.velocity));
        }

        return new Vector2D(0, 0);
    }

    void turnOnArrive(Vector2D target) {
        this.arriveTarget = target;
    }

    public void turnOffArrive() {
        this.arriveTarget = null;
    }

    // ========= wander ========
    public Vector2D wander() {
        // first, add a small random vector to the target’s position
        // (RandomClamped returns a value between -1 and 1)
        wanderTarget.add(new Vector2D(
                RandomClamped() * wanderJitter,
                RandomClamped() * wanderJitter));

        // reproject this new vector back onto a unit circle
        wanderTarget.Normalize();
        wanderTarget.mul(wanderRadius);

        //move the target into a position WanderDist in front of the agent
        Vector2D targetLocal = Vector2DOperations.add(wanderTarget, new Vector2D(wanderDistance, 0));

        //project the target into world space
        Vector2D targetWorld = PointToWorldSpace(targetLocal,
                agent.heading,
                agent.side,
                agent.position);

        return Vector2DOperations.sub(targetWorld, agent.position);
    }

    public void turnOnWander() {
        this.wandering = true;
    }

    public void turnOffWander() {
        this.wandering = false;
    }

    // ==================obstacleAvoidance=======================
    public Vector2D obstacleAvoidance() {
        //the detection box length is proportional to the agent's velocity
        boxLength = minDetectionBoxLength + (agent.velocity.Length() / agent.maxSpeed) * minDetectionBoxLength;

        //tag all obstacles within range of the box for processing
//m_pVehicle->World()->TagObstaclesWithinViewRange(m_pVehicle, m_dDBoxLength);
//this will keep track of the closest intersecting obstacle (CIB)
        BaseGameEntity ClosestIntersectingObstacle = null;

//this will be used to track the distance to the CIB
        double DistToClosestIntersectionObstacle = Double.MAX_VALUE;
//this will record the transformed local coordinates of the CIB

        Vector2D LocalPosOfClosestObstacle = new Vector2D();

        List<BaseGameEntity> obstaclesList = gameworld.getObstacles();

        for (BaseGameEntity obstacle : obstaclesList) {

//if the obstacle has been tagged within range proceed
            if (obstacle.position.Distance(agent.position) - obstacle.size / 2 - agent.size / 2 < boxLength && obstacle != agent) {
//calculate this obstacle's position in local space
                Vector2D LocalPos = Transformation.PointToLocalSpace(obstacle.position,
                        agent.heading,
                        agent.side,
                        agent.position);

//if the local position has a negative x value then it must lay
//behind the agent. (in which case it can be ignored)
                if (LocalPos.x >= 0) {
//if the distance from the x axis to the object's position is less
//than its radius + half the width of the detection box then there
//is a potential intersection.
                    double ExpandedRadius = obstacle.size / 2 + agent.size / 2;

                    if (Math.abs(LocalPos.y) < ExpandedRadius) {
//now to do a line/circle intersection test. The center of the
//circle is represented by (cX, cY). The intersection points are
//given by the formula x = cX +/-sqrt(r^2-cY^2) for y=0.
//We only need to look at the smallest positive value of x because
//that will be the closest point of intersection.
                        double cX = LocalPos.x;
                        double cY = LocalPos.y;
//we only need to calculate the sqrt part of the above equation once
                        double SqrtPart = Math.sqrt(ExpandedRadius * ExpandedRadius - cY * cY);
                        double ip = cX - SqrtPart;
// A = cX 
                        if (ip <= 0) {
                            ip = cX + SqrtPart;
                        }
//test to see if this is the closest so far. If it is, keep a
//record of the obstacle and its local coordinates
                        if (ip < DistToClosestIntersectionObstacle) {
                            DistToClosestIntersectionObstacle = ip;
                            ClosestIntersectingObstacle = obstacle;

                            LocalPosOfClosestObstacle = LocalPos;
                        }
                    }
                }
            }

        }

        //if we have found an intersecting obstacle, calculate a steering
//force away from it
        Vector2D SteeringForce = new Vector2D();

        if (ClosestIntersectingObstacle != null) {
//the closer the agent is to an object, the stronger the steering force

            //should be
            double multiplier = 1.0 + (boxLength - LocalPosOfClosestObstacle.x) / boxLength;

//calculate the lateral force
            SteeringForce.y = (ClosestIntersectingObstacle.size / 2 - LocalPosOfClosestObstacle.y) * multiplier;
//apply a braking force proportional to the obstacle’s distance from
//the vehicle.
            double BrakingWeight = 0.2;
            SteeringForce.x = (ClosestIntersectingObstacle.size / 2 - LocalPosOfClosestObstacle.x) * BrakingWeight;
        }
//finally, convert the steering vector from local to world space

        return Transformation.VectorToWorldSpace(SteeringForce,
                agent.heading,
                agent.side);

    }

    public void turnOnObstacleAvoidance() {
        this.obstacleAvoidance = true;
    }

    public void turnOffObstacleAvoidance() {
        this.obstacleAvoidance = false;
    }


    /**
     * Creates the antenna utilized by WallAvoidance
     */
    private void CreateFeelers() {


        m_Feelers.clear();
        //feeler pointing straight in front
        m_Feelers.add(Vector2DOperations.add(agent.position, mul(m_dWallDetectionFeelerLength, agent.heading)));

        //feeler to left
        Vector2D temp = new Vector2D(agent.heading);
        Vec2DRotateAroundOrigin(temp, (Math.PI / 2) * 3.5f);
        m_Feelers.add(Vector2DOperations.add(agent.position, mul(m_dWallDetectionFeelerLength / 2.0f, temp)));

        //feeler to right
        temp = new Vector2D(agent.heading);
        Vec2DRotateAroundOrigin(temp, (Math.PI / 2) * 0.5f);
        m_Feelers.add(Vector2DOperations.add(agent.position, mul(m_dWallDetectionFeelerLength / 2.0f, temp)));
    }


    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    // ================== Wall Avoidance =======================
    Vector2D WallAvoidance() {
        List<Wall> walls = gameworld.getWalls();
//the feelers are contained in a std::vector, m_Feelers
        double DistToThisIP = 0.0;
        double DistToClosestIP = Double.MAX_VALUE;
//this will hold an index into the vector of walls
        int ClosestWall = -1;
        Vector2D SteeringForce = new Vector2D();
        Vector2D point = new Vector2D();//used for storing temporary info
        Vector2D ClosestPoint = new Vector2D(); //holds the closest intersection point

        CreateFeelers();

//examine each feeler in turn
        for (int flr = 0; flr < m_Feelers.size(); ++flr) {
//run through each wall checking for any intersection points
            for (int w = 0; w < walls.size(); ++w) {
                if (LineIntersection2D(agent.position, m_Feelers.get(flr), walls.get(w).From(), walls.get(w).To(), DistToThisIP, point)) {

//is this the closest found so far? If so keep a record
                    if (DistToThisIP < DistToClosestIP) {
                        DistToClosestIP = DistToThisIP;
                        ClosestWall = w;
                        ClosestPoint = point;
                    }
                }
            }//next wall
//if an intersection point has been detected, calculate a force
//that will direct the agent away
            if (ClosestWall >= 0) {
//calculate by what distance the projected position of the agent
//will overshoot the wall
                Vector2D OverShoot = sub(m_Feelers.get(flr), ClosestPoint);
//create a force in the direction of the wall normal, with a
//magnitude of the overshoot
                SteeringForce = mul(walls.get(ClosestWall).Normal(), OverShoot.Length());
            }
        }//next feeler
        return SteeringForce;
    }


    void turnOnWallAvoidance() {
        this.wallAvoidance = true;
    }

    void turnOffWallAvoidance() {
        this.wallAvoidance = false;
    }

    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    // ================== Hide =======================
    public static Vector2D getHidingPosition(Vector2D obstaclePosition, double obstacleRadius, Vector2D targetPosition) {
        //calculate how far away the agent is to be from the chosen obstacle’s bounding radius
        double distanceFromBoundary = 30.0;
        double distAway = obstacleRadius + distanceFromBoundary;

        //calculate the heading toward the object from the target
        Vector2D toObstacle = Vec2DNormalize(Vector2DOperations.sub(obstaclePosition, targetPosition));

        //scale it to size and add to the obstacle's position to get the hiding spot.
        return Vector2DOperations.add((Vector2DOperations.mul(toObstacle, distAway)), obstaclePosition);
    }

    private Vector2D hide(BaseGameEntity target, List<Column> obstacles) {

        double distanceToClosest = Double.MAX_VALUE;
        Vector2D bestHidingSpot = null;

        for (BaseGameEntity obstacle : obstacles) {
            Vector2D hidingSpot = getHidingPosition(obstacle.position, obstacle.size, target.position);
            double distance = Vec2DDistanceSq(hidingSpot, agent.position);
            if (distance < distanceToClosest) {
                distanceToClosest = distance;
                bestHidingSpot = hidingSpot;
            }
        }

        if (bestHidingSpot == null) {
            return flee(target.position);
        }

        return arrive(bestHidingSpot, Deceleration.fast);
    }

    public void turnOnHide(BaseGameEntity target) {
        this.hideTarget = target;
    }

    public void turnOffHide(BaseGameEntity target) {
        this.hideTarget = null;
    }


}
