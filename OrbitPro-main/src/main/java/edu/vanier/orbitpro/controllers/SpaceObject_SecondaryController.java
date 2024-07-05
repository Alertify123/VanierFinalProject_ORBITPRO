/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package edu.vanier.orbitpro.controllers;

import edu.vanier.orbitpro.controllers.Space_ObjectFXMLController;

import java.net.URL;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.vanier.orbitpro.MainApp;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import edu.vanier.orbitpro.controllers.helpers.PlanetSettings;
import edu.vanier.orbitpro.controllers.helpers.SpaceObjectPhysics;
import edu.vanier.orbitpro.MainApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author ChrisJ
 */
public class SpaceObject_SecondaryController implements Initializable {

    private final static Logger logger = LoggerFactory.getLogger(SpaceObject_SecondaryController.class);

    @FXML
    private Button backButton;
    @FXML
    private Group videoGroup;
    @FXML
    private AnchorPane infoAnchorPane;
    @FXML
    private VBox videoVBox;
    @FXML
    private VBox backVBox;
    @FXML
    private MediaView videoMediaView;
    @FXML
    private Label planetInfoLabel;

    private ImageView background;
    private MediaPlayer videoPlayer;

    private PlanetSettings planetSelection = new PlanetSettings();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String selectedPlanetName = PlanetSettings.getSelectedPlanetName();

        backButton.setOnAction(this::handleBackButtonClick);

        background = new ImageView(new Image(getClass().getResource("/images/part2/spaceBackground.jpg").toString()));
        background.fitWidthProperty().bind(infoAnchorPane.widthProperty());
        background.fitHeightProperty().bind(infoAnchorPane.heightProperty());

        infoAnchorPane.getChildren().add(0, background);

        if (selectedPlanetName != null) {

            planetSelection.setPlanet(selectedPlanetName);

            double radius = planetSelection.getRadius();
            double mass = planetSelection.getMass();
            String facts = planetSelection.facts();

            planetInfoLabel.setText(String.format(
                    "PLANET: %s \nRADIUS: %.2f km \n MASS: %.2f kg \nFACTS: %s",
                    selectedPlanetName,
                    radius,
                    mass,
                    facts
            ));
            
            planetInfoLabel.setAlignment(Pos.CENTER);

        }

        String videoFile = switch (selectedPlanetName) {
            case "Mercury" ->
                "/images/part2/mercury.mp4";
            case "Moon" ->
                "/images/part2/moon.mp4";
            case "Sun" ->
                "/images/part2/sun.mp4";
            case "Earth" ->
                "/images/part2/earth.mp4";
            case "Mars" ->
                "/images/part2/mars.mp4";
            case "Venus" ->
                "/images/part2/venus.mp4";
            case "Jupiter" ->
                "/images/part2/jupiter.mp4";
            case "Uranus" ->
                "/images/part2/uranus.mp4";
            case "Saturn" ->
                "/images/part2/saturn.mp4";
            case "Neptune" ->
                "/images/part2/neptune.mp4";
            default ->
                "/images/part2/earth.mp4";
        };

        Media media = new Media(getClass().getResource(videoFile).toExternalForm());

        videoPlayer = new MediaPlayer(media);

        videoPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        videoMediaView.setMediaPlayer(videoPlayer);

        videoPlayer.play();

    }

    private void handleBackButtonClick(ActionEvent event) {
        loadPrimaryScene(event);

    }

    private void loadPrimaryScene(ActionEvent event) {
        MainApp.switchSceneSim2(MainApp.TERTIARY_LAYOUT, new Space_ObjectFXMLController());

    }

}
