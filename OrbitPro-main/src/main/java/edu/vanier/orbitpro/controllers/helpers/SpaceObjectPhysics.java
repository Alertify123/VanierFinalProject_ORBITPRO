/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.orbitpro.controllers.helpers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents the physics properties of a space object, including its movement
 * and boundary interactions. This class encapsulates the characteristics of
 * objects like asteroids, providing methods to manage their position, speed,
 * and direction.
 */
public class SpaceObjectPhysics {

    private Circle shape;
    private double directionX;
    private double directionY;
    private double speed;

    /**
     * Constructs a SpaceObjectPhysics instance with specified attributes.
     *
     * @param radius The radius of the circle representing the space object.
     * @param speed The speed at which the object moves.
     * @param directionX The horizontal direction component.
     * @param directionY The vertical direction component.
     */
    public SpaceObjectPhysics(double radius, double speed, double directionX, double directionY) {
        this.shape = new Circle(radius, Color.GRAY); // Set color and radius
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
    }

    /**
     * Returns the shape of the space object.
     *
     * @return The Circle object representing the space object.
     */
    public Circle getShape() {
        return this.shape;
    }

    /**
     * Returns the current speed of the space object.
     *
     * @return The speed of the object.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Returns the horizontal direction component of the object's movement.
     *
     * @return The horizontal direction component.
     */
    public double getDirectionX() {
        return directionX;
    }

    /**
     * Returns the vertical direction component of the object's movement.
     *
     * @return The vertical direction component.
     */
    public double getDirectionY() {
        return directionY;
    }

    /**
     * Sets the speed of the space object.
     *
     * @param speed The new speed to set.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Sets the horizontal direction component of the object's movement.
     *
     * @param directionX The new horizontal direction component to set.
     */
    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    /**
     * Updates the position of the space object based on its speed, direction,
     * and elapsed time.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    public void updatePosition(double deltaTime) {
        shape.setTranslateX(shape.getTranslateX() + speed * directionX * deltaTime);
        shape.setTranslateY(shape.getTranslateY() + speed * directionY * deltaTime);
    }

//    public boolean isOutOfBounds(double paneWidth, double paneHeight) {
//        return shape.getTranslateX() < -50 || shape.getTranslateX() > paneWidth + 50
//                || shape.getTranslateY() < -50 || shape.getTranslateY() > paneHeight + 50;
//    }
    
     /**
     * Determines if the space object is near any border of the pane.
     * 
     * @param paneWidth The width of the pane in which the object is contained.
     * @param paneHeight The height of the pane in which the object is contained.
     * @param margin The margin within which the object is considered to be near the border.
     * @return true if the object is near any border, false otherwise.
     */
    public boolean isNearBorder(double paneWidth, double paneHeight, double margin) {
        Circle shape = this.getShape();
        double x = shape.getTranslateX();
        double y = shape.getTranslateY();
        double radius = shape.getRadius();

       
        return x - radius < margin || x + radius > paneWidth - margin
                || y - radius < margin || y + radius > paneHeight - margin;
    }
}
