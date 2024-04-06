package com.example.Controller;

import Model.Scheduler;
import Model.Task;
import View.SimulationFrame;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable{
    public int timeLimit;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int maxServiceTime;
    public int minServiceTime;
    public int numberOfServers;
    public int numberOfClients;
    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> tasks;

    public SimulationManager(int timeLimit, int maxArrivalTime, int minArrivalTime, int maxServiceTime, int minServiceTime int numberOfServers, int numberOfClients, SimulationFrame frame){
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxServiceTime = maxServiceTime;
        this.minServiceTime = minServiceTime;
        this.maxArrivalTime = maxArrivalTime;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.frame = frame;
        this.tasks = new ArrayList<>();
        this.scheduler = new Scheduler(numberOfServers, 10);
    }

    public void generateRandomTasks(){
        Random random = new Random();
        for(int i = 0; i < numberOfClients; i++){
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            tasks.add(new Task(i + 1, arrivalTime, serviceTime));
        }
    }

    @Override
    public void run() {
        int currentTime = 0;
        while(currentTime < timeLimit){
            generateRandomTasks();
            for(Task task: tasks){
                scheduler.dispatchTask(task);
            }
            tasks.clear();
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            currentTime++;
        }
    }
}