package threebodysimulation.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Optional;

public class Controller {
    private static int amountOfBodies=0;
    public ScrollPane moreSettingsMenu;
    public Button moreSettingsButton;
    public Pane simulationPane;
    public VBox settingsMenu;
    public Button startButton;
    public Button stopButton;
    public VBox settingsMenuSubPanel;
    public ComboBox presetsBox;
    public VBox moreSettingsMenuPanelWithBodies;
    public Button addBodyButton;
    private Background defaultSimulationPaneBackground= new Background(new BackgroundFill(Color.web("#2457AA"),null,null));
    private Background settingsMenusBackground=new Background(new BackgroundFill(Color.web("#7487FA"),null,null));
    public MenuItem exitButton;
    public MenuItem setDefaultPreferencesButton;
    public MenuItem aboutButton;

    public void initialize(){
        startButton.requestFocus();
        settingsMenu.setBackground(settingsMenusBackground);
        moreSettingsMenu.setBackground(settingsMenusBackground);
        //???
        settingsMenuSubPanel.prefHeightProperty().bind(settingsMenu.heightProperty());
        presetsBox.getItems().addAll(new Label("dupa"), new Label("penis"));
        addNewBodyToPanel("Kutangus", 400,200,2,4);
        addNewBodyToPanel("Sraturn", 100,100,-8,9);
        addNewBodyToPanel("Merkurwy", 500,300,-2,5);

    }
    public void moreOnAction() {
        if(!moreSettingsMenu.isVisible()){
            moreSettingsMenu.setVisible(true);
            moreSettingsButton.setText("<<< LESS");
        }
        else {
            moreSettingsMenu.setVisible(false);
            moreSettingsButton.setText("MORE >>>");
        }
        if(!((Stage)moreSettingsMenu.getScene().getWindow()).isMaximized()){
            moreSettingsMenu.getScene().getWindow().sizeToScene();
        }

    }

    public void changeBackground() {
        final Alert alert= new Alert(Alert.AlertType.NONE);
        final FlowPane pickerContainer = new FlowPane(20, 20);
        final ColorPicker colorPicker=new ColorPicker();
        final Circle colorCircle=new Circle(50);
        colorPicker.setValue((Color)simulationPane.getBackground().getFills().get(0).getFill());
        alert.setTitle("Setting background");
        alert.setHeaderText("Pick new background color");
        colorCircle.fillProperty().bind(colorPicker.valueProperty());
        pickerContainer.getChildren().addAll(colorCircle,colorPicker);
        alert.getDialogPane().setContent(pickerContainer);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(threebodysimulation.Main.getIcon());
        alert.getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        Optional<ButtonType> optional=alert.showAndWait();
        if(optional.isPresent() && optional.get().equals(ButtonType.OK)){
            Background newSimulationPaneBackground= new Background(new BackgroundFill(colorPicker.getValue(),null,null));
            simulationPane.setBackground(newSimulationPaneBackground);
        }
    }

    public void closeApplication() {
        Platform.exit();
    }

    public void setDefaultPreferences() {
        simulationPane.setBackground(defaultSimulationPaneBackground);
    }

    public void showInfo() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("About");
        Label contentText=new Label();
        contentText.setTextFill(Color.WHITE);
        contentText.setText("Three-body problem simulation.\n" +
                "Authors:\nVersion: 1.0\n'some text'");
        Pane dialogPane=new Pane();
        dialogPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        alert.getDialogPane().setBackground(new Background( new BackgroundImage(new Image("/code/milky_way_free.jpg"),BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        dialogPane.getChildren().addAll(contentText);
        alert.getDialogPane().setContent(dialogPane);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(threebodysimulation.Main.getIcon());
        alert.getButtonTypes().addAll(ButtonType.OK);
        alert.show();

    }

    private void addNewBodyToPanel(String name, int mass, int velocityValue, int velocityX, int velocityY){
        amountOfBodies++;
        VBox bodyInfoVBox=new VBox();
        bodyInfoVBox.setSpacing(2);
        bodyInfoVBox.prefWidthProperty().bind(moreSettingsMenuPanelWithBodies.widthProperty());
        TextField nameField = new TextField();
        nameField.setStyle("-fx-background-color: #2457AA; -fx-text-fill: #FDFDFE; -fx-font-weight: bold;");
        nameField.setText(Integer.toString(amountOfBodies)+". "+name);
        Label labelMass= new Label("Mass: ");
        TextField massField = new TextField();
        massField.setText(Integer.toString(mass));
        Label labelVelocityValue = new Label("Velocity value: ");
        TextField velocityValueField = new TextField();
        velocityValueField.setText(Integer.toString(velocityValue));
        HBox velocityVectorCoordinatesHBox=new HBox();
        velocityVectorCoordinatesHBox.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        velocityVectorCoordinatesHBox.setAlignment(Pos.BASELINE_RIGHT);
        velocityVectorCoordinatesHBox.setSpacing(4);
        Label labelX = new Label("X:");
        TextField xField = new TextField();
        xField.setText(Integer.toString(velocityX));
        xField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(3));
        Label labelY = new Label("Y:");
        TextField yField = new TextField();
        yField.setText(Integer.toString(velocityY));
        yField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(3));
        Separator separator = new Separator();
        separator.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        separator.setPadding(new Insets(7,0,7,0));
        velocityVectorCoordinatesHBox.getChildren().addAll(labelX,xField,labelY,yField);
        bodyInfoVBox.getChildren().addAll(nameField,labelMass,massField,labelVelocityValue,velocityValueField,velocityVectorCoordinatesHBox,separator);
        moreSettingsMenuPanelWithBodies.getChildren().add(bodyInfoVBox);
    }

    public void addNewBody(ActionEvent event) {
        addNewBodyToPanel("name",1,0,0,0);
    }
}
