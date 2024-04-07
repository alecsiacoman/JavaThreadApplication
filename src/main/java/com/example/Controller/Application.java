package com.example.Controller;

import Model.SelectionPolicy;
import View.SimulationFrame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;

public class Application extends javafx.application.Application {

    private Boolean okData;
    private int simulationInterval = -1;
    private int maxArrivalTime = -1;
    private int minArrivalTime = -1;
    private int maxServiceTime = -1;
    private int minServiceTime = -1;
    private int activeQueues = -1;
    private int numberOfClients = -1;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("simulation-view.fxml"));
        SimulationFrame controller = new SimulationFrame();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 900, 565);
        stage.setTitle("Queue Management Simulation");
        stage.setScene(scene);
        stage.show();
        okData = false;
        setButtons(controller);
    }

    private void setButtons(SimulationFrame controller){
        controller.getBtnValidateData().setOnAction(event -> {
            okData = true;
            getData(controller);
            verifyData();
            if(okData == true)
                controller.setLblValidateData("VALID data!");
            else {
                controller.setLblValidateData("INVALID data!");
            }
        });

        controller.getBtnStartSimulation().setOnAction(event -> {
            if(okData == true){
                controller.setLblValidateData("");
                startSimulation(simulationInterval, maxArrivalTime, minArrivalTime, maxServiceTime, minServiceTime, activeQueues, numberOfClients, controller, SelectionPolicy.SHORTEST_QUEUE);
            } else { controller.setLblValidateData("Cannot start the simulation until the data is valid!"); }
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

    private void verifyData() {
        if(simulationInterval <= 0)
            okData = false;
        if(minArrivalTime >= maxArrivalTime || minArrivalTime <= 0 || maxArrivalTime <= 0)
            okData = false;
        if(minServiceTime >= maxServiceTime || minServiceTime <= 0 || maxServiceTime <= 0)
            okData = false;
        if(activeQueues <= 0 || activeQueues > 5)
            okData = false;
        if(numberOfClients > 10 || numberOfClients <= 0)
            okData = false;
    }

    private void startSimulation(int simulationInterval, int maxArrivalTime, int minArrivalTime, int maxServiceTime, int minServiceTime, int activeQueues, int numberOfClients, SimulationFrame controller, SelectionPolicy selectionPolicy){
        SimulationManager manager = new SimulationManager(simulationInterval,
                maxArrivalTime, minArrivalTime,
                maxServiceTime, minServiceTime,
                activeQueues, numberOfClients, controller,
                selectionPolicy);

        Thread simulationThread = new Thread(manager);
        simulationThread.start();
    }
    
    public static void main(String[] args) {
        launch();
    }
}
