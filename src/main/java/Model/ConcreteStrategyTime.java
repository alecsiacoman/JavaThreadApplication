package Model;

import java.util.List;

public class ConcreteStrategyTime implements Strategy{

    @Override
    public void addTask(List<Server> servers, Task task) {
        Server server = servers.get(0);
        int shortest = server.getWaitingPeriod().get();

        for(Server item: servers){
            int time = item.getWaitingPeriod().get();
            if(time < shortest){
                server = item;
                shortest = time;
            }
        }

        //add task to the server with the shortest processing time
        server.addTask(task);
    }
}
