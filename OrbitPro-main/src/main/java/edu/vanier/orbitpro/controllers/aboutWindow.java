/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.orbitpro.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;

/**
 *
 * @author bidid
 */
public class aboutWindow {

    public void display() {
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Escape Velocity");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Escape Velocities of Planets");
        xAxis.setLabel("Planets");
        yAxis.setLabel("Escape Velocity (km/s)");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(createData("Mercury", 4.3, "gray"));
        series.getData().add(createData("Mars", 5, "red"));
        series.getData().add(createData("Venus", 10.4, "peachpuff"));
        series.getData().add(createData("Earth", 11.2, "blue"));
        series.getData().add(createData("Uranus", 21.3, "cyan"));
        series.getData().add(createData("Neptune", 23.5, "blue"));
        series.getData().add(createData("Saturn", 35.5, "khaki"));
        series.getData().add(createData("Jupiter", 59.5, "orange"));

        barChart.getData().add(series);

        Label equation = new Label("The formula for escape velocity is given by: ve = sqrt((2*G*M)/r)");
        Label values = new Label("where:\n"
                + "- ve is the escape velocity,\n"
                + "- G is the gravitational constant,\n"
                + "- M is the mass of the celestial body (planet in this case), and\n"
                + "- r is the radius of the celestial body from its center to the point of escape.");

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(barChart, equation, values);

        Scene scene1 = new Scene(layout, 800, 600);

        popupWindow.setScene(scene1);

        popupWindow.showAndWait();
    }

    private XYChart.Data<String, Number> createData(String planet, double velocity, String color) {
        XYChart.Data<String, Number> data = new XYChart.Data<>(planet, velocity);
        data.nodeProperty().addListener((observable, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-bar-fill: " + color + ";");
                final Text text = new Text(velocity + " km/s");
                text.setStyle("-fx-font-size: 14px;");
                text.setTranslateY(-20);
                ((StackPane) newNode).getChildren().add(text);
            }
        });
        return data;
    }
}
