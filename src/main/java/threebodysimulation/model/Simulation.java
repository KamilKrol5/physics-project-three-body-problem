package threebodysimulation.model;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;


public class Simulation {
    public static final double DEFAULT_GRAVITATIONAL_CONSTANT = 6.67408e-11;

    private ObservableList<Body> bodies = FXCollections.observableArrayList();
    public ObservableList<Body> getBodies() { return bodies; }

    private DoubleProperty scale = new SimpleDoubleProperty(this, "scale", 1);
    public DoubleProperty ScaleProperty() { return scale; }
    public double getScale() { return scale.get(); }
    public void setScale(double newValue) { scale.setValue(newValue); }

    private DoubleProperty gravitationalConstant = new SimpleDoubleProperty(this, "gravitationalConstant", 6.67408e-11);
    public DoubleProperty GravitationalConstantProperty() { return gravitationalConstant; }
    public double getGravitationalConstant() { return gravitationalConstant.get(); }
    public void setGravitationalConstant(double newValue) { gravitationalConstant.setValue(newValue); }

    public Simulation() {

    }

    public Simulation(int numberOfBodies) {
        for (int i = 0; i < numberOfBodies; i++) {
            bodies.add(new Body());
        }
    }

    private Point2D calculateForce(Body body1, Body body2) {
        Point2D direction = body2.getPosition().subtract(body1.getPosition());
        //System.err.format("position difference %s\n", direction);
        double magnitude = getGravitationalConstant() * ((body1.getMass() * body2.getMass()) / (direction.magnitude() * direction.magnitude()));
        //System.err.format("magnitude %f\n", magnitude);
        return direction.normalize().multiply(magnitude);
    }

    private void updateVelocity(Body _this, double deltaTime) {
        Point2D netForce = new Point2D(0, 0);
        for (Body body : bodies) {
            if (body == _this) continue;
            Point2D force = calculateForce(_this, body);
            //System.err.format("force %s\n", force);
            netForce = netForce.add(force);
        }

        _this.setVelocity(_this.getVelocity().add(netForce.multiply(deltaTime / _this.getMass())));
        //System.err.format("new velocity %s\n", _this.getVelocity());
    }

    private void updatePosition(Body body, double deltaTime) {
        body.setPosition(body.getPosition().add(body.getVelocity().multiply(deltaTime)));
    }

    private AnimationTimer animationTimer;
    private boolean isAnimationRunning = false;
    private boolean firstTick = true;

    public void start() {
        if (animationTimer == null) {
            animationTimer = new AnimationTimer() {
                long lastUpdate;
                final long fixedStep = 20000000;
                final double fixedStepSeconds = fixedStep / 1e9;
                long dt;

                @Override
                public void handle(long now) {
                    dt += firstTick ? 0 : (now - lastUpdate);
                    firstTick = false;
                    System.err.format("frame delta time: %d, fixed steps: %d\n", now - lastUpdate, dt / fixedStep);
                    lastUpdate = now;

                    while (dt >= fixedStep) {
                        for (Body body : bodies) {
                            updateVelocity(body, fixedStepSeconds);
                        }
                        //System.err.println();
                        for (Body body : bodies) {
                            updatePosition(body, fixedStepSeconds);
                            //System.err.println(body.getPosition());
                        }
                        dt -= fixedStep;
                    }
                }
            };
            firstTick = true;
        }
        isAnimationRunning = true;
        animationTimer.start();
    }

    public void restart() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
        }
        isAnimationRunning = true;
        start();
    }

    public void pause() {
        if (animationTimer != null)
            animationTimer.stop();
        isAnimationRunning = false;
        firstTick = true;
    }

    public void pauseUnpause() {
        if (isAnimationRunning) {
            pause();
        } else {
            start();
        }
    }
}
