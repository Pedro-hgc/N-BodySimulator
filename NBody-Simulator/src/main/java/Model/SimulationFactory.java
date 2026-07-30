package Model;

import java.util.ArrayList;

public class SimulationFactory {

    public static SimulationEngine createSequential() {
        return new Simulation(new ArrayList<>());
    }

    public static SimulationEngine createParallel() {
        return new ParallelSimulation(new ArrayList<>());
    }

    public static SimulationEngine createGPU() {
        return new GPUSimulation(new ArrayList<>());
    }
}