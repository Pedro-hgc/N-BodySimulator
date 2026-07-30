package Model;

import java.util.List;

public interface SimulationEngine {
    void addBody(Body body);
    void update(double dt);
    List<Body> getBodies();
    void setDimensions(double width, double height);
}