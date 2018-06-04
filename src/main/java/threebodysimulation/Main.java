package threebodysimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import threebodysimulation.controller.Controller;

public class Main extends Application {
    static Image icon = new Image("iconPhysics.png");

    public static Image getIcon(){ return icon; }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/view_main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, controller.simulationController.getPauseUnpauseEventHandler(KeyCode.P));
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, controller.simulationController.getRestartEventHandler(KeyCode.R));
        primaryStage.setTitle("Three-body problem simulation");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
