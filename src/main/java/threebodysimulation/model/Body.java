package threebodysimulation.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Body {
    private ObjectProperty<Point2D> position = new SimpleObjectProperty<>(this, "position");
    public ObjectProperty<Point2D> positionProperty() { return position; }
    public Point2D getPosition() { return position.get(); }
    public void setPosition(Point2D newValue) { position.setValue(newValue); }

    private ObjectProperty<Point2D> velocity = new SimpleObjectProperty<>(this, "velocity");
    public ObjectProperty<Point2D> velocityProperty() { return velocity; }
    public Point2D getVelocity() { return velocity.get(); }
    public void setVelocity(Point2D newValue) { velocity.setValue(newValue); }

    private DoubleProperty mass = new SimpleDoubleProperty(this, "mass");
    public DoubleProperty massProperty() { return mass; }
    public double getMass() { return mass.get(); }
    public void setMass(double newValue) { mass.setValue(newValue); }

    public Body() {
        this(new Point2D(0, 0), new Point2D(0, 0), 2e15);
    }

    public Body(Point2D position, Point2D velocity, double mass) {
        setPosition(position);
        setVelocity(velocity);
        setMass(mass);
    }

    @Override
    public String toString() {
        return String.format("Body[mass = %f; position = %s; velocity = %s", getMass(), getPosition(), getVelocity());
    }
}
