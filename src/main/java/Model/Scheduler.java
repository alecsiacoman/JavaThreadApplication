package Model;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServer;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServer, int maxTasksPerServer){
        this.maxNoServer = maxNoServer;
        this.maxTasksPerServer = maxTasksPerServer;
        servers = new ArrayList<>();

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

    public void dispatchTask(Task task){
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }
}
