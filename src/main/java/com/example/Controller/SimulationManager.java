package com.example.Controller;

import Model.Scheduler;
import Model.SelectionPolicy;
import Model.Server;
import Model.Task;
import View.SimulationFrame;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class SimulationManager implements Runnable{
    public int timeLimit;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int maxServiceTime;
    public int minServiceTime;
    public int numberOfServers;
    public int numberOfClients;
    private Scheduler scheduler;
    private SelectionPolicy selectionPolicy;
    private SimulationFrame frame;
    private List<Task> tasks;
    private Server[] servers;

    public SimulationManager(int timeLimit, int maxArrivalTime, int minArrivalTime, int maxServiceTime, int minServiceTime, int numberOfServers, int numberOfClients, SimulationFrame frame, SelectionPolicy selectionPolicy){
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxServiceTime = maxServiceTime;
        this.minServiceTime = minServiceTime;
        this.maxArrivalTime = maxArrivalTime;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.frame = frame;
        this.tasks = new ArrayList<>();
        this.selectionPolicy = selectionPolicy;
        this.scheduler = new Scheduler(numberOfServers, 10);
        scheduler.changeStrategy(selectionPolicy);
        this.servers = new Server[numberOfServers];
        for(int i = 0; i < numberOfServers; i++)
            servers[i] = new Server();
        generateRandomTasks();
    }

    public void generateRandomTasks(){
        Random random = new Random();
        for(int i = 0; i < numberOfClients; i++){
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            tasks.add(new Task(i + 1, arrivalTime, serviceTime));
        }
        Collections.sort(tasks, Comparator.comparing(Task::getArrivalTime));
    }

    @Override
    public void run() {
        int currentTime = 0;
        try(FileWriter writer = new FileWriter("logs.txt")){
            while(currentTime < timeLimit){
                Iterator<Task> iterator = tasks.iterator();
                while (iterator.hasNext()){
                    Task task = iterator.next();
                    if (task.getArrivalTime() == currentTime){
                        scheduler.dispatchTask(task);
                        iterator.remove();
                    }
                }
                String entry = generateLog(currentTime);
                writer.write(entry + "\n");
                System.out.println(entry);
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                currentTime++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String generateLog(int currentTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nTime ").append(currentTime).append("\n");
        sb.append("Waiting clients: ");
        for(Task task : tasks){
            sb.append(task.toString());
        }
        int i = 0;
        for(Server item : scheduler.getServers()){
            sb.append("\nQueue ").append(i + 1).append(": ");
            if(item.getQueueSize() != 0){
                Task[] tasksArray = item.getTasks();
                for (Task task : tasksArray) {
                    sb.append(task.toString());
                }
            } else sb.append("closed");
        }
        return sb.toString();
    }
}