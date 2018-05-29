package threebodysimulation.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class Body implements Cloneable, Serializable {
    private static final double DEFAULT_MASS = 2e15;

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
        this(new Point2D(0, 0), new Point2D(0, 0), DEFAULT_MASS);
    }

    public Body(Point2D position, Point2D velocity) {
        this(position, velocity, DEFAULT_MASS);
    }

    public Body(Point2D position, Point2D velocity, double mass) {
        setPosition(position);
        setVelocity(velocity);
        setMass(mass);
    }

    public Body(double positionX, double positionY, double velocityX, double velocityY) {
        this(positionX, positionY, velocityX, velocityY, DEFAULT_MASS);
    }

    public Body(double positionX, double positionY, double velocityX, double velocityY, double mass) {
        this(new Point2D(positionX, positionY), new Point2D(velocityX, velocityY), mass);
    }

    @Override
    public String toString() {
        return String.format("Body[mass = %f; position = %s; velocity = %s", getMass(), getPosition(), getVelocity());
    }

    @Override
    public Body clone() {
        return new Body(this.getPosition(), this.getVelocity(), this.getMass());
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        Point2D point = getPosition();
        out.writeDouble(point.getX());
        out.writeDouble(point.getY());
        point = getVelocity();
        out.writeDouble(point.getX());
        out.writeDouble(point.getY());
        out.writeDouble(getMass());
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        position = new SimpleObjectProperty<>(this, "position");
        setPosition(new Point2D(in.readDouble(), in.readDouble()));
        velocity = new SimpleObjectProperty<>(this, "velocity");
        setVelocity(new Point2D(in.readDouble(), in.readDouble()));
        mass = new SimpleDoubleProperty(this, "mass");
        setMass(in.readDouble());
    }

    private void readObjectNoData() throws ObjectStreamException {
        position = new SimpleObjectProperty<>(this, "position");
        velocity = new SimpleObjectProperty<>(this, "velocity");
        mass = new SimpleDoubleProperty(this, "mass");
    }
}
