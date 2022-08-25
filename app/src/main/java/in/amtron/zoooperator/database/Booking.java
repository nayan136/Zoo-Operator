package in.amtron.zoooperator.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Booking {

    @ColumnInfo
    @PrimaryKey(autoGenerate=true)
    Long id;

    String ticketNo;
    String date;
    String mobile;
    int indianAdults;
    int indianChildrens;
    int indianStudents;
    int indianStillCameras;
    int indianVideoCameras;
    int indianSlrCameras;
    int foreignAdults;
    int foreignChildrens;
    int foreignStudents;
    int foreignStillCameras;
    int foreignVideoCameras;
    int foreignSlrCameras;
    double price;
    String pos_txn;
//    double ipg_percent;
    double ipg_amount;
    double gst_amount;
    double service_chg;
    double payableAmount;

    String orderId;
    String response;

    int server;
    int payment;

    public Booking() {
    }

    public Long getTableId(){
        return id;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setIndianAdults(int indianAdults) {
        this.indianAdults = indianAdults;
    }

    public void setIndianChildrens(int indianChildrens) {
        this.indianChildrens = indianChildrens;
    }

    public void setIndianStudents(int indianStudents) {
        this.indianStudents = indianStudents;
    }

    public void setIndianStillCameras(int indianStillCameras) {
        this.indianStillCameras = indianStillCameras;
    }

    public void setIndianVideoCameras(int indianVideoCameras) {
        this.indianVideoCameras = indianVideoCameras;
    }

    public void setIndianSlrCameras(int indianSlrCameras) {
        this.indianSlrCameras = indianSlrCameras;
    }

    public void setForeignAdults(int foreignAdults) {
        this.foreignAdults = foreignAdults;
    }

    public void setForeignChildrens(int foreignChildrens) {
        this.foreignChildrens = foreignChildrens;
    }

    public void setForeignStudents(int foreignStudents) {
        this.foreignStudents = foreignStudents;
    }

    public void setForeignStillCameras(int foreignStillCameras) {
        this.foreignStillCameras = foreignStillCameras;
    }

    public void setForeignVideoCameras(int foreignVideoCameras) {
        this.foreignVideoCameras = foreignVideoCameras;
    }

    public void setForeignSlrCameras(int foreignSlrCameras) {
        this.foreignSlrCameras = foreignSlrCameras;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPos_txn(String pos_txn) {
        this.pos_txn = pos_txn;
    }

//    public void setIpg_percent(double ipg_percent) {
//        this.ipg_percent = ipg_percent;
//    }

    public void setIpg_amount(double ipg_amount) {
        this.ipg_amount = ipg_amount;
    }

    public void setGst_amount(double gst_amount) {
        this.gst_amount = gst_amount;
    }

    public void setService_chg(double service_chg) {
        this.service_chg = service_chg;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public String getDate() {
        return date;
    }

    public String getMobile() {
        return mobile;
    }

    public int getIndianAdults() {
        return indianAdults;
    }

    public int getIndianChildrens() {
        return indianChildrens;
    }

    public int getIndianStudents() {
        return indianStudents;
    }

    public int getIndianStillCameras() {
        return indianStillCameras;
    }

    public int getIndianVideoCameras() {
        return indianVideoCameras;
    }

    public int getIndianSlrCameras() {
        return indianSlrCameras;
    }

    public int getForeignAdults() {
        return foreignAdults;
    }

    public int getForeignChildrens() {
        return foreignChildrens;
    }

    public int getForeignStudents() {
        return foreignStudents;
    }

    public int getForeignStillCameras() {
        return foreignStillCameras;
    }

    public int getForeignVideoCameras() {
        return foreignVideoCameras;
    }

    public int getForeignSlrCameras() {
        return foreignSlrCameras;
    }

    public double getPrice() {
        return price;
    }

    public String getPos_txn() {
        return pos_txn;
    }

//    public double getIpg_percent() {
//        return ipg_percent;
//    }

    public double getIpg_amount() {
        return ipg_amount;
    }

    public double getGst_amount() {
        return gst_amount;
    }

    public double getService_chg() {
        return service_chg;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getServer() {
        return server;
    }

    public int getPayment() {
        return payment;
    }

    public double getTotalServiceCharge(){
//        return  ipg_amount+service_chg+gst_amount;
        return payableAmount - price;
    }

    public Boolean hasIndian(){
        return (indianAdults+indianChildrens+indianStudents+indianStillCameras+indianSlrCameras+indianVideoCameras) > 0;
    }

    public Boolean hasForeigner(){
        return (foreignAdults+foreignChildrens+foreignStudents+foreignStillCameras+foreignSlrCameras+foreignVideoCameras) > 0;
    }
}
