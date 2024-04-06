package com.example.Controller;

import View.SimulationFrame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private Boolean okData = true;
    private int simulationInterval;
    private int maxArrivalTime;
    private int minArrivalTime;
    private int maxServiceTime;
    private int minServiceTime;
    private int activeQueues;
    private int numberOfClients;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("simulation-view.fxml"));
        SimulationFrame controller = new SimulationFrame();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 1200, 570);
        stage.setTitle("Queue Management Simulation");
        stage.setScene(scene);
        stage.show();

        getData(controller);

        controller.getBtnValidateData().setOnAction(event -> {
            verifyData(controller);
        });

        controller.getBtnStartSimulation().setOnAction(event -> {
            if(okData == true)
                startSimulation(simulationInterval, maxArrivalTime, minArrivalTime, maxServiceTime, minServiceTime, activeQueues, numberOfClients, controller);
            else {
                showErrorDialog("Invalid Data!", "Please make sure to input valid data inside the text fields!");
            }
        });
    }

    private void getData(SimulationFrame controller){
        this.simulationInterval = controller.getSimulationInterval();
        this.maxArrivalTime = controller.getMaximumArrivalTime();
        this.minArrivalTime = controller.getMinimumArrivalTime();
        this.maxServiceTime = controller.getMaximumServiceTime();
        this.minServiceTime = controller.getMinimumServiceTime();
        this.activeQueues = controller.getActiveQueues();
        this.numberOfClients = controller.getNumberOfClients();
    }

    private void verifyData(SimulationFrame controller) {
        if(simulationInterval > 60 || simulationInterval < 0)
            okData = false;
        if(minArrivalTime >= maxArrivalTime)
            okData = false;
        if(minServiceTime >= maxServiceTime)
            okData = false;
        if(activeQueues < 0 || activeQueues > 5)
            okData = false;
        if(numberOfClients > 10 || numberOfClients < 0)
            okData = false;
    }

    private void startSimulation(int simulationInterval, int maxArrivalTime, int minArrivalTime, int maxServiceTime, int minServiceTime, int activeQueues, int numberOfClients, SimulationFrame controller){
        SimulationManager manager = new SimulationManager(simulationInterval,
                maxArrivalTime, minArrivalTime,
                maxServiceTime, minServiceTime,
                activeQueues, numberOfClients, controller);

        Thread simulationThread = new Thread(manager);
        simulationThread.start();
    }

    private void showErrorDialog(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch();
    }
}
