package com.example.Controller;

import Model.Scheduler;
import Model.SelectionPolicy;
import Model.Server;
import Model.Task;
import View.SimulationFrame;
import javafx.application.Platform;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Math.abs;

public class SimulationManager implements Runnable{
    public int timeLimit;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int maxServiceTime;
    public int minServiceTime;
    public int numberOfServers;
    public int numberOfClients;
    private final Scheduler scheduler;
    private final SimulationFrame frame;
    private volatile ConcurrentLinkedQueue<Task> tasks;
    private final Object lock = new Object();
    private Map<Integer, Integer> hourlyArrivals = new HashMap<>();
    private int averageWaitingTime;
    private int averageServiceTime;

    public SimulationManager(int timeLimit, int maxArrivalTime, int minArrivalTime, int maxServiceTime, int minServiceTime, int numberOfServers, int numberOfClients, SimulationFrame frame, SelectionPolicy selectionPolicy){
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxServiceTime = maxServiceTime;
        this.minServiceTime = minServiceTime;
        this.maxArrivalTime = maxArrivalTime;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.frame = frame;
        List<Task> list = generateRandomTasks();
        this.tasks = new ConcurrentLinkedQueue<>(list);
        computeResultTime();
        this.scheduler = new Scheduler(numberOfServers, 10);
        scheduler.changeStrategy(selectionPolicy);

    }

    public List<Task> generateRandomTasks() {
        List<Task> tasks = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfClients; i++) {
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            tasks.add(new Task(i + 1, arrivalTime, serviceTime));
        }
        Collections.sort(tasks, Comparator.comparing(Task::getArrivalTime));
        return tasks;
    }

    @Override
    public void run() {
        int currentTime = 0;
        Object lock = new Object();
        try(FileWriter writer = new FileWriter("logs.txt")){
            while(currentTime < timeLimit){
                if(Application.getActiveQueues() <= 5)
                    updateWaitingClients(this, frame, currentTime);
                if(Application.getActiveQueues() <= 5)
                    updateServerQueues(frame);
                for (Server server : scheduler.getServers()) {
                    server.setCurrentTime(currentTime);
                }
                synchronized (tasks){
                    Iterator<Task> iterator = tasks.iterator();
                    while (iterator.hasNext()){
                        Task task = iterator.next();
                        if (task.getArrivalTime() == currentTime){
                            scheduler.dispatchTask(task);
                            iterator.remove();
                            hourlyArrivals.put(currentTime, hourlyArrivals.getOrDefault(currentTime, 0) + 1);
                        }
                    }
                }
                String entry = generateLog(currentTime);
                writer.write(entry + "\n");
                System.out.println(entry);
                currentTime++;
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(simulationEnded(currentTime)){
                    if(Application.getActiveQueues() <= 5)
                        updateServerQueues(frame);
                    entry = generateLog(currentTime);
                    writer.write(entry + "\nSIMULATION ENDED!");
                    String text = generateResults();
                    writer.write(text);
                    System.out.println(entry);
                    System.out.println("SIMULATION ENDED!");
                    break;
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //GUI
    private void updateWaitingClients(SimulationManager manager, SimulationFrame frame, int currentTime) {
        Platform.runLater(() -> {
            frame.setLblTimer(currentTime);
            frame.clearWaitingClientList(frame.getvBoxList().get(5));
            synchronized (tasks){
                for(Task task: manager.getTasks()){
                    frame.addClientToVBox(frame.getvBoxClients(), task);
                }
            }
        });
    }

    private void updateServerQueues(SimulationFrame frame) {
        for (int i = 0; i < scheduler.getServers().size(); i++) {
            final int index = i;
            Platform.runLater(() -> {
                frame.clearWaitingClientList(frame.getvBoxList().get(index));
                Server server = scheduler.getServers().get(index);
                Task[] tasksArray = server.getTasks();
                for (Task task : tasksArray) {
                    frame.addClientToVBox(frame.getvBoxList().get(index), task);
                }
            });
        }
    }

    private boolean simulationEnded(int currentTime) {
        boolean allServersClosed = true;
        boolean clientsWaiting;
        synchronized (tasks){
            clientsWaiting = !tasks.isEmpty();
        }
        if (!clientsWaiting) {
            for (Server server : scheduler.getServers()) {
                if (server.getQueueSize() != 0) {
                    allServersClosed = false;
                    break;
                }
            }
        } else {
            allServersClosed = false;
        }

        return !clientsWaiting && allServersClosed;
    }

    private String generateLog(int currentTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nTime ").append(currentTime).append("\n");
        sb.append("Waiting clients: ");
        synchronized (tasks){
            for(Task task : tasks){
                sb.append(task.toString());
            }
        }
        for (int i = 0; i < scheduler.getServers().size(); i++) {
            sb.append("\nQueue ").append(i + 1).append(": ");
            Server server = scheduler.getServers().get(i);
            Task[] tasksArray = server.getTasks();
            if (server.getQueueSize() != 0) {
                for (Task task : tasksArray) {
                    sb.append(task.toString());
                }
            } else {
                sb.append("closed");
            }
        }
        return sb.toString();
    }

    private void computeResultTime(){
        int totalWaitingTime = 0, totalServiceTime = 0;
        for(Task task : tasks){
            totalWaitingTime += abs(task.getArrivalTime() - task.getServiceTime());
            totalServiceTime += task.getServiceTime();
        }
        averageWaitingTime = totalWaitingTime / Application.getNumberOfClients();
        averageServiceTime = totalServiceTime / Application.getNumberOfClients();
    }
    private String generateResults(){
        int peakHour = -1;
        int maxArrivals = 0;
        for (Map.Entry<Integer, Integer> entry : hourlyArrivals.entrySet()) {
            if (entry.getValue() > maxArrivals) {
                maxArrivals = entry.getValue();
                peakHour = entry.getKey();
            }
        }
        String text = "\n\nSIMULATIONS DETAILS:\nAverage waiting time: " + averageWaitingTime + "\nAverage service time: " + averageServiceTime + "\nPeak hour: " + peakHour + "\n";
        return text;
    }

    public ConcurrentLinkedQueue<Task> getTasks() {
        return tasks;
    }
}