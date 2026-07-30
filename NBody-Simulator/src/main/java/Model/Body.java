package Model;

public class Body {

    private double mass;
    private double radius;
    private Vector position;
    private Vector velocity;
    private Vector aceleration;

    public static final double G = 1.0;

    public Body(double mass, double radius, Vector position, Vector velocity) {
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
        this.aceleration = new Vector(0, 0);
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Vector getAceleration() {
        return aceleration;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void resetAcceleration() {
        this.aceleration = new Vector(0, 0);
    }

    public Vector calculateForceFrom(Body other){
        Vector delta = other.getPosition().subs(this.getPosition());
        double r = delta.magnitude();

        double softening = 1e-2; 
        double distanceSq = (r * r) + (softening * softening);

        Vector direction = delta.normalizacao();
        double magnitude = (G * this.getMass() * other.getMass()) / distanceSq;

        return direction.multiEscalar(magnitude);
    }

    public void applyForce (Vector force){
        Vector acelerationFromThisForce = force.multiEscalar(1.0/this.mass);
        this.aceleration = this.aceleration.sum(acelerationFromThisForce);
    }

    public void update (double dt){
        Vector velocityChange = this.aceleration.multiEscalar(dt);
        this.velocity = this.velocity.sum(velocityChange);

        Vector positionChange = this.velocity.multiEscalar(dt);
        this.position = this.position.sum(positionChange);
    }
}