package in.amtron.zoooperator.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicePriceDetails {

    private List<PriceDetails> price;
    @SerializedName("service_chg")
    private double serviceCharge;
    @SerializedName("ipg_percent")
    private double ipgPercent;
    @SerializedName("gst_amount")
    private double gstAmount;

    public ServicePriceDetails(List<PriceDetails> price, double serviceCharge, double ipgPercent, double gstAmount) {
        this.price = price;
        this.serviceCharge = serviceCharge;
        this.ipgPercent = ipgPercent;
        this.gstAmount = gstAmount;
    }

    public List<PriceDetails> getPrice() {
        return price;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public double getIpgPercent() {
        return ipgPercent;
    }

    public double getGstAmount() {
        return gstAmount;
    }
}
