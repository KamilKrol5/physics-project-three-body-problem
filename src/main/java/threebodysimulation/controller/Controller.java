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
    public ComboBox<String> presetsBox;
    public VBox moreSettingsMenuPanelWithBodies;
    public Button addBodyButton;
    public Label speedLabel;
    public Slider speedSlider;
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
        presetsBox.getItems().addAll("preset1", "preset2","New preset...");
        addNewBodyToPanel("Middle planet", 400,20,40,200,2,4);
        addNewBodyToPanel("Small planet", 100,100,200,100,-8,9);
        addNewBodyToPanel("Big planet", 500,300,110,300,-2,5);
        speedLabel.textProperty().bind(speedSlider.valueProperty().asString("Speed: %.0f"));
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

    private void addNewBodyToPanel(String name, int mass, int x,int y,int velocityValue, int velocityX, int velocityY){
        //add body to list of bodies
        amountOfBodies++;
        VBox bodyInfoVBox=new VBox();
        bodyInfoVBox.setSpacing(2);
        bodyInfoVBox.setAlignment(Pos.TOP_CENTER);
        bodyInfoVBox.prefWidthProperty().bind(moreSettingsMenuPanelWithBodies.widthProperty());
        TextField nameField = new TextField();
        nameField.setStyle("-fx-background-color: #2457AA; -fx-text-fill: #FDFDFE; -fx-font-weight: bold;");
        nameField.setText(Integer.toString(amountOfBodies)+". "+name);
        Button deleteBodyButton= new Button();
        deleteBodyButton.setText("Delete this body");
        deleteBodyButton.setOnAction((event)->{moreSettingsMenuPanelWithBodies.getChildren().remove(bodyInfoVBox); deleteBodyFromPanel();});
        //---
        Label labelMass= new Label("Mass: ");
        TextField massField = new TextField();
        massField.setText(Integer.toString(mass));
        Label positonLabel = new Label("Position: ");
        HBox coordinatesHBox=new HBox();
        coordinatesHBox.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        coordinatesHBox.setAlignment(Pos.BASELINE_LEFT);
        coordinatesHBox.setSpacing(4);
        //---
        Label labelVelocityValue = new Label("Velocity value: ");
        TextField velocityValueField = new TextField();
        velocityValueField.setText(Integer.toString(velocityValue));
        Label velocityXLabel = new Label("velocity X:");
        TextField velocityXField = new TextField();
        velocityXField.setText(Integer.toString(velocityX));
        velocityXField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(2));
        Label velocityYLabel = new Label("velocity Y:");
        TextField velocityYField = new TextField();
        velocityYField.setText(Integer.toString(velocityY));
        velocityYField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(2));
        //---
        HBox velocityXBox=new HBox();
        velocityXBox.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        velocityXBox.setAlignment(Pos.BASELINE_RIGHT);
        velocityXBox.setSpacing(4);
        HBox velocityYBox=new HBox();
        velocityYBox.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        velocityYBox.setAlignment(Pos.BASELINE_RIGHT);
        velocityYBox.setSpacing(4);
        //---
        Label labelX = new Label("X:");
        TextField xField = new TextField();
        xField.setText(Integer.toString(x));
        xField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(3));
        Label labelY = new Label("Y:");
        TextField yField = new TextField();
        yField.setText(Integer.toString(y));
        yField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(3));
        Separator separator = new Separator();
        separator.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        separator.setPadding(new Insets(7,0,7,0));
        //---
        velocityXBox.getChildren().addAll(velocityXLabel,velocityXField);
        velocityYBox.getChildren().addAll(velocityYLabel,velocityYField);
        coordinatesHBox.getChildren().addAll(labelX,xField,labelY,yField);
        bodyInfoVBox.getChildren().addAll(
                nameField,
                labelMass,
                massField,
                positonLabel,
                coordinatesHBox,
                labelVelocityValue,
                velocityValueField,
                velocityXBox,
                velocityYBox,
                deleteBodyButton,
                separator);
        moreSettingsMenuPanelWithBodies.getChildren().add(bodyInfoVBox);
    }
    public void deleteBodyFromPanel(){
        amountOfBodies--;
    }
    public void addNewBody(ActionEvent event) {
        addNewBodyToPanel("name",1,10,10,0,0,0);
    }
}
