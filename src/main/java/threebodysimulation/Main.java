package threebodysimulation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import threebodysimulation.controller.SimulationController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.err.println(getClass().getResource("."));
        System.err.println(getClass().getClassLoader().getResource("."));
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/view_main.fxml"));
        SimulationController simulationController = new SimulationController();
        Pane root = simulationController.getSimulationView();
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, simulationController.getPauseUnpauseEventHandler(KeyCode.SPACE));
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, simulationController.getRestartEventHandler(KeyCode.R));
        root.setPrefWidth(800);
        root.setPrefHeight(600);
        primaryStage.setTitle("Three-body simulation");
        primaryStage.setScene(new Scene(root));
        simulationController.start();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
