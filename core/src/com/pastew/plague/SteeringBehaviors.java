package com.pastew.plague;


import static com.pastew.plague.Vector2D.Vec2DDistanceSq;

public class SteeringBehaviors {

    private Agent agent;

    private Vector2D seekTarget;
    private Vector2D fleeTarget;

    SteeringBehaviors(Agent agent) {
        this.agent = agent;
    }

    Vector2D calculate() {
        Vector2D force = new Vector2D();

        if (seekTarget != null)
            force.add(seek(seekTarget));

        if (fleeTarget != null)
            force.add(flee(fleeTarget));

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
        if (Vec2DDistanceSq(agent.position, targetPosition) > PanicDistanceSq)
        {
            return new Vector2D(0,0);
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
}
