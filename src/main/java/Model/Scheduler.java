package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServer;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServer, int maxTasksPerServer){
        this.maxNoServer = maxNoServer;
        this.maxTasksPerServer = maxTasksPerServer;
        servers = new CopyOnWriteArrayList<>();

        for(int i = 0; i < maxNoServer; i++){
            Server server = new Server();
            Thread thread = new Thread(server);
            thread.start();
            servers.add(server);
        }
    }

    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        if(policy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ConcreteStrategyTime();
    }

    public synchronized void dispatchTask(Task task){
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return Collections.unmodifiableList(servers);
    }
}
