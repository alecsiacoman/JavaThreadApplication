package Model;

import com.example.Controller.SimulationManager;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int currentTime;

    public Server(){
        tasks = new LinkedBlockingDeque<>();
        waitingPeriod = new AtomicInteger(0);
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void run() {
        while(true){
            try {
                Task task = tasks.peek();
                if (task != null && (task.getArrivalTime() + task.getServiceTime() == currentTime)) {
                    tasks.poll();
                    waitingPeriod.decrementAndGet();
                    Thread.sleep( 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTask(Task task){
        tasks.offer(task);
        waitingPeriod.addAndGet(task.getServiceTime());
    }

    public Task[] getTasks(){
        return tasks.toArray(new Task[0]);
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public int getQueueSize(){
        return tasks.size();
    }
}
