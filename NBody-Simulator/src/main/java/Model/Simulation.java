package Model;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements SimulationEngine {

    private List<Body> bodies;
    private static final double BOUNCE_FACTOR = -0.8;
    private double width = 800.0;
    private double height = 600.0;

    public Simulation() {
        this.bodies = new ArrayList<>();
    }

    public Simulation(List<Body> bodies) {
        this.bodies = bodies;
    }

    @Override
    public void addBody(Body body) {
        this.bodies.add(body);
    }

    @Override
    public List<Body> getBodies() {
        return bodies;
    }

    @Override
    public void setDimensions(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(double dt) {
        if (bodies.isEmpty()) return;

        // Trava o Buraco Negro (Corpo 0) fixo no centro (0,0)
        Body buracoNegro = bodies.get(0);
        buracoNegro.setPosition(new Vector(0, 0));
        buracoNegro.setVelocity(new Vector(0, 0));

        for (Body body : bodies) {
            body.resetAcceleration();
        }

        for (int i = 1; i < bodies.size(); i++){
            Body bodyA = bodies.get(i);
            for (int j = 0; j < bodies.size(); j++){
                if (i == j) continue;
                Body bodyB = bodies.get(j);

                Vector forceExercisedByBodyB = bodyA.calculateForceFrom(bodyB);
                bodyA.applyForce(forceExercisedByBodyB);
            }
        }

        double halfX = width / 2.0;
        double halfY = height / 2.0;

        for (int i = 1; i < bodies.size(); i++){
            Body body = bodies.get(i);
            body.update(dt);

            double px = body.getPosition().getX();
            double py = body.getPosition().getY();
            double vx = body.getVelocity().getX();
            double vy = body.getVelocity().getY();
            boolean collided = false;

            if (px < -halfX) {
                px = -halfX;
                vx *= BOUNCE_FACTOR;
                collided = true;
            } else if (px > halfX) {
                px = halfX;
                vx *= BOUNCE_FACTOR;
                collided = true;
            }

            if (py < -halfY) {
                py = -halfY;
                vy *= BOUNCE_FACTOR;
                collided = true;
            } else if (py > halfY) {
                py = halfY;
                vy *= BOUNCE_FACTOR;
                collided = true;
            }

            if (collided) {
                body.setPosition(new Vector(px, py));
                body.setVelocity(new Vector(vx, vy));
            }
        }
    }
}