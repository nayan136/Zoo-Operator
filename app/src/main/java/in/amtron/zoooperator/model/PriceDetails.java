package in.amtron.zoooperator.model;

import com.google.gson.annotations.SerializedName;

public class PriceDetails {

    @SerializedName("TYPE")
    private String type;
    @SerializedName("ADULT")
    private double adultPrice;
    @SerializedName("CHILD")
    private double childrenPrice;
    @SerializedName("STUDENT")
    private double studentPrice;
    @SerializedName("STILL_CAMERA")
    private double stillPrice;
    @SerializedName("SLR_CAMERA")
    private double dslrPrice;
    @SerializedName("VIDEO_CAMERA")
    private double videoPrice;

    public PriceDetails(String type, double adultPrice, double childrenPrice, double studentPrice, double stillPrice, double dslrPrice, double videoPrice) {
        this.type = type;
        this.adultPrice = adultPrice;
        this.childrenPrice = childrenPrice;
        this.studentPrice = studentPrice;
        this.stillPrice = stillPrice;
        this.dslrPrice = dslrPrice;
        this.videoPrice = videoPrice;
    }

    public String getType() {
        return type;
    }

    public double getAdultPrice() {
        return adultPrice;
    }

    public double getChildrenPrice() {
        return childrenPrice;
    }

    public double getStudentPrice() {
        return studentPrice;
    }

    public double getStillPrice() {
        return stillPrice;
    }

    public double getDslrPrice() {
        return dslrPrice;
    }

    public double getVideoPrice() {
        return videoPrice;
    }
}
