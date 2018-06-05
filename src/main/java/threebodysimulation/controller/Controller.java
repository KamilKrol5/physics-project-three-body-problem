package threebodysimulation.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import threebodysimulation.Main;
import threebodysimulation.model.Body;
import threebodysimulation.model.Simulation;

import java.util.*;

public class Controller {
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
    public MenuItem exitButton;
    public MenuItem setDefaultPreferencesButton;
    public MenuItem aboutButton;
    public BorderPane rootBorderPane;
    public HBox addBodyButtonBox;
    private Background settingsMenusBackground = new Background(new BackgroundFill(Color.web("#7487FA"), null, null));
    private Background defaultSimulationPaneBackground = new Background(new BackgroundFill(Color.web("#2457AA"), null, null));
    private Map<String, SimulationController.Preset> presets = new HashMap<>();
    public SimulationController simulationController;
    private String currentPresetName = "default";
    private static final String DEFAULT_NAME = "name";

    public void initialize() {
        startButton.requestFocus();
        settingsMenu.setBackground(settingsMenusBackground);
        moreSettingsMenu.setBackground(settingsMenusBackground);
        //???
        settingsMenuSubPanel.prefHeightProperty().bind(settingsMenu.heightProperty());
        //---presets---
        SimulationController.Preset defaultPreset = defaultPreset();
        presetsBox.getItems().add("New preset...");
        presets.put("default", defaultPreset);
        addPresets();

        //---presets combo-box handler
        presetsBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentPresetName = newValue;
            if (currentPresetName.equals("New preset...")) {
                showCreatingNewPresetDialog(oldValue);
            } else {
                loadPreset(presets.get(currentPresetName));
                //simulationController.loadPreset(presets.get(currentPresetName));
                System.out.println(currentPresetName);
            }
        });
        ///
        //addNewBodyToPanel("Middle planet", 400, 20, 40, 200, 0.11);
        //addNewBodyToPanel("Small planet", 100, 100, 200, 100, -8);
        //addNewBodyToPanel("Big planet", 500, 300, 110, 300, -2);
        speedLabel.textProperty().bind(speedSlider.valueProperty().asString("Speed: %.0f%%"));
        //---create SimulationView and SimulationController
        simulationController = new SimulationController(defaultPreset);
        rootBorderPane.setCenter(simulationController.getSimulationView());
        simulationPane = simulationController.getSimulationView();
        simulationController.getCurrentSimulation().timeScaleProperty().bind(speedSlider.valueProperty().divide(100));

        presetsBox.setValue("default");
        presetsBox.disableProperty().bind(simulationController.getCurrentSimulation().animationRunningProperty());
        moreSettingsMenuPanelWithBodies.disableProperty().bind(simulationController.getCurrentSimulation().animationRunningProperty().or(needPresetReload));
        addBodyButtonBox.disableProperty().bind(simulationController.getCurrentSimulation().animationRunningProperty().or(needPresetReload));
        setDefaultPreferences();
    }

    private void addPresets() {
        Body hexagonBody1 = new Body(200, 0, 40*Math.cos(Math.toRadians(-45)), 40*Math.sin(Math.toRadians(-45)), 4.0e15);
        Body hexagonBody2 = new Body(200, 400, 40*Math.cos(Math.toRadians(135)), 40*Math.sin(Math.toRadians(135)), 4.0e15);
        //Body body3 = new Body(212, 170, 2, 111, 5e16);
        SimulationController.Preset hexagon = defaultPreset();
        hexagon.getBodies().addAll(hexagonBody1,hexagonBody2);
        //SimulationController.Preset randomPreset = new SimulationController.Preset(Simulation.DEFAULT_GRAVITATIONAL_CONSTANT, 1, randomBodies(3));
        presetsBox.getItems().add("hexagon");
        presets.put("hexagon", hexagon);
        //
        Body twoBodiesBody1 = new Body(550, 300, 10*Math.cos(Math.toRadians(140)), 10*Math.sin(Math.toRadians(140)), 5.0e16);
        Body twoBodiesBody2 = new Body(350, 100, 100*Math.cos(Math.toRadians(-40)), 100*Math.sin(Math.toRadians(-40)), 5.0e15);
        SimulationController.Preset twoBodies = new SimulationController.Preset(twoBodiesBody1,twoBodiesBody2);
        presetsBox.getItems().add("2 bodies");
        presets.put("2 bodies", twoBodies);
        //
//        Body almostStableBody1 = new Body(450, 300, 10 * Math.cos(Math.toRadians(140)), 10 * Math.sin(Math.toRadians(140)), 5.0e16);
//        Body almostStableBody2 = new Body(250, 100, 100*Math.cos(Math.toRadians(-40)), 100*Math.sin(Math.toRadians(-40)), 5.0e15);
//        Body almostStableBody3 = new Body(250, 130, 170*Math.cos(Math.toRadians(0)), 170*Math.sin(Math.toRadians(0)), 5.0e10);
//        SimulationController.Preset almostStable = new SimulationController.Preset(almostStableBody1,almostStableBody2,almostStableBody3);
//        presetsBox.getItems().add("almost stable");
//        presets.put("almost stable", almostStable);
        //
        Body changeOrbitBody1 = new Body(550, 300, 10*Math.cos(Math.toRadians(140)), 10*Math.sin(Math.toRadians(140)), 5.0e16);
        Body changeOrbitBody2 = new Body(350, 100, 100*Math.cos(Math.toRadians(-40)), 100*Math.sin(Math.toRadians(-40)), 5.0e15);
        Body changeOrbitBody3 = new Body(350, 130, 175*Math.cos(Math.toRadians(0)), 175*Math.sin(Math.toRadians(0)), 5.0e9);
        SimulationController.Preset changeOrbit = new SimulationController.Preset(changeOrbitBody1,changeOrbitBody2,changeOrbitBody3);
        presetsBox.getItems().add("change orbit");
        presets.put("change orbit", changeOrbit);
        //
//        Body cometBody1 = new Body(450, 400, 10*Math.cos(Math.toRadians(140)), 10*Math.sin(Math.toRadians(140)), 5.0e16);
//        Body cometBody2 = new Body(250, 200, 100*Math.cos(Math.toRadians(-40)), 100*Math.sin(Math.toRadians(-40)), 5.0e15);
//        Body cometBody3 = new Body(250, 230, 175*Math.cos(Math.toRadians(0)), 175*Math.sin(Math.toRadians(0)), 5.0e9);
//        Body cometBody4 = new Body(50, 95, 4*Math.cos(Math.toRadians(1)), 4*Math.sin(Math.toRadians(1)), 5.0e15);
//        SimulationController.Preset comet = new SimulationController.Preset(cometBody1,cometBody2,cometBody3,cometBody4);
//        presetsBox.getItems().add("comet");
//        presets.put("comet", comet);
//        //
//        Body negativeMass3Body1 = new Body(450, 350, 10*Math.cos(Math.toRadians(140)), 10*Math.sin(Math.toRadians(140)), 5.0e16);
//        Body negativeMass3Body2 = new Body(250, 150, 100*Math.cos(Math.toRadians(-40)), 100*Math.sin(Math.toRadians(-40)), 5.0e15);
//        Body negativeMass3Body3 = new Body(250, 180, 175*Math.cos(Math.toRadians(0)), 175*Math.sin(Math.toRadians(0)), 5.0e9);
//        Body negativeMass3Body4 = new Body(50, 50, 100*Math.cos(Math.toRadians(90)), 100*Math.sin(Math.toRadians(90)), 5.0e8);
//        SimulationController.Preset negativeMass3 = new SimulationController.Preset(negativeMass3Body1,negativeMass3Body2,negativeMass3Body3,negativeMass3Body4);
//        presetsBox.getItems().add("3");
//        presets.put("3", negativeMass3);
        //
        //presetsBox.getItems().addAll(presets.keySet());
        //random
        addRandomPreset(3,"3 random");
        addRandomPreset(5,"5 random");
    }

    private Collection<Body> randomBodies(int amount) {
        Random random = new Random();
        ArrayList<Body> bodies = new ArrayList<>(amount);
        for(int i=0;i<amount;i++){
            bodies.add(new Body(random.nextInt(700),random.nextInt(600),random.nextDouble(),random.nextDouble(),4e15));
        }
        return bodies;
    }

    public void moreOnAction() {
        if (!moreSettingsMenu.isVisible()) {
            moreSettingsMenu.setVisible(true);
            moreSettingsButton.setText("<<< LESS");
        } else {
            moreSettingsMenu.setVisible(false);
            moreSettingsButton.setText("MORE >>>");
        }
        if (!((Stage) moreSettingsMenu.getScene().getWindow()).isMaximized()) {
            moreSettingsMenu.getScene().getWindow().sizeToScene();
        }

    }

    public void changeBackground() {
        final Alert alert = new Alert(Alert.AlertType.NONE);
        final FlowPane pickerContainer = new FlowPane(20, 20);
        final ColorPicker colorPicker = new ColorPicker();
        final Circle colorCircle = new Circle(50);
        colorPicker.setValue((Color) simulationPane.getBackground().getFills().get(0).getFill());
        alert.setTitle("Setting background");
        alert.setHeaderText("Pick new background color");
        colorCircle.fillProperty().bind(colorPicker.valueProperty());
        pickerContainer.getChildren().addAll(colorCircle, colorPicker);
        alert.getDialogPane().setContent(pickerContainer);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(threebodysimulation.Main.getIcon());
        alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> optional = alert.showAndWait();
        if (optional.isPresent() && optional.get().equals(ButtonType.OK)) {
            Background newSimulationPaneBackground = new Background(new BackgroundFill(colorPicker.getValue(), null, null));
            simulationPane.setBackground(newSimulationPaneBackground);
        }
    }

    public void closeApplication() {
        Platform.exit();
    }

    public void setDefaultPreferences() {
        simulationPane.setBackground(defaultSimulationPaneBackground);
    }

    public void addRandomPreset(int amountOfBodies, String nameOfPreset){
        SimulationController.Preset randomPreset = new SimulationController.Preset(Simulation.DEFAULT_GRAVITATIONAL_CONSTANT, 1, randomBodies(amountOfBodies));
        presets.put(nameOfPreset, randomPreset);
        presetsBox.getItems().add(nameOfPreset);
    }

    public void showAboutWindow() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("About");
        Label contentText = new Label();
        contentText.setTextFill(Color.WHITE);
        contentText.setTextAlignment(TextAlignment.CENTER);
        contentText.setText("\nThree-body problem simulation.\n\n" +
                "Authors:\nJakub Dąbek\nKamil Król\n\n" +
                "Version:\n1.1\n");
        //contentText.setAlignment(Pos.BASELINE_CENTER);
        VBox dialogPane = new VBox();
        dialogPane.setPrefSize(400, 200);
        dialogPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        alert.getDialogPane().setBackground(new Background(new BackgroundImage(new Image("milky_way_free.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        dialogPane.setAlignment(Pos.TOP_CENTER);
        dialogPane.getChildren().addAll(contentText);
        alert.getDialogPane().setContent(dialogPane);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(threebodysimulation.Main.getIcon());
        alert.getButtonTypes().addAll(ButtonType.OK);
        alert.show();

    }

    private void showCreatingNewPresetDialog(String oldValue) {
        final Dialog<String> dialog = new TextInputDialog();
        dialog.setTitle("Creating new preset");
        dialog.getDialogPane().setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Type name of new preset");
        ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(Main.getIcon());
        //dialog.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Optional<String> optional = dialog.showAndWait();
        if (optional.isPresent()) {
            if (optional.get().isEmpty()) {
                showErrorAlert("Error", "Name of the preset cannot be empty.");
                presetsBox.setValue(oldValue);
            } else {
                presetsBox.getItems().add(optional.get());
                presets.put(optional.get(), new SimulationController.Preset(randomBodies(3)));
                //presets.put(optional.get(), defaultPreset());
                presetsBox.setValue(presetsBox.getItems().get(presetsBox.getItems().size() - 1));
            }
        } else {
            presetsBox.setValue(oldValue);
        }
    }

    private void showErrorAlert(String title, String message) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        ((Stage) errorAlert.getDialogPane().getScene().getWindow()).getIcons().add(Main.getIcon());
        errorAlert.setTitle(title);
        errorAlert.setContentText(message);
        errorAlert.setHeaderText(null);
        errorAlert.show();
    }

    private void loadPreset(SimulationController.Preset preset) {
        needPresetReload.set(false);
        simulationController.loadPreset(preset);
        clearPanel();
        for (Body body : preset.getBodies()) {
            addNewBodyToPanel(body);
        }
    }

    private void clearPanel() {
        moreSettingsMenuPanelWithBodies.getChildren().clear();
    }

    private void addNewBodyToPanel(Body newBody) {
        addNewBodyToPanel(newBody, DEFAULT_NAME);
    }

    private void addNewBodyToPanel(Body body, String name) {
        //presets.get(currentPresetName).bodies.add(body);
        //add body to list of bodies
        VBox bodyInfoVBox = new VBox();
        bodyInfoVBox.setSpacing(2);
        bodyInfoVBox.setAlignment(Pos.TOP_CENTER);
        bodyInfoVBox.prefWidthProperty().bind(moreSettingsMenuPanelWithBodies.widthProperty());
        TextField nameField = new TextField();
        nameField.setStyle("-fx-background-color: #2457AA; -fx-text-fill: #FDFDFE; -fx-font-weight: bold;");
        nameField.setText(name);
        Button deleteBodyButton = new Button();
        deleteBodyButton.setText("Delete this body");
        deleteBodyButton.setOnAction((event) -> {
            moreSettingsMenuPanelWithBodies.getChildren().remove(bodyInfoVBox);
            presets.get(currentPresetName).bodies.remove(body);
            deleteBody();
        });
        //---mass---
        Label massLabel = new Label("Mass: ");
        TextField massField = new TextField();
        TextFormatter<Double> textFormatter = new TextFormatter<>(new DoubleStringConverter());
        massField.setTextFormatter(textFormatter);
        body.massProperty().unbind();
        textFormatter.setValue(body.getMass());

        body.massProperty().bind(Bindings.createDoubleBinding(() -> {
            Double value = textFormatter.valueProperty().getValue();
            //System.err.println(value);
            return value;
        }, textFormatter.valueProperty()));
        textFormatter.valueProperty().addListener((observable, oldValue, newValue) -> {
            //System.err.format("%s %s %s\n", oldValue, newValue, body.getMass());
        });

        //---position label---
        Label positionLabel = new Label("Position: ");
        HBox coordinatesHBox = new HBox();
        coordinatesHBox.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        coordinatesHBox.setAlignment(Pos.BASELINE_RIGHT);
        coordinatesHBox.setSpacing(4);

        //---velocity value---
        Label labelVelocityValue = new Label("Velocity value: ");
        TextField velocityValueField = new TextField();
        //velocityValueField.setText(Integer.toString(velocityValue));
        velocityValueField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(2.5));

        TextFormatter<Double> velocityValueTextFormatter = new TextFormatter<>(new DoubleStringConverter() {
            @Override
            public String toString(Double value) {
                return String.format("%.0f", value);
            }
        });
        velocityValueField.setTextFormatter(velocityValueTextFormatter);
        velocityValueTextFormatter.setValue(body.getVelocity().magnitude());

        HBox velocityValueBox = new HBox();
        velocityValueBox.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        velocityValueBox.setAlignment(Pos.BASELINE_RIGHT);
        velocityValueBox.setSpacing(4);
        velocityValueBox.getChildren().addAll(labelVelocityValue, velocityValueField);


        //---velocity angle---
        Label velocityAngleLabel = new Label("Velocity angle: ");
        TextField velocityAngleField = new TextField();
        //velocityAngleField.setText(Double.toString(velocityAngle));
        velocityAngleField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(2.5));

        TextFormatter<Double> velocityAngleTextFormatter = new TextFormatter<>(new DoubleStringConverter() {
            @Override
            public String toString(Double value) {
                return String.format("%.0f", value);
            }
        });
        velocityAngleField.setTextFormatter(velocityAngleTextFormatter);
        Point2D velocity = body.getVelocity();
        velocityAngleTextFormatter.setValue(Math.toDegrees(Math.atan2(velocity.getY(), velocity.getX())));

        //Label degLabel = new Label("deg.");
        HBox velocityAngleBox = new HBox();
        velocityAngleBox.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        velocityAngleBox.setAlignment(Pos.BASELINE_RIGHT);
        velocityAngleBox.setSpacing(4);
        velocityAngleBox.getChildren().addAll(velocityAngleLabel, velocityAngleField);


        body.velocityProperty().bind(Bindings.createObjectBinding(() ->
                        new Point2D(
                                velocityValueTextFormatter.getValue() * Math.cos(Math.toRadians(velocityAngleTextFormatter.getValue())),
                                velocityValueTextFormatter.getValue() * Math.sin(Math.toRadians(velocityAngleTextFormatter.getValue()))),
                velocityValueTextFormatter.valueProperty(), velocityAngleTextFormatter.valueProperty()
        ));


        //---
//        Label velocityXLabel = new Label("velocity X:");
//        TextField velocityXField = new TextField();
//        velocityXField.setText(Integer.toString(velocityX));
//        velocityXField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(2));
//        Label velocityYLabel = new Label("velocity Y:");
//        TextField velocityYField = new TextField();
//        velocityYField.setText(Integer.toString(velocityY));
//        velocityYField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(2));
//        ---decided to replace it byb angle
//        HBox velocityXBox = new HBox();
//        velocityXBox.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
//        velocityXBox.setAlignment(Pos.BASELINE_RIGHT);
//        velocityXBox.setSpacing(4);
//        HBox velocityYBox = new HBox();
//        velocityYBox.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
//        velocityYBox.setAlignment(Pos.BASELINE_RIGHT);
//        velocityYBox.setSpacing(4);
        //---
        Label labelX = new Label("X:");
        TextField xField = new TextField();
        //xField.setText(Integer.toString(x));
        xField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(3));
        TextFormatter<Double> xFieldTextFormatter = new TextFormatter<>(new DoubleStringConverter() {
            @Override
            public String toString(Double value) {
                return String.format("%.0f", value);
            }
        });
        xField.setTextFormatter(xFieldTextFormatter);
        xFieldTextFormatter.setValue(body.getPosition().getX());

        Label labelY = new Label("Y:");
        TextField yField = new TextField();
        //yField.setText(Integer.toString(y));
        yField.prefWidthProperty().bind(bodyInfoVBox.widthProperty().divide(3));
        TextFormatter<Double> yFieldTextFormatter = new TextFormatter<>(new DoubleStringConverter() {
            @Override
            public String toString(Double value) {
                return String.format("%.0f", value);
            }
        });
        yField.setTextFormatter(yFieldTextFormatter);
        yFieldTextFormatter.setValue(body.getPosition().getY());

        Separator separator = new Separator();
        separator.prefWidthProperty().bind(bodyInfoVBox.widthProperty());
        separator.setPadding(new Insets(7, 0, 7, 0));

        body.positionProperty().bind(Bindings.createObjectBinding(() ->
                        new Point2D(xFieldTextFormatter.getValue(), yFieldTextFormatter.getValue()),
                xFieldTextFormatter.valueProperty(), yFieldTextFormatter.valueProperty()
        ));
        //---
//        velocityXBox.getChildren().addAll(velocityXLabel, velocityXField);
//        velocityYBox.getChildren().addAll(velocityYLabel, velocityYField);
        coordinatesHBox.getChildren().addAll(labelX, xField, labelY, yField);
        bodyInfoVBox.getChildren().addAll(
                nameField,
                massLabel,
                massField,
                positionLabel,
                coordinatesHBox,
                velocityValueBox,
                velocityAngleBox,
                //velocityXBox,
                //velocityYBox,
                deleteBodyButton,
                separator);
        moreSettingsMenuPanelWithBodies.getChildren().add(bodyInfoVBox);
    }

    private void deleteBodyFromPanel() {
    }

    public void addNewBody(ActionEvent event) {
        Random random = new Random();
        Body body = new Body(random.nextInt(500),random.nextInt(500),random.nextDouble()*5,random.nextDouble()*5,5e15);
        presets.get(currentPresetName).getBodies().add(body);
        addNewBodyToPanel(body);
    }

    public void deleteBody() {
        deleteBodyFromPanel();
    }

    private static SimulationController.Preset defaultPreset() {
        final double vel = 1e1;
        final Point2D offset = new Point2D(100, 100);
        return new SimulationController.Preset(
                new Body(new Point2D(0, 0).add(offset), new Point2D(vel, -vel)),
                new Body(new Point2D(200, 0).add(offset), new Point2D(vel, vel)),
                new Body(new Point2D(200, 200).add(offset), new Point2D(-vel, vel)),
                new Body(new Point2D(0, 200).add(offset), new Point2D(-vel, -vel))
        );
    }

    public void stopSimulation() {
        startButton.setText("START");
        loadPreset(presets.get(currentPresetName));
    }

    private SimpleBooleanProperty needPresetReload = new SimpleBooleanProperty(this, "needPresetReload", false);

    public void startPauseSimulation() {
        if (simulationController.getCurrentSimulation().isAnimationRunning()) {
            startButton.setText("START");
            simulationController.getCurrentSimulation().pause();
        } else {
            startButton.setText("PAUSE");
            if(!needPresetReload.get()) {
                loadPreset(presets.get(currentPresetName));
            }
            needPresetReload.set(true);
            simulationController.start();
        }
    }

    public void showInfo(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("About");
        Label contentText = new Label();
        contentText.setTextFill(Color.BLACK);
        contentText.setText("\nManual: \n\n" +
                "Click 'start' to start simulation on default preset.\n" +
                "Preset can be changed in box on menu panel.\n" +
                "There are some presets given from authors but also users can create \n" +
                "their own presets by clicking 'New preset...'. New presets can be edited\n" +
                "in the panel on the right. In this panel existing bodies can be edited\n" +
                "or deleted and new bodies can be added.\n\n" +
                "Hints:\n" +
                " ■ The velocity angle is in degrees not in radians.\n"+
                " ■ For better looking effects it may be needed to set a huge mass.\n" +
                " ■ User can use scientific notation.\n" +
                " ■ Bodies added by user have random values.\n" +
                " ■ Random presets are different for every launching of the program.\n" +
                " ■ Presets can be changed only when simulation is paused or stopped.\n" +
                " ■ Every body in current preset can be edited on the right panel\n" +
                "only after stopping simulation. All typed changes will be seen\n" +
                "after starting simulation.\n" +
                " ■ Negative mass value is allowed. There is a preset showing that case.\n" +
                " ■ If bodies get too close the force is so big that their velocity \n" +
                "increases significantly. It may cause an effect of disappearing bodies.\n");
        //🚽⚤☭●◉■💊🚑★☢★✦
        Pane dialogPane = new Pane();
        dialogPane.setPrefSize(405, 400);
        //dialogPane.setBackground(new Background(new BackgroundFill(Color.web("#7487FA"), null, null)));
        alert.getDialogPane().setBackground(new Background(new BackgroundFill(Color.web("#7487FA"), null, null)));
        dialogPane.getChildren().addAll(contentText);
        alert.getDialogPane().setContent(dialogPane);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(threebodysimulation.Main.getIcon());
        alert.getButtonTypes().addAll(ButtonType.OK);
        alert.show();
    }
}
