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
    public volatile int timeLimit;
    public volatile int maxArrivalTime;
    public volatile int minArrivalTime;
    public volatile int maxServiceTime;
    public volatile int minServiceTime;
    public static int numberOfServers;
    public static int numberOfClients;
    public volatile String strategy;
    private int averageWaitingTime;
    private int averageServiceTime;
    private final Scheduler scheduler;
    private final SimulationFrame frame;
    private volatile ConcurrentLinkedQueue<Task> tasks;
    private Map<Integer, Integer> hourlyArrivals = new HashMap<>();
    private volatile Boolean okData = false;
    public volatile static int totalWaitingTime = 0;

    public SimulationManager(SimulationFrame frame){
        this.frame = frame;
        setData(frame);
        List<Task> list = generateRandomTasks();
        this.tasks = new ConcurrentLinkedQueue<>(list);
        computeResultTime();
        this.scheduler = new Scheduler(numberOfServers, 200);
        if(strategy.equals("Strategy QUEUE"))
            scheduler.changeStrategy(SelectionPolicy.SHORTEST_QUEUE);
        else
            scheduler.changeStrategy(SelectionPolicy.SHORTEST_TIME);
    }

    private synchronized void setData(SimulationFrame controller){
        this.timeLimit = controller.getSimulationInterval();
        this.maxArrivalTime = controller.getMaximumArrivalTime();
        this.minArrivalTime = controller.getMinimumArrivalTime();
        this.maxServiceTime = controller.getMaximumServiceTime();
        this.minServiceTime = controller.getMinimumServiceTime();
        this.numberOfServers = controller.getActiveQueues();
        this.numberOfClients = controller.getNumberOfClients();
        this.strategy = controller.getStrategy();
    }

    private synchronized void verifyData() {
        if(timeLimit <= 0)
            okData = false;
        if(minArrivalTime >= timeLimit || maxArrivalTime >= timeLimit)
            okData = false;
        if(minServiceTime >= timeLimit || maxServiceTime >= timeLimit)
            okData = false;
        if(minArrivalTime >= maxArrivalTime || minArrivalTime <= 0 || maxArrivalTime <= 0)
            okData = false;
        if(minServiceTime >= maxServiceTime || minServiceTime <= 0 || maxServiceTime <= 0)
            okData = false;
        if(numberOfServers <= 0)
            okData = false;
        if(numberOfClients <= 0)
            okData = false;
    }

    public void validateData(SimulationFrame controller){
        okData = true;
        setData(controller);
        verifyData();
        if(okData == true)
            controller.setLblValidateData("VALID data!");
        else {
            controller.setLblValidateData("INVALID data!");
        }
    }

    public void startSimulation(SimulationFrame controller){
        if(okData == true){
            if(numberOfClients <= 5)
                controller.setLblValidateData("");
            else
                controller.setLblValidateData("Cannot display the simulation! See logs!");
            Application.startSimulation(controller);
        } else { controller.setLblValidateData("Cannot start the simulation until the data is valid!"); }

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
        try(FileWriter writer = new FileWriter("logs.txt")){
            while(currentTime < timeLimit){
                if(getActiveQueues() <= 5){
                    updateWaitingClients(this, frame, currentTime);
                    updateServerQueues(frame);
                }
                for (Server server : scheduler.getServers()) {
                    server.setCurrentTime(currentTime);
                }
                processTasks(currentTime);
                String entry = generateLog(currentTime);
                writer.write(entry + "\n");
                System.out.println(entry);
                currentTime++;
                Thread.sleep(1000);
                if(simulationEnded(currentTime)){
                    handleSimulationEnd(currentTime, writer);
                    break;
                }
            }
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private void processTasks(int currentTime){
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
    }

    private void handleSimulationEnd(int currentTime, FileWriter writer) throws IOException {
        averageWaitingTime = totalWaitingTime / getNumberOfClients();
        updateServerQueues(frame);
        String entry = generateLog(currentTime);
        writer.write(entry + "\nSIMULATION ENDED!");
        writer.write(generateResults());
        System.out.println(entry);
        System.out.println("SIMULATION ENDED!");
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
        if(currentTime == timeLimit){
            return true;
        }
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
       // int totalWaitingTime = 0, totalServiceTime = 0;
        int totalServiceTime = 0;
        for(Task task : tasks){
            //totalWaitingTime += abs(task.getArrivalTime() - task.getServiceTime());
            totalServiceTime += task.getServiceTime();
        }

        averageServiceTime = totalServiceTime / getNumberOfClients();
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

    public static int getActiveQueues() {
        return numberOfServers;
    }

    public static int getNumberOfClients() {
        return numberOfClients;
    }
}