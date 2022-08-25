package in.amtron.zoooperator.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Payment {

    @ColumnInfo
    @PrimaryKey(autoGenerate=true)
    Long id;

    Long bookingId;
    String orderId;
    String response;
    String txnId;

    public Payment(Long bookingId, String orderId) {
        this.bookingId = bookingId;
        this.orderId = orderId;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getResponse() {
        return response;
    }

    public String getTxnId() {
        return txnId;
    }


}
