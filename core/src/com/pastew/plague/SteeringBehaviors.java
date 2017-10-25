package com.pastew.plague;


public class SteeringBehaviors {

    private Agent agent;

    private Vector2D seekTarget;

    SteeringBehaviors(Agent agent) {
        this.agent = agent;
    }

    Vector2D calculate() {
        Vector2D force = new Vector2D();

        if (seekTarget != null)
            force.add(Seek(seekTarget));

        return force;
    }


    private Vector2D Seek(Vector2D targetPosition) {
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
}
