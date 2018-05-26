package threebodysimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.err.println(getClass().getResource("."));
        System.err.println(getClass().getClassLoader().getResource("."));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/view_main.fxml"));
        primaryStage.setTitle("Three-body simulation");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
