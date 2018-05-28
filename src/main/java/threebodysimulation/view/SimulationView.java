package threebodysimulation.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Iterator;

public class SimulationView extends Pane {

    public SimulationView(int numberOfBodies) {
        setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        for (int i = 0; i < numberOfBodies; i++) {
            getChildren().add(new Circle(50, Color.RED));
        }
    }

    public void bindBodiesPositions(Iterable<ObjectProperty<Point2D>> positions) {
        int index = 0;
        for (ObjectProperty<Point2D> property : positions) {
            Circle current = (Circle)getChildren().get(index++);
            current.layoutXProperty().bind(Bindings.createDoubleBinding(() -> property.get().getX(), property));
            current.layoutYProperty().bind(Bindings.createDoubleBinding(() -> property.get().getY(), property));
        }
    }
}
