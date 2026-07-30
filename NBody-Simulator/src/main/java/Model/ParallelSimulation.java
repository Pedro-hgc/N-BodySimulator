package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelSimulation implements SimulationEngine {

    private static final double BOUNCE_FACTOR = -0.8;
    private int subSteps = 15;

    private double width = 800.0;
    private double height = 600.0;

    private final List<Body> bodies;

    public ParallelSimulation() {
        this.bodies = new ArrayList<>();
    }

    public ParallelSimulation(List<Body> bodies) {
        this.bodies = bodies;
    }

    @Override
    public List<Body> getBodies() {
        return bodies;
    }

    @Override
    public void addBody(Body body) {
        this.bodies.add(body);
    }

    @Override
    public void setDimensions(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void setSubSteps(int subSteps) {
        this.subSteps = Math.max(1, subSteps);
    }

    @Override
    public void update(double dt) {
        int n = bodies.size();
        if (n == 0) return;

        double miniDt = dt / subSteps;
        double halfX = width / 2.0;
        double halfY = height / 2.0;

        for (int step = 0; step < subSteps; step++) {

            // Trava o Buraco Negro (Corpo 0) fixo no centro (0,0)
            Body buracoNegro = bodies.get(0);
            buracoNegro.setPosition(new Vector(0, 0));
            buracoNegro.setVelocity(new Vector(0, 0));

            bodies.parallelStream().forEach(Body::resetAcceleration);

            // Calcula gravidade apenas a partir do corpo 1 em diante
            IntStream.range(1, n).parallel().forEach(i -> {
                Body bodyA = bodies.get(i);
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        Body bodyB = bodies.get(j);
                        Vector force = bodyA.calculateForceFrom(bodyB);
                        bodyA.applyForce(force);
                    }
                }
            });

            // Atualização física e limitação de bordas
            IntStream.range(1, n).parallel().forEach(i -> {
                Body body = bodies.get(i);
                body.update(miniDt);

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
            });
        }
    }
}