package Model;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server(){
        tasks = new LinkedBlockingDeque<>();
        waitingPeriod = new AtomicInteger(0);
    }

    @Override
    public void run() {
        while(true){
            try{
                Task task = tasks.take();
                Thread.sleep(task.getServiceTime() * 1000);
                waitingPeriod.decrementAndGet();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void addTask(Task task){
        //add task to queue
        //increment the waiting Period
        tasks.offer(task);
        waitingPeriod.incrementAndGet();
    }

    public Task[] getTasks(){
        return tasks.toArray(new Task[0]);
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
}
