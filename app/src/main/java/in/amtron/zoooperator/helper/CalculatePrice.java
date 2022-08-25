package in.amtron.zoooperator.helper;

import in.amtron.zoooperator.model.ServicePriceDetails;

public class CalculatePrice {

    // without any charges like gst, service charge
    private double amount;
    private int totalVisitors;
    private ServicePriceDetails servicePriceDetails;
    private double serviceCharge;
    private double ipgAmount;
    private double gstAmount;
    private double totalAmount;

//    public CalculatePrice(int amount, int totalVisitors, ServicePriceDetails servicePriceDetails) {
//        this.amount = amount;
//        this.totalVisitors = totalVisitors;
//        this.servicePriceDetails = servicePriceDetails;
//
//        calculateServiceCharge();
//        calculateIpgAmount();
//        calculateGstAmount();
//    }

    public CalculatePrice(double amount, int totalVisitors, ServicePriceDetails servicePriceDetails) {
        this.amount = amount;
        this.totalVisitors = totalVisitors;
        this.servicePriceDetails = servicePriceDetails;

        calculateServiceCharge();
        calculateIpgAmount();
        calculateGstAmount();
        calculateTotalAmount();
    }

    private void calculateServiceCharge(){
        serviceCharge = Util.money(totalVisitors*servicePriceDetails.getServiceCharge());
    }

    private void calculateIpgAmount(){
        ipgAmount = Util.money(amount*servicePriceDetails.getIpgPercent());
    }

    private void calculateGstAmount(){
        gstAmount = Util.money(serviceCharge*servicePriceDetails.getGstAmount());
    }

    private void calculateTotalAmount(){
        totalAmount = Math.ceil(amount+serviceCharge+ipgAmount+gstAmount);
        totalAmount = Util.money(totalAmount);
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public double getIpgAmount() {
        return ipgAmount;
    }

    public double getGstAmount() {
        return gstAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
