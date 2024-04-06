package com.example.Controller;

import Model.Scheduler;
import Model.Task;
import View.SimulationFrame;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class SimulationManager implements Runnable{
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> tasks;

    public SimulationManager(){

    }

    public void generateRandomTasks(){

    }

    @Override
    public void run() {

    }
}