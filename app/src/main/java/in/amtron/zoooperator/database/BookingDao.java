package in.amtron.zoooperator.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import in.amtron.zoooperator.model.TodayBookingDetails;

@Dao
public interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addBooking(Booking booking);

    @Query("select * from booking order by id desc LIMIT :limit OFFSET :offset")
    List<Booking> getBookings(int limit, int offset);

    @Query("select * from booking where id = :id")
    Booking getBooking(Long id);

    @Query("select * from booking where ticketNo = :ticketNo")
    Booking getBooking(String ticketNo);

    @Query("select * from booking where server = 0 AND payment = 1")
    List<Booking> getOfflineBooking();

    @Query("select * from booking where server = 0 AND orderId IS NOT NULL AND payment = 1 LIMIT :limit")
    List<Booking> getOfflineBooking(int limit);

    @Query("select * from booking where orderId = :orderId")
    Booking getSimilarOrderId(String orderId);

    @Query("select COUNT(*) AS count, (SUM(indianAdults)+SUM(indianChildrens)+SUM(indianStudents)+SUM(foreignAdults)+SUM(foreignChildrens)+SUM(foreignStudents) ) as visitors, (SUM(payableAmount)) as amount from booking where date = :date AND payment = 1")
    TodayBookingDetails getTodayBookingDetails(String date);

    @Query("select * from booking where date = :date order by id desc")
    List<Booking> getTodayBookings(String date);

    @Query("select COUNT(id) from booking where date = :date AND payment = 1")
    int getTodayPaymentBookingCount(String date);

    @Query("select COUNT(id) from booking where date = :date AND payment = 0")
    int getTodayNotPaymentBookingCount(String date);

    @Query("select * from booking where server = :server")
    List<Booking> getServerStatusBookings(int server);

    @Query("select * from booking where payment = :payment")
    List<Booking> getPaymentStatusBookings(int payment);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBooking(Booking booking);

    @Query("UPDATE booking SET server = :server WHERE id IN (:ids)")
    void updateServerStatus(List<Long> ids, int server);

}
