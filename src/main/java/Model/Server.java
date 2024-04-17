package Model;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int currentTime;
    private volatile Boolean notDone;

    public Server(){
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
        notDone = true;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void run() {
        while(notDone){
            try {
                Task task = tasks.peek();
                if (task != null){
                    task.decrementServiceTime();
                    waitingPeriod.decrementAndGet();
                    if(task.getServiceTime() == 0) {
                        synchronized (this) {
                            tasks.poll();
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addTask(Task task){
        tasks.offer(task);
        waitingPeriod.addAndGet(task.getServiceTime());
    }

    public Task[] getTasks(){
        return tasks.toArray(new Task[0]);
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public synchronized int getQueueSize(){
        return tasks.size();
    }
}
