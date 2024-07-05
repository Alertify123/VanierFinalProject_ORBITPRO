package edu.vanier.orbitpro;

import javafx.application.Application;
import edu.vanier.orbitpro.controllers.EscapeVelocityFXMLController;
import edu.vanier.orbitpro.controllers.FXMLMainAppController;
import edu.vanier.orbitpro.controllers.Space_ObjectFXMLController;
import edu.vanier.orbitpro.controllers.SpaceObject_SecondaryController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import javafx.scene.image.Image;

/**
 * Main class for the OrbitPro application.
 * 
 * @author bidid
 */
public class MainApp extends Application {

    public static final String MAINAPP_LAYOUT = "MainApp_layout";
    public static final String SECONDARY_LAYOUT = "EscapeVelocity_Layout";
    public static final String TERTIARY_LAYOUT = "Space_ObjectFXML";
    public static final String FOURTH_LAYOUT = "SpaceObject_Secondary";
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            logger.info("Bootstrapping the application...");
            Parent root = loadFXML(MAINAPP_LAYOUT, new FXMLMainAppController());
            scene = new Scene(root, 640, 480);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            Image icon = new Image("/images/OrbitPro.png");
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("WELCOME TO SPACE OBJECT");
            primaryStage.setAlwaysOnTop(true);
            primaryStage.show();
            primaryStage.setAlwaysOnTop(false);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Changes the primary stage's current scene.
     *
     * @param fxmlFile The name of the FXML file to be loaded.
     * @param fxmlController An instance of the FXML controller to be associated
     * with the loaded FXML scene graph.
     */
    public static void switchSceneSim1(String fxmlFile, Object fxmlController) {
        try {
            scene.setRoot(loadFXML(SECONDARY_LAYOUT, new EscapeVelocityFXMLController()));
        } catch (IOException ex) {
            logger.error("Error loading FXML file: " + fxmlFile, ex);
        }
    }
    
        /**
     * Changes the primary stage's current scene.
     *
     * @param fxmlFile The name of the FXML file to be loaded.
     * @param fxmlController An instance of the FXML controller to be associated
     * with the loaded FXML scene graph.
     */
    public static void switchSceneSim2(String fxmlFile, Object fxmlController) {
        try {
            scene.setRoot(loadFXML(TERTIARY_LAYOUT, new Space_ObjectFXMLController()));
        } catch (IOException ex) {
            logger.error("Error loading FXML file: " + fxmlFile, ex);
        }
    }

        /**
     * Changes the primary stage's current scene.
     *
     * @param fxmlFile The name of the FXML file to be loaded.
     * @param fxmlController An instance of the FXML controller to be associated
     * with the loaded FXML scene graph.
     */
    public static void switchSceneInfo(String fxmlFile, Object fxmlController) {
        try {
            scene.setRoot(loadFXML(FOURTH_LAYOUT, new SpaceObject_SecondaryController()));
        } catch (IOException ex) {
            logger.error("Error loading FXML file: " + fxmlFile, ex);
        }
    }
    
    public static void switchSceneHome(String fxmlFile, Object fxmlController) {
        try {
            scene.setRoot(loadFXML(MAINAPP_LAYOUT, new FXMLMainAppController()));
        } catch (IOException ex) {
            logger.error("Error loading FXML file: " + fxmlFile, ex);
        }
    }

    /**
     * Loads a scene graph from an FXML file.
     *
     * @param fxmlFile The name of the FXML file to be loaded.
     * @param fxmlController An instance of the FXML controller to be associated
     * with the loaded FXML scene graph.
     * @return The root node of the loaded scene graph.
     * @throws IOException
     */
    public static Parent loadFXML(String fxmlFile, Object fxmlController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/" + fxmlFile + ".fxml"));
        fxmlLoader.setController(fxmlController);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
