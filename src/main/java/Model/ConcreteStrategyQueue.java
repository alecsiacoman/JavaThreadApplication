package Model;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task, int maxTasks) {
        Server server = servers.get(0);
        int shortest = server.getTasks().length;

        for(Server item: servers){
            int length = item.getTasks().length;
            if(length < shortest && server.getTasks().length < maxTasks){
                server = item;
                shortest = shortest;
            }
        }

        //add task to the server with the shortest length
        server.addTask(task);
    }
}
