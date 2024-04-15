package com.example.Controller;

import Model.SelectionPolicy;
import View.SimulationFrame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("simulation-view.fxml"));
        SimulationFrame controller = new SimulationFrame();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 990, 520);
        stage.setTitle("Queue Management Simulation");
        stage.setScene(scene);
        stage.show();
        SimulationManager manager = new SimulationManager(controller);
        controller.setSimulationManager(manager);
    }

    public synchronized static void startSimulation(SimulationFrame controller){
        SimulationManager manager = new SimulationManager(controller);
        Thread simulationThread = new Thread(manager);
        simulationThread.start();
    }
}
