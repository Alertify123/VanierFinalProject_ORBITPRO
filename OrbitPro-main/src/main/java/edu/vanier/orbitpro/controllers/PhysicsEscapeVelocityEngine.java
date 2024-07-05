/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.orbitpro.controllers;

import javafx.scene.paint.Color;

/**
 * This class calculates escape velocity and provides information about
 * various celestial bodies' properties such as mass, radius, gravity, and color.
 * 
 * @author bidid
 */
public class PhysicsEscapeVelocityEngine {

    private static final double G = 6.674 * Math.pow(10, -11);

    private double mass;
    private double radius;
    private Color planetColor;
    private double gravity;

    /**
     * Sets ,through a switch case, the mass, radius, gravity, and color of the selected planet that is 
     * to be used for the animation and other visual elements.
     * 
     * @param planet The name of the planet as a string.
     */
    public void setPlanet(String planet) {
        switch (planet) {
            case "Mercury" -> {
                mass = 3.285 * Math.pow(10, 23);
                radius = 2.4397 * Math.pow(10, 3);
                gravity = 3.7;
                planetColor = Color.GRAY;
            }
            case "Venus" -> {
                mass = 4.867 * Math.pow(10, 24);
                radius = 6.0518 * Math.pow(10, 3);
                gravity = 8.87;
                planetColor = Color.YELLOW;
            }
            case "Earth" -> {
                mass = 5.9723 * Math.pow(10, 24);
                radius = 6.371 * Math.pow(10, 3);
                gravity = 9.81;
                planetColor = Color.BLUE;
            }
            case "Mars" -> {
                mass = 6.4171 * Math.pow(10, 23);
                radius = 3.3895 * Math.pow(10, 3);
                gravity = 3.721;
                planetColor = Color.RED;
            }
            case "Jupiter" -> {
                mass = 1.8982 * Math.pow(10, 27);
                radius = 6.9911 * Math.pow(10, 4);
                gravity = 24.79;
                planetColor = Color.ORANGE;
            }
            case "Saturn" -> {
                mass = 5.6834 * Math.pow(10, 26);
                radius = 5.8232 * Math.pow(10, 4);
                gravity = 10.44;
                planetColor = Color.BISQUE;
            }
            case "Uranus" -> {
                mass = 8.6810 * Math.pow(10, 25);
                radius = 2.5362 * Math.pow(10, 4);
                gravity = 8.87;
                planetColor = Color.AQUAMARINE;
            }
            case "Neptune" -> {
                mass = 1.02413 * Math.pow(10, 26);
                radius = 2.4622 * Math.pow(10, 4);
                gravity = 11.15;
                planetColor = Color.CORNFLOWERBLUE;
            }
            default ->
                throw new IllegalArgumentException("Unknown planet: " + planet);
        }
    }

    /**
     * Calculates the escape velocity of the selected planet.
     * 
     * @return The escape velocity in kilometers per second.
     */
    public double calculateEscapeVelocity() {
        return (Math.sqrt((2 * G * mass) / (radius * 1000))) / 1000;
    }

    /**
     * Gets the gravity of the selected planet.
     * 
     * @return The gravity in meters per second squared.
     */
    public double getGravity() {
        return gravity;
    }

    /**
     * Gets the radius of the selected planet.
     * 
     * @return The radius in kilometers.
     */
    public double getRadius() {
        return radius;
    }
    
    /**
     * Gets the mass of the selected planet.
     * 
     * @return The mass in kilograms.
     */
    public double getMass() {
        return mass;
    }
    
    /**
     * Gets the color representing the selected planet.
     * 
     * @return The Color object representing the planet's color.
     */
    public Color getPlanetColor() {
        return planetColor;
    }
}
