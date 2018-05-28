package threebodysimulation.controller;

import javafx.geometry.Point2D;
import threebodysimulation.model.Body;
import threebodysimulation.model.Simulation;
import threebodysimulation.view.SimulationView;

import java.util.List;

public class SimulationController {

    private Simulation simulation;
    public SimulationView getSimulationView() { return simulationView; }

    private SimulationView simulationView;

    public SimulationController() {
        init(3);
    }

    public SimulationController(int numberOfBodies) {
        init(numberOfBodies);
    }

    private void init(int numberOfBodies) {
        simulation = new Simulation(numberOfBodies);
        simulationView = new SimulationView(numberOfBodies);
        simulationView.bindBodiesPositions(simulation.getBodies().stream().map(Body::positionProperty)::iterator);

        fillExampleData(simulation.getBodies());
    }

    private void fillExampleData(List<Body> bodies) {
        for (int i = 0; i < bodies.size(); i++) {
            Point2D position;
            switch (i) {
            case 0:
                position = new Point2D(50, 50);
                break;
            case 1:
                position = new Point2D(400, 200);
                break;
            case 2:
                position = new Point2D(100, 300);
                break;
            default:
                position = new Point2D(0, 0);
                break;
            }
            bodies.get(i).setPosition(position);
        }
    }

    public void start() {
        simulation.start();
    }
}
