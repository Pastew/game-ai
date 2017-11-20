package com.pastew.plague;

import com.badlogic.gdx.utils.compression.lzma.Base;

import static com.pastew.plague.Transformation.PointToWorldSpace;
import static com.pastew.plague.Vector2D.Vec2DDistanceSq;
import static com.pastew.plague.Vector2D.Vec2DNormalize;
import static com.pastew.plague.utils.RandFloat;
import static com.pastew.plague.utils.RandomClamped;
import static com.pastew.plague.utils.TwoPi;
import static java.lang.Math.min;

import java.util.List;

public class SteeringBehaviors {

    private Agent agent;

    private GameWorld gameworld;

    private Vector2D seekTarget;
    private Vector2D fleeTarget;
    private Vector2D arriveTarget;

    // Wander
    private boolean wandering = false;
    double wanderRadius = 1.2; // radius of the constraining circle
    double wanderDistance = 1.0; // distance the wander circle is projected in front of the agent
    double wanderJitter = 80.0; // maximum amount of random displacement that can be added to the target each second.
    double theta = RandFloat() * TwoPi;
    private Vector2D wanderTarget = new Vector2D(wanderRadius * Math.cos(theta),
            wanderRadius * Math.sin(theta));

    //obstacleAvoidance
    double minDetectionBoxLength = 100.0;
    private boolean obstacleAvoidance = false;
    private BaseGameEntity hideTarget; // Przed kim ma sie chowac


    SteeringBehaviors(Agent agent, GameWorld gameworld) {
        this.agent = agent;
        this.gameworld = gameworld;

    }

    Vector2D calculate() {
        Vector2D force = new Vector2D();

        if (seekTarget != null) {
            force.add(seek(seekTarget));
        }

        if (fleeTarget != null) {
            force.add(flee(fleeTarget));
        }

        if (arriveTarget != null) {
            force.add(arrive(arriveTarget, Deceleration.fast));
        }

        if (wandering) {
            force.add(wander());
        }

        if (obstacleAvoidance) {
            force.add(obstacleAvoidance());
        }

        if (hideTarget != null) {
            force.add(hide(hideTarget, gameworld.getColumns()));
        }
        return force;
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
        final double DISTANCE_WHEN_STOP = 10;

        if (dist > DISTANCE_WHEN_STOP) {
            //because Deceleration is enumerated as an int, this value is required
            //to provide fine tweaking of the deceleration.
            final double DecelerationTweaker = 0.3;

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
        double boxLength = minDetectionBoxLength + (agent.velocity.Length() / agent.maxSpeed) * minDetectionBoxLength;

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

    private Vector2D hide(BaseGameEntity target, List<Column> obstacles){

        double distanceToClosest = Double.MAX_VALUE;
        Vector2D bestHidingSpot = null;

        for (BaseGameEntity obstacle : obstacles){
            Vector2D hidingSpot = getHidingPosition(obstacle.position, obstacle.size, target.position);
            double distance = Vec2DDistanceSq(hidingSpot, agent.position);
            if (distance < distanceToClosest){
                distanceToClosest = distance;
                bestHidingSpot = hidingSpot;
            }
        }

        if(bestHidingSpot == null) {
            System.out.println("Flee");
            return flee(target.position);
        }

        System.out.println("hiding " + bestHidingSpot);

        return arrive(bestHidingSpot, Deceleration.fast);
    }

    public void turnOnHide(BaseGameEntity target) {
        this.hideTarget = target;
    }
    public void turnOffHide(BaseGameEntity target) {this.hideTarget = null; }


}
