/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.orbitpro.controllers;

import edu.vanier.orbitpro.MainApp;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.lang.System;
import java.util.concurrent.TimeUnit;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * This class controls the functionality of the Escape Velocity GUI. It handles
 * user interactions, such as selecting a planet, adjusting initial velocity,
 * and starting the animation.
 *
 * @author bidid
 */
public class EscapeVelocityFXMLController {

    @FXML
    private ComboBox<String> planetComboBox;

    @FXML
    private HBox actionAndAboutHBox;

    @FXML
    private Label aboutLabel;

    @FXML
    private Label radiusLabel;

    @FXML
    private Label massLabel;

    @FXML
    private Label initialVelocityLabel;

    @FXML
    private Slider velocitySlider;

    @FXML
    private Button startButton;

    @FXML
    private Button returnHome;

    @FXML
    private Button goNext;

    @FXML
    private Label velocityLabel;

    @FXML
    private Label heightLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Pane centerPane;

    private boolean isAnAnimationRunning = false;
    private PhysicsEscapeVelocityEngine physicsEngine;
    private AnimationTimer animationTimer;
    private double initialVelocity;

    /**
     * Initializes the controller, setting up the planet selection dropdown, the
     * velocity slider, and other GUI elements.
     */
    @FXML
    public void initialize() {
        planetComboBox.getItems().addAll("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune");

        velocitySlider.setShowTickLabels(true);
        velocitySlider.setShowTickMarks(true);
        velocitySlider.setMajorTickUnit(100);
        velocitySlider.setMinorTickCount(10);

        velocitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            handleVelocitySlider();
        });

        actionAndAboutHBox.setSpacing(10);

        aboutLabel.setOnMouseClicked((MouseEvent e) -> {
            aboutWindow aboutWindow = new aboutWindow();
            aboutWindow.display();
        });

        physicsEngine = new PhysicsEscapeVelocityEngine();

        startButton.setDisable(true);
        velocitySlider.setDisable(false);

        returnHome.setOnAction(this::handleReturnHome);
        goNext.setOnAction(this::handleGoNext);
    }

    /**
     * Handles the action of returning to the home screen.
     *
     * @param event The ActionEvent triggered by the return home button.
     */
    @FXML
    private void handleReturnHome(ActionEvent event) {
        MainApp.switchSceneHome(MainApp.MAINAPP_LAYOUT, new FXMLMainAppController());
    }

        /**
     * Handles the action of going to the next simulation.
     *
     * @param event The ActionEvent triggered by the go next button.
     */
    @FXML
    private void handleGoNext(ActionEvent event) {
        MainApp.switchSceneSim2(MainApp.TERTIARY_LAYOUT, new Space_ObjectFXMLController());
    }
    
    /**
     * Handles the action of selecting a planet from the drop-down menu of the
     * combobox which will then use the string value to set the planet in the
     * physics engine where all of its properties are stored in the
     * setPlanet(String planet) method and also allows for the escape velocity
     * to be calculated, in addition it updates informative labels regarding the
     * radius and mass of the planet (with formatting).
     *
     * @param event The ActionEvent triggered by selecting a planet from
     * combobox.
     */
    @FXML
    private void handlePlanetSelection(ActionEvent event) {
        String selectedPlanet = planetComboBox.getValue();
        physicsEngine.setPlanet(selectedPlanet);
        double radius = physicsEngine.getRadius();
        double mass = physicsEngine.getMass();

        radiusLabel.setText(String.format("%.2f", radius) + " km");
        massLabel.setText(String.format("%.2e", mass) + " kg");
        startButton.setDisable(false);
    }

    /**
     * Handles the action of starting the animation and also clears the pane of
     * any elements of the previous motion (clears pane of clutter).
     *
     * @param event The ActionEvent triggered by clicking the start button.
     */
    @FXML
    private void handleStartButton(ActionEvent event) {
        centerPane.getChildren().removeIf(node -> node instanceof Circle);
        centerPane.getChildren().removeIf(node -> node instanceof Rectangle);
        centerPane.getChildren().removeIf(node -> node instanceof Text);

        Animation();
    }

    /**
     * Handles changes to the velocity slider from values 0.1 to 100 to be used
     * as the initial launch velocity in kilometers/second for the projectile
     * and kinematics computations.
     *
     */
    @FXML
    private void handleVelocitySlider() {
        initialVelocity = velocitySlider.getValue();
        System.out.println("SliderInitialVelocity = " + initialVelocity);
        initialVelocityLabel.setText(String.format("%.2f %s ", initialVelocity, " km/s"));
    }

    /**
     * This method sets up and starts the animation of the projectile motion. It
     * creates the necessary graphical elements, such as the ball representing
     * the projectile and the bottom rectangle representing the selected planet.
     *
     * The position and size of these elements are dynamically adjusted to the
     * dimensions of the center pane, making them resizable. The animation is
     * driven by an AnimationTimer, which continuously updates the position of
     * the ball based on the kinematics equations of motion.
     *
     * Firstly, it uses the equation Vf = Vi + at to calculate the max time of
     * the projectile's motion according to the escape velocity as Vi and
     * knowing that Vf is zero (since that is where it would peak and start
     * falling back down theoretically) for any selected planet.
     *
     * This serves as the basis for the second computation which aims to find
     * the peak height of each planet's respective escape velocity using the
     * equation of displacement = Vit + 0.5at^2.
     *
     * This displacement value is then used as an anchor for the scaling since
     * regardless of selected velocity and planet the top of the centerPane will
     * always be relative to maxD (peak height) and therefore we use it as a
     * scaling factor (each planet has its own since they all have different
     * escape velocities).
     *
     * The AnimationTimer's handle method is responsible for updating the
     * position of the ball according to the elapsed time since the animation
     * started. It computes the new position using the projectile motion
     * equations and adjusts the display accordingly.
     *
     * Additionally, it updates the height and time labels to reflect the
     * current state of the animation. The animation continues until the
     * projectile either reaches the top of the screen (escape success) or falls
     * back to the bottom (escape fail).
     *
     * Upon reaching either condition, the animation stops, and the appropriate
     * text indicating escape success or failure is displayed in the center of
     * the pane. While the animation is running, the start button and velocity
     * slider are disabled to prevent user interaction that could create
     * conflict with the animation.
     *
     * Once the animation is complete, these controls are "re-enabled", allowing
     * the user to play around with the parameters of the launch as they please.
     *
     */
    private void Animation() {

        physicsEngine.setPlanet(planetComboBox.getValue());
        Circle ball = new Circle(centerPane.getWidth() / 2, centerPane.getHeight() - 60, 10);
        ball.setFill(Color.WHITE);
        centerPane.getChildren().add(ball);

        Rectangle bottomRectangle = new Rectangle(centerPane.getWidth(), 50);
        bottomRectangle.setFill(physicsEngine.getPlanetColor());
        bottomRectangle.setY(centerPane.getHeight() - 50);
        centerPane.getChildren().add(bottomRectangle);

        double escapeVelocity = physicsEngine.calculateEscapeVelocity();

        System.out.println("escapeVelocity = " + escapeVelocity);

        centerPane.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            double heightRatio = newSceneHeight.doubleValue() / oldSceneHeight.doubleValue();
            double newY = ball.getCenterY() * heightRatio;
            ball.setCenterY(newY);

            bottomRectangle.setY(newSceneHeight.doubleValue() - bottomRectangle.getHeight());

        });

        centerPane.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            bottomRectangle.setWidth(newSceneWidth.doubleValue());
        });

        startButton.setDisable(true);
        velocitySlider.setDisable(true);
        isAnAnimationRunning = true;

        animationTimer = new AnimationTimer() {
            long lastUpdate = 0;

            double maxTime = (physicsEngine.calculateEscapeVelocity()) * 1000 / (physicsEngine.getGravity());
            double maxD = (maxTime * (physicsEngine.calculateEscapeVelocity() * 1000) + ((-1) * physicsEngine.getGravity() * maxTime * maxTime) / 2);
            double scaleY1 = (centerPane.getHeight() - 60 - ball.getRadius()) / maxD;

            @Override
            public void handle(long now) {

                double seconds = (now / 1000000000.0) * 150;

                if (lastUpdate == 0) {
                    lastUpdate = (long) seconds;
                    return;
                }

                double elapsedSeconds = seconds - lastUpdate;

                double newY = +(elapsedSeconds * (initialVelocity * 1000) + ((-1) * physicsEngine.getGravity() * elapsedSeconds * elapsedSeconds) / 2);
                heightLabel.setText(String.format("%.2f %s", newY <= 0 ? 0 : newY, "km"));
                double distance = newY;
                newY = (centerPane.getHeight() - 60 - ball.getRadius()) - newY * scaleY1;

                timeLabel.setText(String.format("%.2f %s", elapsedSeconds, "seconds"));

                if (newY <= 0 || newY >= centerPane.getHeight() - 60) {
                    boolean escapeSuccess = newY <= 0;
                    if (escapeSuccess) {
                        Text escapeText = new Text(centerPane.getWidth() / 2 - 95, (centerPane.getHeight() - 50) / 2, "Escape Success");
                        escapeText.setFill(Color.WHITE);
                        escapeText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                        centerPane.getChildren().add(escapeText);
                    } else {
                        Text escapeText = new Text(centerPane.getWidth() / 2 - 95, (centerPane.getHeight() - 50) / 2, "Escape Failed");
                        escapeText.setFill(Color.WHITE);
                        escapeText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                        centerPane.getChildren().add(escapeText);
                    }
                    startButton.setDisable(false);
                    velocitySlider.setDisable(false);
                    isAnAnimationRunning = false;
                    stop();
                }
                ball.setCenterY(newY);
            }
        };
        animationTimer.start();
    }

}
