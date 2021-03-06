package threebodysimulation.controller;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import threebodysimulation.model.Body;
import threebodysimulation.model.Simulation;
import threebodysimulation.view.SimulationView;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimulationController {

    private Simulation simulation;

    public SimulationView getSimulationView() {
        return simulationView;
    }
    public Simulation getCurrentSimulation() {
        return simulation;
    }

    private final SimulationView simulationView;

    private Preset currentPreset;

    public SimulationController() {
        this(defaultPreset());
    }

    public SimulationController(Preset preset) {
        simulationView = new SimulationView();
        currentPreset = preset;
        resetSimulation();
    }

    private void resetSimulation() {
        if (simulation != null)
            simulation.pause();
        simulationView.reset(currentPreset.bodies.size());
        initializeSimulation(currentPreset);
        simulationView.bindBodiesPositions((Iterable<ObjectProperty<Point2D>>) simulation.getBodies().stream().map(Body::positionProperty)::iterator);
    }

    private void initializeSimulation(Preset preset) {
        if (simulation == null)
            simulation = new Simulation();
        simulation.setGravitationalConstant(preset.gravitationalConstant);
        simulation.setScale(preset.scale);
        List<Body> simulationBodies = simulation.getBodies();
        simulationBodies.clear();
        preset.bodies.stream().map(Body::clone).forEach(simulationBodies::add);
    }

    public void loadPreset(Preset preset) {
        currentPreset = preset.clone();
        resetSimulation();
    }

    public Preset savePreset() {
        return null;
    }

    private static Preset defaultPreset() {
        final double vel = 1e1;
        final Point2D offset = new Point2D(100, 100);
        return new Preset(
                new Body(new Point2D(0, 0).add(offset), new Point2D(vel, -vel)),
                new Body(new Point2D(200, 0).add(offset), new Point2D(vel, vel)),
                new Body(new Point2D(200, 200).add(offset), new Point2D(-vel, vel)),
                new Body(new Point2D(0, 200).add(offset), new Point2D(-vel, -vel))
        );
    }

    public void start() {
        simulation.start();
    }

    public EventHandler<KeyEvent> getPauseUnpauseEventHandler(KeyCode key) {
        return event -> {
            if (event.getCode().equals(key)) {
                simulation.pauseUnpause();
            }
        };
    }

    public EventHandler<KeyEvent> getRestartEventHandler(KeyCode key) {
        return event -> {
            if (event.getCode().equals(key)) {
                resetSimulation();
            }
        };
    }

    public static class Preset implements Serializable, Cloneable {
        double gravitationalConstant;

        public double getGravitationalConstant() {
            return gravitationalConstant;
        }

        public void setGravitationalConstant(double gravitationalConstant) {
            this.gravitationalConstant = gravitationalConstant;
        }

        double scale;

        public double getScale() {
            return scale;
        }

        public void setScale(double scale) {
            this.scale = scale;
        }

        ObservableList<Body> bodies;

        public ObservableList<Body> getBodies() {
            return bodies;
        }

        public Preset() {
            this(Simulation.DEFAULT_GRAVITATIONAL_CONSTANT);
        }

        public Preset(double gravitationalConstant) {
            this(gravitationalConstant, 1);
        }

        public Preset(double gravitationalConstant, double scale) {
            this(gravitationalConstant, 1, (List<Body>) null);
        }

        public Preset(Collection<Body> bodies) {
            this(Simulation.DEFAULT_GRAVITATIONAL_CONSTANT, 1, bodies);
        }

        public Preset(Body... bodies) {
            this(Arrays.asList(bodies));
        }

        public Preset(double gravitationalConstant, Body... bodies) {
            this(gravitationalConstant, 1, bodies);
        }

        public Preset(double gravitationalConstant, double scale, Body... bodies) {
            this(gravitationalConstant, 1, Arrays.asList(bodies));
        }

        public Preset(double gravitationalConstant, double scale, Collection<Body> bodies) {
            this.gravitationalConstant = gravitationalConstant;
            this.scale = scale;
            if (bodies != null) {
                this.bodies = FXCollections.observableArrayList();
                for (Body body : bodies) {
                    this.bodies.add(body.clone());
                }
            } else {
                this.bodies = FXCollections.observableArrayList();
            }
        }

        @Override
        public String toString() {
            return String.format("Preset [gravitationalConstant = %f; bodies = %s", gravitationalConstant, bodies);
        }

        @Override
        public Preset clone() {
            return new Preset(gravitationalConstant, scale, bodies);
        }
    }
}
