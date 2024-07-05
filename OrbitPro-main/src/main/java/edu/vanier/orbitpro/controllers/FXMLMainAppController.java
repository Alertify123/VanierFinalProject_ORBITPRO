package edu.vanier.orbitpro.controllers;

import javafx.event.ActionEvent;
import edu.vanier.orbitpro.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.layout.Pane;

/**
 * FXML controller class for the primary stage's scene.
 * 
 * @author bidid
 */
public class FXMLMainAppController {

    private final static Logger logger = LoggerFactory.getLogger(FXMLMainAppController.class);

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnSwitchSecondary;

    @FXML
    private Button btnSwitchTertiary;

    @FXML
    private Pane orbitPane;

    @FXML
    public void initialize() {
        logger.info("Initializing MainAppController...");
        
        Image imageBtnSim1 = new Image("/images/sim1.png");
        Image imageBtnSim2 = new Image("/images/sim2.png");
        
        ImageView imageViewBtnSim1 = new ImageView(imageBtnSim1);
        imageViewBtnSim1.setFitHeight(200);
        imageViewBtnSim1.setFitWidth(200);
        
        ImageView imageViewBtnSim2 = new ImageView(imageBtnSim2);
        imageViewBtnSim2.setFitHeight(200);
        imageViewBtnSim2.setFitWidth(200);
        
        btnSwitchSecondary.setGraphic(imageViewBtnSim1);
        btnSwitchTertiary.setGraphic(imageViewBtnSim2);
        
        Image gif = new Image("/images/dog-moon.gif");
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);

        Background background = new Background(new BackgroundImage(gif,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize));

        borderPane.setBackground(background);
        ImageView logo = new ImageView("/images/OrbitPro.png");
        logo.fitWidthProperty().bind(orbitPane.widthProperty());
        logo.fitHeightProperty().bind(orbitPane.widthProperty());
        orbitPane.getChildren().add(logo);
        btnSwitchSecondary.setOnAction(this::loadSecondaryScene);
        btnSwitchTertiary.setOnAction(this::loadTertiaryScene);
    }

    public void loadSecondaryScene(ActionEvent event) {
        MainApp.switchSceneSim1(MainApp.SECONDARY_LAYOUT, new EscapeVelocityFXMLController());
        logger.info("Loaded the secondary scene...");
    }

    public void loadTertiaryScene(ActionEvent event) {
        MainApp.switchSceneSim2(MainApp.TERTIARY_LAYOUT, new Space_ObjectFXMLController());
        logger.info("Loaded the tertiary scene...");
    }
}
