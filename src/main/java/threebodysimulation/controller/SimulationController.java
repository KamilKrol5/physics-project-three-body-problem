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
            Point2D velocity;
            double vel = 1e1;
            switch (i) {
            case 0:
                position = new Point2D(100, 100);
                velocity = new Point2D(vel, -vel);
                break;
            case 1:
                position = new Point2D(300, 100);
                velocity = new Point2D(vel, vel);
                break;
            case 3:
                position = new Point2D(100, 300);
                velocity = new Point2D(-vel, -vel);
                break;
            case 2:
                position = new Point2D(300, 300);
                velocity = new Point2D(-vel, vel);
                break;
            default:
                position = new Point2D(0, 0);
                velocity = new Point2D(0, 0);
                break;
            }
            bodies.get(i).setPosition(position);
            bodies.get(i).setVelocity(velocity);
        }
    }

    public void start() {
        simulation.start();
    }
}
