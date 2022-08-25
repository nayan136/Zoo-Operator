package in.amtron.zoooperator.model;

public class TodayBookingDetails {

    private int count;
    private int visitors;
    private double amount;

    public TodayBookingDetails(int count, int visitors, double amount) {
        this.count = count;
        this.visitors = visitors;
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public int getVisitors() {
        return visitors;
    }

    public double getAmount() {
        return amount;
    }
}
