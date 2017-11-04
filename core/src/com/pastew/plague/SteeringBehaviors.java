package com.pastew.plague;

import static com.pastew.plague.Vector2D.Vec2DDistanceSq;
import static java.lang.Math.min;

public class SteeringBehaviors {

    private Agent agent;

    private Vector2D seekTarget;
    private Vector2D fleeTarget;
    private Vector2D arriveTarget;

    SteeringBehaviors(Agent agent) {
        this.agent = agent;
    }

    Vector2D calculate() {
        Vector2D force = new Vector2D();

        if (seekTarget != null)
            force.add(seek(seekTarget));

        if (fleeTarget != null)
            force.add(flee(fleeTarget));

        if (arriveTarget != null)
            force.add(arrive(arriveTarget, Deceleration.fast));

        return force;
    }

    // ======== seek ========
    private Vector2D seek(Vector2D targetPosition) {
        Vector2D targetPositionCopy = new Vector2D(targetPosition);
        Vector2D desiredVelocity = Vector2D.Vec2DNormalize(targetPositionCopy.sub(agent.position)).mul(agent.maxSpeed);

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
        Vector2D desiredVelocity = Vector2D.Vec2DNormalize(agentPositionCopy.sub(targetPosition)).mul(agent.maxSpeed);

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
        final double DISTANCE_WHEN_STOP = 60;

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
}
