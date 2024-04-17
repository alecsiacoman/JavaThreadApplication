package Model;

import com.example.Controller.SimulationManager;

import java.util.List;

public class ConcreteStrategyTime implements Strategy{

    @Override
    public void addTask(List<Server> servers, Task task, int maxTasks) {
       synchronized (servers){
           Server server = servers.get(0);
           int shortest = server.getWaitingPeriod().get();

           for(Server item: servers){
               int time = item.getWaitingPeriod().get();
               if(time < shortest && server.getTasks().length < maxTasks){
                   server = item;
                   shortest = time;
               }
           }
           synchronized (server){
               //add task to the server with the shortest processing time
               server.addTask(task);
               SimulationManager.totalWaitingTime += server.getWaitingPeriod().get();
           }
       }
    }
}
