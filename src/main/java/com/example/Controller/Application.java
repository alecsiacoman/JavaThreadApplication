package com.example.Controller;

import View.SimulationFrame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("simulation-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 570);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        SimulationFrame frame = fxmlLoader.getController();
        SimulationManager manager = new SimulationManager(frame.getSimulationInterval(),
                                    frame.getMaximumArrivalTime(),
                                    frame.getMinimumArrivalTime(),
                                    frame.getMaximumServiceTime(),
                                    frame.getMinimumServiceTime(),
                                    frame.getActiveQueues(),
                                    frame.getNumberOfClients(), frame);
        Thread simulationThread = new Thread(manager);
        simulationThread.start();
    }

    public static void main(String[] args) {
        launch();
    }
}