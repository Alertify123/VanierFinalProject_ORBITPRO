/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.orbitpro.controllers.helpers;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * A helper class for managing various planet-related settings, such as gravitational constants,
 * mass, radius, and related animations for a planetary simulation.
 * This class includes methods to initialize media, set up animations, and calculate physical properties
 * like gravitational force and escape velocity for different celestial bodies.
 */
public class PlanetSettings {

    private static final double G = 6.674 * Math.pow(10, -11); // Gravitational constant in m³/kg/s²

    private static String selectedPlanetName;

    private double mass;
    private double radius;
    private double height;
    private Color planetColor;
    private double gravity;
    private double time;
    private double shipMass;

    private List<Animation> planetAnimations = new ArrayList<>();

    private String facts;

    
    /**
     * Constructs a new instance of PlanetSettings and resets its attributes.
     */
    public PlanetSettings() {
        reset();

    }
    
     /**
     * Initializes a media player with a background space sound.
     * @return MediaPlayer instance loaded with the background space sound.
     */
    public MediaPlayer mediaInitSpace() {
        String spaceSoundFile = getClass().getResource("/sound/spaceSound.mp3").toString();
        Media backgroundSound = new Media(spaceSoundFile);
        return new MediaPlayer(backgroundSound);
    }

     /**
     * Initializes a media player with a rocket sound effect.
     * @return MediaPlayer instance loaded with the rocket sound effect.
     */
    public MediaPlayer mediaInitRocket() {
        String audioFilePath = getClass().getResource("/sound/spaceRocket.mp3").toString();
        Media shipSound = new Media(audioFilePath);
        return new MediaPlayer(shipSound);
    }

    /**
     * Initializes a media player with a message notification sound.
     * @return MediaPlayer instance loaded with the message sound.
     */
    public MediaPlayer mediaInitMessage() {
        String audioFilePath = getClass().getResource("/sound/message.mp3").toString();
        Media messageSound = new Media(audioFilePath);
        return new MediaPlayer(messageSound);
    }

     /**
     * Sets the properties of a planet based on a given planet name.
     * @param planet A string representing the name of the planet.
     */
    public void setPlanet(String planet) {
        switch (planet) {
            case "Mercury" -> {
                mass = 3.285 * Math.pow(10, 23);
                radius = 2.4397 * Math.pow(10, 3);
                gravity = 3.7;
                planetColor = Color.GRAY;
                facts = "Mercury is one of the five classical planets visible with the naked eye,\n"
                        + "and is named after the swift-footed Roman messenger god.\n"
                        + "It is not known exactly when the planet was first discovered,\n"
                        + "although it was first observed through telescopes in the seventeenth century\n"
                        + "by astronomers Galileo Galilei and Thomas Harriot.\n"
                        + "More information \n" + "https://www.nhm.ac.uk/discover/planet-mercury.html#:~:text=Mercury%20is%20one%20of%20the,Galileo%20Galilei%20and%20Thomas%20Harriot.";
            }
            case "Venus" -> {
                mass = 4.867 * Math.pow(10, 24);
                radius = 6.0518 * Math.pow(10, 3);
                gravity = 8.87;
                planetColor = Color.YELLOW;
                facts = "The first person to point a telescope at Venus was Galileo Galilei in 1610. \n"
                        + "Even with his crude telescope, Galileo realized that Venus goes through phases like the Moon.\n"
                        + "These observations helped support the Copernican view that the planets orbited the Sun, \n"
                        + "and not the Earth as previously believed.";
            }
            case "Earth" -> {
                mass = 5.9723 * Math.pow(10, 24);
                radius = 6.371 * Math.pow(10, 3);
                gravity = 9.81;
                planetColor = Color.BLUE;
                facts = "Earth has never been perfectly round.\n"
                        + "The planet bulges around the equator by an extra 0.3 percent\n"
                        + "as a result of the fact that it rotates about its axis.\n"
                        + "Recent research from NASA's Jet Propulsion Laboratory\n"
                        + "suggests that melting glaciers are causing Earth's waistline to spread.\n"
                        + "Read more: https://climate.nasa.gov/news/2469/10-interesting-things-about-earth/";
            }
            case "Mars" -> {
                mass = 6.4171 * Math.pow(10, 23);
                radius = 3.3895 * Math.pow(10, 3);
                gravity = 3.721;
                planetColor = Color.RED;
                facts = "The atmosphere on Mars is about 100 times thinner than the Earth's atmosphere,\n"
                        + "and it has very little oxygen.\n"
                        + "You wouldn’t be able to breathe the Martian air and would need a spacesuit with oxygen\n"
                        + "when you went outdoors.\n"
                        + "Mars has two moons, and their names are Phobos and Deimos.\n"
                        + "https://www.learningresources.co.uk/blog/10-facts-about-mars-for-kids/";
            }
            case "Jupiter" -> {
                mass = 1.8982 * Math.pow(10, 27);
                radius = 6.9911 * Math.pow(10, 4);
                gravity = 24.79;
                planetColor = Color.ORANGE;
                facts = "Jupiter experiences giant storms, powerful winds, aurorae,\n"
                        + "and extreme temperatures and pressures.\n"
                        + "The Great Red Spot is a high-pressure storm that has been raging\n"
                        + "for several centuries: its winds swirl rapidly, hitting speeds of up to 680 km per hour\n"
                        + "(over three times as fast as the most powerful hurricanes ever recorded on Earth).\n"
                        + "Learn more: https://www.esa.int/Science_Exploration/Space_Science/Juice/Facts_about_Jupiter";
            }
            case "Saturn" -> {
                mass = 5.6834 * Math.pow(10, 26);
                radius = 5.8232 * Math.pow(10, 4);
                gravity = 10.44;
                planetColor = Color.BISQUE;
                facts = "It is a gas giant with an average radius of about nine-and-a-half times that of Earth.\n"
                        + "It has only one-eighth the average density of Earth,\n"
                        + "but is over 95 times more massive.\n"
                        + "Even though Saturn is nearly the size of Jupiter,\n"
                        + "Saturn has less than one-third of Jupiter's mass.\n"
                        + "Read more: https://en.wikipedia.org/wiki/Saturn#:~:text=It%20is%20a%20gas%20giant,one%2Dthird%20of%20Jupiter's%20mass.";
            }
            case "Uranus" -> {
                mass = 8.6810 * Math.pow(10, 25);
                radius = 2.5362 * Math.pow(10, 4);
                gravity = 8.87;
                planetColor = Color.AQUAMARINE;
                facts = "Uranus is a very cold and windy world.\n"
                        + "The ice giant is surrounded by 13 faint rings and 28 small moons.\n"
                        + "Uranus rotates at a nearly 90-degree angle from the plane of its orbit.\n"
                        + "This unique tilt makes Uranus appear to spin sideways,\n"
                        + "orbiting the Sun like a rolling ball.\n"
                        + "Learn more: https://science.nasa.gov/uranus/facts/#:~:text=Uranus%20is%20a%20very%20cold,Sun%20like%20a%20rolling%20ball.";
            }
            case "Neptune" -> {
                mass = 1.02413 * Math.pow(10, 26);
                radius = 2.4622 * Math.pow(10, 4);
                gravity = 11.15;
                planetColor = Color.CORNFLOWERBLUE;
                facts = "Neptune was the first planet located through mathematical calculations.\n"
                        + "Using predictions sent to him by French astronomer Urbain Le Verrier,\n"
                        + "based on disturbances in the orbit of Uranus,\n"
                        + "German astronomer Johann Galle was the first to observe the planet in 1846.\n"
                        + "The planet is named after the Roman god of the sea, as suggested by Le Verrier.\n"
                        + "Learn more: https://science.nasa.gov/neptune/";
            }

            case "Sun" -> {
                mass = 1.9885 * Math.pow(10, 30);
                radius = 6.9630 * Math.pow(10, 5);
                gravity = 274;
                planetColor = Color.ORANGE;
                facts = "The Sun is a ball of gas and plasma - around 91% of it is hydrogen gas.\n"
                        + "Under intense heat and gravitational force, this is fused into helium during nuclear fusion.\n\n"
                        + "When the plasma is heated to the temperatures seen on the Sun,\n"
                        + "it contains so much energy that the charged particles can escape the star's gravity\n"
                        + "and blow out into space.\n"
                        + "Learn more: https://www.nhm.ac.uk/discover/factfile-the-sun.html";
            }

            case "Moon" -> {
                mass = 7.342 * Math.pow(10, 22);
                radius = 1.7381 * Math.pow(10, 3);
                gravity = 1.622;
                planetColor = Color.GREY;
                facts = "It is the fifth largest natural satellite in the Solar System,\n"
                        + "and the largest among planetary satellites relative to the size of the planet that it orbits.\n"
                        + "The Moon is in synchronous rotation with Earth.\n"
                        + "Its near side is marked by large dark plains (volcanic 'maria')\n"
                        + "that fill the spaces between the bright ancient crustal highlands and the prominent impact craters.\n"
                        + "Learn more: https://www.rmg.co.uk/stories/topics/interesting-facts-about-moon";
            }
            default ->
                throw new IllegalArgumentException("Unknown planet: " + planet);
        }
    }

    /**
     * Calculates and returns the escape velocity based on the planet's mass and radius.
     * @return The escape velocity in m/s.
     */
    public double calculateEscapeVelocity() {
        return (Math.sqrt((2 * G * mass) / (radius * 1000))) / 1000; // Escape velocity in m/s
    }

    /**
     * Resets the attributes of this object to their default states.
     */
    public void reset() {
        height = 0;
        time = 0;
    }

    public double getHeight() {
        return height;
    }

    public double getTime() {
        return time;
    }

    public double getGravity() {
        return gravity;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public Color getPlanetColor() {
        return planetColor;
    }

    public String facts() {
        return facts;
    }
    
    public double calculateGravitationalForce() {
        double planetMass = getMass(); 
        double spaceshipMass = this.shipMass; 
        double planetRadius = this.getRadius(); 

       
        if (radius == 0) {
            throw new IllegalArgumentException("Radius must be greater than zero to calculate gravitational force.");
        }

        // Calculate the gravitational force
        double forceMagnitude = (G * spaceshipMass * planetMass) / Math.pow(planetRadius, 2);

        return forceMagnitude; 
    }

    public static void setSelectedPlanetName(String planetName) {
        selectedPlanetName = planetName;
    }

    public static String getSelectedPlanetName() {
        return selectedPlanetName;
    }

    public void setShipMass(double mass) {
        this.shipMass = mass;
    }

    /**
     * Sets up animations for each planet within the given pane.
     * @param animationPane The pane in which to display the animations.
     * @param planets A list of Circle objects representing the planets.
     * @param allAnimations A list to store all the animations.
     */
    public void setupPlanetAnimations(Pane animationPane, List<Circle> planets, List<Animation> allAnimations) {
        for (Animation animation : allAnimations) {
            animation.stop();
        }
        allAnimations.clear();
        for (Circle planet : planets) {
            RotateTransition rt = createRotateTransition(planet, calculateRotationDuration(planet));
            planetAnimations.add(rt);
            rt.play();
        }
    }

     /**
     * Creates a rotate transition for a node with a specified duration.
     * @param node The node to apply the rotation to.
     * @param duration The duration of the rotation in seconds.
     * @return A RotateTransition object configured with the specified node and duration.
     */
    private RotateTransition createRotateTransition(Node node, double duration) {
        RotateTransition rt = new RotateTransition(Duration.seconds(duration), node);
        rt.setByAngle(-360);
        rt.setCycleCount(RotateTransition.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        return rt;
    }

    /**
     * Calculates the duration of rotation for a planet based on its identifier.
     * @param planet A Circle object representing the planet.
     * @return The duration in seconds for one complete rotation of the planet.
     */
    private double calculateRotationDuration(Circle planet) {
        String planetId = planet.getId();
        return switch (planetId) {

            case "Moon" ->
                4;

            case "Mercury", "Venus", "Uranus", "Neptune", "Mars", "Earth", "Jupiter", "Saturn" ->
                7;
            case "Sun" ->
                4;
            default ->
                15; 
        };
    }

}
