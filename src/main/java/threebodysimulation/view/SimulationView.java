package threebodysimulation.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class SimulationView extends Pane {

    public SimulationView() {
        super();
        setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        setClip(clip);
    }

    public SimulationView(int numberOfBodies) {
        this();
        reset(numberOfBodies);
    }

    public void reset(int numberOfBodies) {
        getChildren().clear();
        for (int i = 0; i < numberOfBodies; i++) {
            getChildren().add(new Circle(10, Color.RED));
        }
    }

    public void bindBodiesPositions(Iterable<ObjectProperty<Point2D>> positions) {
        int index = 0;
        for (ObjectProperty<Point2D> property : positions) {
            final Circle current = (Circle) getChildren().get(index++);
            current.layoutXProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> property.get().getX() - current.getLayoutBounds().getMinX(),
                            property, current.layoutBoundsProperty()));

            current.layoutYProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> property.get().getY() - current.getLayoutBounds().getMinY(),
                            property, current.layoutBoundsProperty()));
        }
    }
}
