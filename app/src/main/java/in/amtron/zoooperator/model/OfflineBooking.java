package in.amtron.zoooperator.model;

import com.google.gson.JsonArray;

public class OfflineBooking {

    private String API_KEY;
    private String ip;
    private String os;
    private String imei;
    private String zooUserId;
    private JsonArray data;

    public OfflineBooking(String API_KEY, String ip, String os, String imei, String zooUserId, JsonArray data) {
        this.API_KEY = API_KEY;
        this.ip = ip;
        this.os = os;
        this.imei = imei;
        this.zooUserId = zooUserId;
        this.data = data;
    }
}
