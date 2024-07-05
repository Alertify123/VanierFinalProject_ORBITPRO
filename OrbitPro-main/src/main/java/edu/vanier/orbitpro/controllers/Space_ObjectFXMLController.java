/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package edu.vanier.orbitpro.controllers;

import edu.vanier.orbitpro.controllers.helpers.PlanetSettings;
import edu.vanier.orbitpro.controllers.helpers.SpaceObjectPhysics;
import edu.vanier.orbitpro.MainApp;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controls interactions and animations for space objects application Controller
 * manages the UI like buttons, sliders, and canvases Handles the media players
 * for playing sounds. Sets up animations for planets and manages logic
 * including collisions and sound effects based on interactions
 *
 *
 * @author ChrisJ
 */
public class Space_ObjectFXMLController implements Initializable {

    private final static Logger logger = LoggerFactory.getLogger(Space_ObjectFXMLController.class);
    // JavaFX components
    @FXML
    private ChoiceBox<String> Actions_ChoiceBox;
    @FXML
    private ScrollPane objectScrollPane;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider massSlider;
    @FXML
    private TextField velocityField;
    @FXML
    private Label infoLabel;
    @FXML
    private Button moreButton;
    @FXML
    private Label timeLabel;
    @FXML
    private Group animationGroup;
    @FXML
    private Pane animationPane;
    @FXML
    private Circle Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Moon, Sun;
    @FXML
    private Label volumeNumbers;
    @FXML
    private Label MassSliderNumbers;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resetButton;

    private double myVolumeNumbers;

    private double myMassSliderNumbers;

    private final String[] actionsChoices = {"Previous Project", "Back To Home", "Light Mode", "Dark Mode"};

    // Helper objects and settings
    private Canvas cartesianPlane;
    private GraphicsContext cartesianGraphicsContext;
    private PlanetSettings planetSettings;

    String[] planetNames = {"Sun", "Mercury", "Venus", "Earth", "Moon", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"};

    VBox buttonContainer;

    // Media players for sound effects
    private MediaPlayer mediaPlayerSpace;
    private MediaPlayer mediaPlayerShip;
    private MediaPlayer mediaPlayerMessage;

    // Collections for managing space objects   
    private List<SpaceObjectPhysics> asteroids = new ArrayList<>();
    private List<Circle> planets;

    private final Random random = new Random();

    // Constants
    private static final int MAX_ASTEROIDS = 3;
    private static final double BORDER_MARGIN = 20.0; // Margin to prevent asteroids from hitting the border

    //ship and animations
    Rectangle ship = new Rectangle(50, 50);
    private AnimationTimer asteroidAnimation;
    private TranslateTransition transition;
    private final List<Animation> allAnimations = new ArrayList<>();

    //flags
    private boolean planetButtonClicked = false;
    private boolean reverseOriginal = false;
    private boolean moreButtonClicked = false;
    private boolean hasAsteroidMovementStarted = false;

    private double shipMass = 1.0;
    final double[] angle = {0, 1, 2, 3, 4, 5, 6, 7, 8};

    // Images for visual objects
    Image asteroidImage = new Image("/images/asteroid.jpg");
    Image spaceShipImage = new Image("/images/SpaceShip.png");

    /**
     * Initializes the controller class. This method sets up UI components,
     * initializes media players, and configures initial animations.
     *
     * @param url The location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the
     * root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        planetSettings = new PlanetSettings();

        
        startButton.setOnAction(event -> startAnimations());
        pauseButton.setOnAction(event -> pauseAnimations());
        resetButton.setOnAction(event -> resetAnimations());

        planets = new ArrayList<>(); 

        
        Circle[] allPlanets = {Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Moon, Sun};

        
        for (Circle planet : allPlanets) {
            if (planet != null) {
                planets.add(planet);
            }
        }

        initializePlanets();

        try {
            mediaPlayerSpace = planetSettings.mediaInitSpace();
            mediaPlayerShip = planetSettings.mediaInitRocket();
            mediaPlayerMessage = planetSettings.mediaInitMessage();

        } catch (Exception e) {
            System.out.println(e);   // Or handle more gracefully, possibly with user notification

        }

        planetSettings.setupPlanetAnimations(animationPane, planets, allAnimations);

        initClock();

        
        startPlanetOrbitAnimation();

        velocityField.setOnAction(event -> {
            updateSpaceshipAnimationDuration();
        });

        animationPane.setBackground(Background.fill(Color.BLACK));

        Actions_ChoiceBox.getItems().addAll(actionsChoices);

        Platform.runLater(() -> {
            double width = animationPane.getWidth();
            double height = animationPane.getHeight();
            cartesianPlane = createCartesianPlane(width, height);
            animationGroup.getChildren().add(cartesianPlane);

            cartesianGraphicsContext = cartesianPlane.getGraphicsContext2D();
        });

        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            handleVolumeSlider();
            double volume = newValue.doubleValue() / 100.0; 
            if (mediaPlayerSpace != null) {
                mediaPlayerSpace.setVolume(volume);
//            System.out.println("Space MediaPlayer Volume: " + volume);
            }
            if (mediaPlayerShip != null) {
                mediaPlayerShip.setVolume(volume);
            }
        });

//        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                mediaPlayerShip.setVolume(volumeSlider.getValue() * 0.02);
//                mediaPlayerSpace.setVolume(volumeSlider.getValue() * 0.02);
//            }
//        });
        volumeNumbers.setText(volumeSlider.getValue() + " dB");

        massSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            handleMassSlider();
            shipMass = newValue.doubleValue();
        });

        MassSliderNumbers.setText(massSlider.getValue() + " Kg");

//        cartesianPlane = createCartesianPlane(1000,751);
        Actions_ChoiceBox.setOnAction(this::getActionChoice);

        moreButton.setOnAction(this::handleMoreButtonAction);

        buttonContainer = new VBox();

        objectScrollPane.setContent(buttonContainer);

        objectScrollPane.setFitToWidth(true); 
        objectScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); 

        infoLabel.setText(" Planet \n Information");

        initializePlanetButtons();

     
        ship.setFill(new ImagePattern(spaceShipImage));
        ship.setTranslateX(100); 
        ship.setTranslateY(100);

        animationPane.getChildren().add(ship);

        setupSpaceshipMovementHandler();

        startAsteroidManagement();

        setupMediaPlayer();
        setupSpaceshipAudio();
    }

    /**
     * Creates a canvas representing a Cartesian plane and draws a grid on it.
     * The grid is drawn using a specified width and height, with a fixed grid
     * color and transparency. This method initializes the canvas and sets its
     * GraphicsContext to draw the Cartesian grid. Additionally, the canvas is
     * made non-interactive by setting mouse events to be transparent, meaning
     * they will not interact with the canvas but pass through to underlying
     * components.
     *
     * @param width The width of the canvas and the Cartesian plane in pixels.
     * @param height The height of the canvas and the Cartesian plane in pixels.
     * @return A Canvas object representing the Cartesian plane with a grid
     * drawn on it.
     */
    private Canvas createCartesianPlane(double width, double height) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        cartesianGraphicsContext = gc;

        drawCartesianGrid(gc, width, height, Color.rgb(255, 255, 255, 0.5));

        canvas.setMouseTransparent(true);

        return canvas;
    }

    /**
     * Draws Cartesian Grid for drawing
     * @param gc Graphics Context for 
     * @param width The width of the canvas and the Cartesian plane in pixels.
     * @param height The height of the canvas and the Cartesian plane in pixels.
     
     * drawn on it.
     */
    private void drawCartesianGrid(GraphicsContext gc, double width, double height, Color strokeColor) {

        double xAxisStart = 0;
        double xAxisEnd = width;

        double gridSize = 20;
        double halfWidth = width / 2;
        double halfHeight = height / 2;
        double gridCountX = halfWidth / gridSize;
        double gridCountY = height / gridSize;

        gc.setStroke(strokeColor);
        gc.setLineWidth(0.5);

        for (int i = 1; i < gridCountX; i++) {
            double x = i * gridSize;
            gc.strokeLine(halfWidth + x, 0, halfWidth + x, height);
            gc.strokeLine(halfWidth - x, 0, halfWidth - x, height);
        }

        for (int i = 1; i < gridCountY; i++) {
            double y = i * gridSize;
            gc.strokeLine(0, halfHeight + y, width, halfHeight + y);
            gc.strokeLine(0, halfHeight - y, width, halfHeight - y);
        }

        gc.setStroke(strokeColor);

        gc.setLineWidth(1);
        gc.strokeLine(xAxisStart, halfHeight, xAxisEnd, halfHeight);
        for (int i = 1; i < gridCountX; i += 2) {
            double x = i * gridSize;
            gc.strokeLine(halfWidth + x, halfHeight - 5, halfWidth + x, halfHeight + 5);
            gc.strokeLine(halfWidth - x, halfHeight - 5, halfWidth - x, halfHeight + 5);

        }

        gc.strokeLine(halfWidth, 0, halfWidth, height);
        for (int i = 1; i < gridCountY; i += 2) {
            double y = i * gridSize;
            gc.strokeLine(halfWidth - 5, halfHeight + y, halfWidth + 5, halfHeight + y);
            gc.strokeLine(halfWidth - 5, halfHeight - y, halfWidth + 5, halfHeight - y);

        }

    }

    /**
    
    * Creates current Time
     */
    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e
                -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            timeLabel.setText(LocalDateTime.now().format(formatter));

        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /**
    * Sets Up events for choice Box
     */
    public void getActionChoice(ActionEvent e) {

        String myActionChoice = Actions_ChoiceBox.getValue();

        switch (myActionChoice) {
            case "Previous Project":

                MainApp.switchSceneSim1(MainApp.SECONDARY_LAYOUT, new EscapeVelocityFXMLController());
                break;
            case "Back To Home":

                MainApp.switchSceneHome(MainApp.MAINAPP_LAYOUT, new FXMLMainAppController());
                break;
            case "Light Mode":
                animationPane.setBackground(Background.fill(Color.WHITE));
                drawCartesianGrid(cartesianGraphicsContext, cartesianPlane.getWidth(), cartesianPlane.getHeight(), Color.rgb(0, 0, 0, 0.3)); 
                break;
            case "Dark Mode":
                animationPane.setBackground(Background.fill(Color.BLACK));
                drawCartesianGrid(cartesianGraphicsContext, cartesianPlane.getWidth(), cartesianPlane.getHeight(), Color.rgb(255, 255, 255, 0.3)); 
                break;
        }
    }

    /**
    * Loads event
     */
    private void loadSecondaryScene(Event e) {

         MainApp.switchSceneInfo(MainApp.FOURTH_LAYOUT, new SpaceObject_SecondaryController());
    }

    @FXML
    private void handleVolumeSlider() {
        myVolumeNumbers = volumeSlider.getValue();
        volumeNumbers.setText(String.format("%.2f %s ", myVolumeNumbers, " dB"));
    }

    @FXML
    private void handleMassSlider() {
        myMassSliderNumbers = massSlider.getValue();
        MassSliderNumbers.setText(String.format("%.2f %s ", myMassSliderNumbers, " Kg"));
    }

    /**
    * Handles Planet Button, when clicked
    * @param label sets information in label
    * @param planetName takes planetName
     */
    private void handlePlanetButton(ActionEvent event, Label label, String planetName) {
        planetButtonClicked = true; // Indicate that a planet button has been clicked
        PlanetSettings.setSelectedPlanetName(planetName);
        planetSettings.setPlanet(planetName);

        double radius = planetSettings.getRadius();
        double mass = planetSettings.getMass();

        Platform.runLater(() -> {
            label.setText(planetName + "\n" + String.format("%.2f", radius) + " KM" + "\n" + mass + " KG");
        });

        String planetInfoName = planetName;
        
        PlanetSettings.setSelectedPlanetName(planetInfoName);

        
        checkAndPlaySpaceSound();

    }

    /**
    * checks collision between objects
    * @param asteroid takes spaceObjectphysics
    * @param planet takes circle Planets
     */
    private boolean checkCollision(SpaceObjectPhysics asteroid, Circle planet) {
        double asteroidCenterX = asteroid.getShape().getTranslateX();
        double asteroidCenterY = asteroid.getShape().getTranslateY();
        double planetCenterX = planet.getLayoutX();
        double planetCenterY = planet.getLayoutY();

//        System.out.println(asteroid.getShape().getTranslateX());
        double dx = asteroidCenterX - planetCenterX;
        double dy = asteroidCenterY - planetCenterY;
        double distance = Math.sqrt((dx * dx) + (dy * dy));

        return distance < (asteroid.getShape().getRadius() + planet.getRadius());

    }

   /**
    * creates asteroids to create
    * @param paneWidth width of pane
    * @param pane Height pane of Height
    * @param asteroids List of physics
    * @param animationPane takes pane
    * @param asteroidImage takes image for asteroid
     */
    private void createAsteroid(double paneWidth, double paneHeight, List<SpaceObjectPhysics> asteroids, Pane animationPane, Image asteroidImage) {
        if (asteroids.size() < MAX_ASTEROIDS) { 
            double radius = 10; // Standard radius
            double speed = hasAsteroidMovementStarted ? 50 : 0; 
            double directionX = random.nextBoolean() ? 1 : -1; 
            double directionY = random.nextBoolean() ? 1 : -1; 

            SpaceObjectPhysics asteroid = new SpaceObjectPhysics(radius, speed, directionX, directionY);
            asteroid.getShape().setFill(new ImagePattern(asteroidImage));
            asteroid.getShape().setTranslateX(random.nextDouble() * paneWidth);
            asteroid.getShape().setTranslateY(random.nextDouble() * paneHeight);

            asteroids.add(asteroid);
            animationPane.getChildren().add(asteroid.getShape());

        }
    }

    
    private void initializeAsteroids(double paneWidth, double paneHeight, Pane animationPane, Image asteroidImage) {
        
        asteroids = new ArrayList<>();

       
        for (int i = 0; i < 5; i++) {
            createAsteroid(paneWidth, paneHeight, asteroids, animationPane, asteroidImage);
        }

        if (asteroidAnimation == null) {
            asteroidAnimation = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (!reverseOriginal) {
                        Iterator<SpaceObjectPhysics> iterator = asteroids.iterator();
                        while (iterator.hasNext()) {
                            SpaceObjectPhysics asteroid = iterator.next();
                            asteroid.updatePosition(0.016); 

                            boolean removeAsteroid = false;
                            for (Circle planet : planets) {
                                if (checkCollision(asteroid, planet)) {
                                    Platform.runLater(() -> animationPane.getChildren().remove(asteroid.getShape()));
                                    removeAsteroid = true;
                                    break;
                                }
                            }

                            if (!removeAsteroid && asteroid.isNearBorder(animationPane.getWidth(), animationPane.getHeight(), BORDER_MARGIN)) {
                                removeAsteroid = true;
                            }

                            if (removeAsteroid) {
                                Platform.runLater(() -> animationPane.getChildren().remove(asteroid.getShape()));
                                iterator.remove();
                            }
                        }

                        manageAsteroids();
                    }
                }
            };
        }
    }

    
    private boolean checkSpaceshipCollisionPlanets(Rectangle spaceship, List<Circle> planets) {
        
        double spaceshipCenterX = spaceship.getTranslateX() + spaceship.getWidth() / 2;
        double spaceshipCenterY = spaceship.getTranslateY() + spaceship.getHeight() / 2;
        
        double spaceshipRadius = Math.sqrt(Math.pow(spaceship.getWidth() / 2, 2) + Math.pow(spaceship.getHeight() / 2, 2));

        for (Circle planet : planets) {
            double planetCenterX = planet.getLayoutX();
            double planetCenterY = planet.getLayoutY();
            double planetRadius = planet.getRadius();

          
            double dx = spaceshipCenterX - planetCenterX;
            double dy = spaceshipCenterY - planetCenterY;
            double distance = Math.sqrt(dx * dx + dy * dy);

//            System.out.println(spaceshipCenterX);
           
            if (distance < (spaceshipRadius + planetRadius)) {
                handleCollision(planet.getId());
                return true;
            }
//            System.out.println(planet.getId());
        }

        return false;

    }

    
    private boolean checkSpaceshipCollisionAsteroids(Rectangle spaceship, List<SpaceObjectPhysics> asteroids) {
        
        double spaceshipCenterX = spaceship.getTranslateX() + spaceship.getWidth() / 2;
        double spaceshipCenterY = spaceship.getTranslateY() + spaceship.getHeight() / 2;
        
        double spaceshipRadius = Math.sqrt(Math.pow(spaceship.getWidth() / 2, 2) + Math.pow(spaceship.getHeight() / 2, 2));

        for (SpaceObjectPhysics asteroid : asteroids) {
            Circle asteroidShape = asteroid.getShape();
           
            double asteroidCenterX = asteroidShape.getTranslateX() + asteroidShape.getRadius();
            double asteroidCenterY = asteroidShape.getTranslateY() + asteroidShape.getRadius();
            double asteroidRadius = asteroidShape.getRadius();

            
            double dx = spaceshipCenterX - asteroidCenterX;
            double dy = spaceshipCenterY - asteroidCenterY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            
            if (distance < (spaceshipRadius + asteroidRadius)) {
                return true; 

            }
        }

        return false;
    }

    
    private void handleCollision(String planetName) {
        planetSettings.setPlanet(planetName); 
        planetSettings.setShipMass(shipMass); 
        double force = planetSettings.calculateGravitationalForce();
        double escapeVelocity = planetSettings.calculateEscapeVelocity();

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Collision Detected");
            alert.setHeaderText("Collision with " + planetName);
            alert.setContentText(String.format("Collision detected with %s!\nGravitational Force: %.2e N\nEscape Velocity: %.2f km/s", planetName, force, escapeVelocity));
            alert.showAndWait();
        });
    }

    
    private void manageAsteroids() {
        Platform.runLater(() -> {
            while (asteroids.size() < MAX_ASTEROIDS) {
                createAsteroid(animationPane.getWidth(), animationPane.getHeight(), asteroids, animationPane, asteroidImage);
            }
        });
    }

    
    private void stopAllAnimations() {
        for (Animation animation : allAnimations) {
            animation.stop();
        }

        mediaPlayerSpace.stop();

        mediaPlayerShip.stop();

    }

    
    private void checkAndHandleCollision() {
        if (checkSpaceshipCollisionPlanets(ship, planets)) {
            stopAllAnimations();
            stopSpaceship();
            reverseOriginal = true;
            Platform.runLater(() -> showAlertCollision("You hit a planet!"));
        } else if (checkSpaceshipCollisionAsteroids(ship, asteroids)) {
            stopAllAnimations();
            stopSpaceship();
            reverseOriginal = true;
            Platform.runLater(() -> showAlertCollision("You hit an asteroid!"));
        }
    }

    
    private void showAlertCollision(String message) {
        mediaPlayerMessage.play();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Planet Collided");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnHidden(event -> resumeGame());
        alert.showAndWait();
    }

   
    private void resumeGame() {
        reverseOriginal = false; 
        
        for (Animation animation : allAnimations) {
            animation.play();
        }

    }

    
    private void startAnimations() {
        // Resume all registered animations
        for (Animation animation : allAnimations) {
            if (animation.getStatus() == Animation.Status.PAUSED) {
                animation.play();
            }
        }

        
        if (asteroidAnimation != null && !reverseOriginal) {
            asteroidAnimation.start();
        }

        // Resume the spaceship transition
        if (transition != null && transition.getStatus() == Animation.Status.PAUSED) {
            transition.play();
            mediaPlayerShip.play();
        }

        startAsteroids(); 
        allAnimations.forEach(Animation::play); 

        allAnimations.forEach(Animation::play);

        
    }


    private void pauseAnimations() {
        
        for (Animation animation : allAnimations) {
            if (animation.getStatus() == Animation.Status.RUNNING) {
                animation.pause();
                mediaPlayerSpace.stop();
            }
        }

        
        if (asteroidAnimation != null) {
            asteroidAnimation.stop();
            mediaPlayerSpace.stop();
        }

        
        if (transition != null && transition.getStatus() == Animation.Status.RUNNING) {
            transition.pause();
            mediaPlayerSpace.stop();
        }
        mediaPlayerSpace.stop();

       
        pauseMediaPlayers();
    }

   
    private void resetAnimations() {
        
        for (Animation animation : allAnimations) {
            animation.stop();
            animation.jumpTo(Duration.ZERO);
        }

        
        if (asteroidAnimation != null) {
            asteroidAnimation.stop();
        }

        
        if (transition != null) {
            transition.stop();
        }

        
        ship.setTranslateX(100);  
        ship.setTranslateY(100);
        ship.setRotate(0); 

        
        clearAsteroids();

        shipMass = 10;
        massSlider.setValue(shipMass);
        MassSliderNumbers.setText(String.format("%.2f Kg", shipMass));

        
        initializeAsteroids(animationPane.getWidth(), animationPane.getHeight(), animationPane, asteroidImage);

        setupInitialPlanetPositions(); 

        
        resetMediaPlayers();

        
        resetGameState();
    }

    
    private void resetGameState() {

        resetFlags(); 



        reverseOriginal = false; 

    }

  
    private void resetMediaPlayers() {
        if (mediaPlayerSpace != null) {
            mediaPlayerSpace.stop();
            mediaPlayerSpace.seek(Duration.ZERO);
        }
        if (mediaPlayerShip != null) {
            mediaPlayerShip.stop();
            mediaPlayerShip.seek(Duration.ZERO);
            
        }
    }

  
    private void clearAsteroids() {
        if (asteroids != null) {
            Iterator<SpaceObjectPhysics> iterator = asteroids.iterator();
            while (iterator.hasNext()) {
                SpaceObjectPhysics asteroid = iterator.next();
                Platform.runLater(() -> animationPane.getChildren().remove(asteroid.getShape()));
                iterator.remove();
            }
        }
    }

  
    private void pauseMediaPlayers() {
        if (mediaPlayerSpace != null || mediaPlayerSpace.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayerSpace.stop();  // Ensures it stops only if it is playing
            mediaPlayerSpace.seek(Duration.ZERO); // Reset to start
            System.out.println("Space sound stopped.");
        }
        if (mediaPlayerShip != null || mediaPlayerShip.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayerShip.stop();  // Ensures it stops only if it is playing
            System.out.println("Ship sound stopped.");
        }

    }


    private void stopSpaceship() {
        if (transition != null) {
            transition.stop();
        }
    }

    
    private void checkAndPlaySpaceSound() {
       
        if (planetButtonClicked && moreButtonClicked) {

            mediaPlayerSpace.setVolume(0.05);
            mediaPlayerSpace.play(); 
            mediaPlayerSpace.setCycleCount(MediaPlayer.INDEFINITE); 

        }

    }

    
    private void resetFlags() {
        planetButtonClicked = false;
        moreButtonClicked = false;
    }

    
    private void updateAsteroids() {
        Iterator<SpaceObjectPhysics> iterator = asteroids.iterator();
        while (iterator.hasNext()) {
            SpaceObjectPhysics asteroid = iterator.next();
            asteroid.updatePosition(0.016); 
        }
    }

    
    private void checkAsteroidCollisions() {
        List<SpaceObjectPhysics> toRemove = new ArrayList<>();
        try {
            for (int i = 0; i < asteroids.size(); i++) {
                for (int j = i + 1; j < asteroids.size(); j++) {
                    SpaceObjectPhysics a1 = asteroids.get(i);
                    SpaceObjectPhysics a2 = asteroids.get(j);
                    if (areColliding(a1, a2)) {
                        toRemove.add(a1);
                        toRemove.add(a2);
                    }
                }
            }

           
            toRemove.forEach(asteroid -> {
                asteroids.remove(asteroid);
                Platform.runLater(() -> animationPane.getChildren().remove(asteroid.getShape()));
            });
        } catch (Exception e) {
            logger.error("Concurrent modification error during collision checking", e);
        }
    }

    
    private boolean areColliding(SpaceObjectPhysics a1, SpaceObjectPhysics a2) {
        double dx = a1.getShape().getTranslateX() - a2.getShape().getTranslateX();
        double dy = a1.getShape().getTranslateY() - a2.getShape().getTranslateY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (a1.getShape().getRadius() + a2.getShape().getRadius());
    }

   
    private void handleMoreButtonAction(Event event) {
        moreButtonClicked = true; // Flag the more button as clicked

        if (!planetButtonClicked) {
            
            showAlertMoreButton("Please Click a Planet Button First!", () -> {
                
                moreButtonClicked = false;
            });
        } else {
          
            checkAndPlaySpaceSound();
            loadSecondaryScene(event);
            moreButtonClicked = false;
        }

     
        if (mediaPlayerMessage != null) {
            mediaPlayerMessage.stop(); 
            mediaPlayerMessage.seek(Duration.ZERO);
            mediaPlayerMessage.play();
        }

        
        planetButtonClicked = false;
    }

    
    private void showAlertMoreButton(String message, Runnable onDismiss) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reminder");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnHidden(evt -> onDismiss.run()); 
        alert.showAndWait();
    }

    
    private void setupInitialPlanetPositions() {

        Sun.setRotate(0);
        Mercury.setRotate(0); 
        Venus.setRotate(0);
        Earth.setRotate(0);
        Mars.setRotate(0);
        Jupiter.setRotate(0);
        Saturn.setRotate(0);
        Uranus.setRotate(0);
        Neptune.setRotate(0);

        
        planetSettings.setupPlanetAnimations(animationPane, planets, allAnimations);
    }

    
    private void setupPlanetAppearance(Circle planet, String imagePath, Color strokeColor) {
        Image image = new Image(imagePath);
        planet.setStroke(strokeColor);
        planet.setFill(new ImagePattern(image));
    }

   
    private void initializePlanets() {
        setupPlanetAppearance(Sun, "/images/Sun.gif", Color.BLACK);
        setupPlanetAppearance(Mercury, "/images/mercury.gif", Color.BLACK);
        setupPlanetAppearance(Venus, "/images/venus.gif", Color.BLACK);
        setupPlanetAppearance(Earth, "/images/earth.gif", Color.BLACK);
        setupPlanetAppearance(Moon, "/images/moon.gif", Color.BLACK);
        setupPlanetAppearance(Mars, "/images/mars.gif", Color.BLACK);
        setupPlanetAppearance(Jupiter, "/images/jupiter.jpg", Color.BLACK);
        setupPlanetAppearance(Saturn, "/images/saturn.png", Color.BLACK);
        setupPlanetAppearance(Uranus, "/images/uranus.gif", Color.BLACK);
        setupPlanetAppearance(Neptune, "/images/neptune.gif", Color.BLACK);
        setupPlanetAppearance(Neptune, "/images/neptune.gif", Color.BLACK);
    }

    
    private void startAsteroids() {
        hasAsteroidMovementStarted = true;
        asteroids.forEach(asteroid -> {
            double speed = 50; // Standard moving speed
            asteroid.setSpeed(speed);
        });
    }

   
    private void updateSpaceshipAnimationDuration() {
        try {

            double velocityValue = Double.parseDouble(velocityField.getText());


 
            if (velocityValue < 1 || velocityValue > 10) {

                showAlert("Invalid Input", "Velocity must be greater than 1 and less than 10");
                return;
            }

            
            double duration = calculateDuration(velocityValue);

           
            setSpaceshipAnimationDuration(duration);

            System.out.println(duration);
        } catch (NumberFormatException e) {
           
            showAlert("Invalid Input", "Please enter a valid numeric value for velocity.");
        }
    }

 
    private void setSpaceshipAnimationDuration(double duration) {
        if (transition != null) {
           
            transition.stop();
           
            transition.setDuration(Duration.seconds(duration));
            transition.play();
        }
    }

   
    private void showAlert(String title, String content) {
        mediaPlayerMessage.play();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

   
    private void startAsteroidManagement() {
        Platform.runLater(() -> {
            double paneWidth = animationPane.getWidth();
            double paneHeight = animationPane.getHeight();

            initializeAsteroids(paneWidth, paneHeight, animationPane, asteroidImage);

            
            asteroidAnimation = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    try {
                        if (!reverseOriginal) {
                            Iterator<SpaceObjectPhysics> iterator = asteroids.iterator();

                            updateAsteroids();
                            checkAsteroidCollisions();

                            while (iterator.hasNext()) {
                                SpaceObjectPhysics asteroid = iterator.next();

                               
                                asteroid.updatePosition(0.016); 

                                
                                boolean removeAsteroid = false;
                                for (Circle planet : planets) {
                                    if (checkCollision(asteroid, planet)) {
                                        Platform.runLater(() -> animationPane.getChildren().remove(asteroid.getShape()));
                                        removeAsteroid = true;
                                        break;
                                    }
                                }

                               
                                if (!removeAsteroid && asteroid.isNearBorder(animationPane.getWidth(), animationPane.getHeight(), BORDER_MARGIN)) {
                                    removeAsteroid = true;
                                }

                                if (removeAsteroid) {
                                    Platform.runLater(() -> animationPane.getChildren().remove(asteroid.getShape()));
                                    iterator.remove();
                                }
                            }

                            
                            manageAsteroids();
                        }
                    } catch (Exception e) {
                        logger.error("Error during asteroid animation: " + e.getMessage(), e);
                    }
                }
            };

            asteroidAnimation.start(); 
        });
    }

   
    private void setupSpaceshipMovementHandler() {
        Platform.runLater(() -> {
            double paneWidth = animationPane.getWidth(); 
            double paneHeight = animationPane.getHeight(); 

            animationPane.setOnMouseClicked((MouseEvent event) -> {
                try {
                    double velocityValue = Double.parseDouble(velocityField.getText()); 
                    if (velocityValue <= 0) {
                        showAlert("Invalid Input", "Velocity must be greater than zero.");
                        return;
                    }

                    
                    double baseDuration = 20; 
                    double duration = Math.max(0.1, baseDuration / (velocityValue / 2)); 

                    double margin = 10; 

                    double targetX = event.getX();
                    double targetY = event.getY();

                    double currentX = ship.getTranslateX();
                    double currentY = ship.getTranslateY();

                   
                    targetX = Math.min(Math.max(margin, targetX), paneWidth - ship.getWidth() - margin);
                    targetY = Math.min(Math.max(margin, targetY), paneHeight - ship.getHeight() - margin);

                    
                    double rotateAngle = Math.toDegrees(Math.atan2(targetY - currentY, targetX - currentX)) - 180;

                   
                    ship.setRotate(rotateAngle);

                    
                    transition = new TranslateTransition(Duration.seconds(duration), ship);
                    transition.setToX(targetX);
                    transition.setToY(targetY);

                    transition.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                        if (!reverseOriginal) {
                            checkAndHandleCollision();
                        }
                    });

                    
                    Timeline startDelay = new Timeline(new KeyFrame(Duration.millis(1), e -> mediaPlayerShip.play()));

                    
                    transition.setOnFinished(e -> mediaPlayerShip.stop());

                    
                    startDelay.setCycleCount(1);
                    startDelay.play();

                   
                    transition.play();

                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter a valid number for velocity.");
                }
            });
        });
    }

    
    public void startPlanetOrbitAnimation() {
        AnimationTimer planetOrbitTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Set planet positions based on their orbital radius and angle
                setPlanetPosition(10, 8, Mercury);
                setPlanetPosition(47, 7, Venus);
                setPlanetPosition(100, 6, Earth);
                setPlanetPosition(152, 5, Mars);
                setPlanetPosition(200, 4, Jupiter);
                setPlanetPosition(244, 3, Saturn);
                setPlanetPosition(298, 2, Uranus);
                setPlanetPosition(340, 1, Neptune);

               
                double x = Earth.getLayoutX() + (Earth.getRadius() + 15) * Math.cos(angle[8]);
                double y = Earth.getLayoutY() + (Earth.getRadius() + 15) * Math.sin(angle[8]);
                Moon.setLayoutX(x);
                Moon.setLayoutY(y);

               
                angle[0] += Math.toRadians(1);      
                angle[1] += Math.toRadians(0.4040 / 8);
                angle[2] += Math.toRadians(0.5050 / 8);
                angle[3] += Math.toRadians(0.7148 / 8);
                angle[4] += Math.toRadians(0.9634 / 8);
                angle[5] += Math.toRadians(1.7793 / 8);
                angle[6] += Math.toRadians(2.1756 / 8);
                angle[7] += Math.toRadians(2.5641 / 8);
                angle[8] += Math.toRadians(3.4965 / 8);
            }
        };
        planetOrbitTimer.start();
    }

    
    private void setPlanetPosition(double orbitalDistance, int angleIndex, Circle planet) {
        double xPlanet = Sun.getLayoutX() + (Sun.getRadius() + orbitalDistance) * Math.cos(angle[angleIndex]);
        double yPlanet = Sun.getLayoutY() + (Sun.getRadius() + orbitalDistance) * Math.sin(angle[angleIndex]);
        planet.setLayoutX(xPlanet);
        planet.setLayoutY(yPlanet);
    }

    
    private void initializePlanetButtons() {
        for (String planetText : planetNames) {
            Button planetButton = new Button(planetText);
            planetButton.setOnAction(event -> handlePlanetButton(event, infoLabel, planetText));
            buttonContainer.getChildren().add(planetButton);
        }
        buttonContainer.setSpacing(10); 
    }

  
    private void setupMediaPlayer() {
        if (mediaPlayerMessage != null) {
            mediaPlayerMessage.setOnEndOfMedia(() -> {
                mediaPlayerMessage.stop();
                mediaPlayerMessage.seek(Duration.ZERO);  
            });
        }
    }

   
    private void setupSpaceshipAudio() {
        try {
            
            mediaPlayerShip = planetSettings.mediaInitRocket();
            if (mediaPlayerShip != null) {
                mediaPlayerShip.setCycleCount(MediaPlayer.INDEFINITE);
            }
        } catch (Exception e) {
            System.out.println("Error initializing spaceship media player: " + e.getMessage());
        }
    }

    
    private double calculateDuration(double velocity) {
     
        double m = -1.222;
        double b = 16.222;

       
        double duration = m * velocity + b;

       
        return Math.max(Math.min(duration, 15), 4);

    }

}
