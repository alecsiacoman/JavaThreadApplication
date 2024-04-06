package Model;

public class Task {
    private int ID;
    private int arrivalTime;
    private int serviceTime;

    public Task(int ID, int arrivalTime, int serviceTime){
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getId() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    @Override
    public String toString(){
        return "(" + ID + " " + arrivalTime + " " + serviceTime + "); ";
    }
}
