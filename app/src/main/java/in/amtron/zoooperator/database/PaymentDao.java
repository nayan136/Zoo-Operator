package in.amtron.zoooperator.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addPayment(Payment payment);

    @Query("select * from payment")
    List<Payment> getPayments();

    @Query("select * from payment where id = :id")
    Payment getPayment(Long id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePayment(Payment payment);
}
