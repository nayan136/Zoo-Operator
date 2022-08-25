package in.amtron.zoooperator.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import in.amtron.zoooperator.helper.CommonHelper;

/**
 * Created by nayan on 9/1/18.
 */

@Database(
        entities = {Booking.class,Payment.class},
        version = 7,
        exportSchema = false
)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE = null;

    public abstract BookingDao bookingDao();
    public abstract PaymentDao paymentDao();

    public static AppDatabase getDatabase(Context context){

        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, CommonHelper.DATABASE_NAME)
                    //Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                    // To simplify the exercise, allow queries on the main thread.
                    // Don't do this on a real app!
                    .allowMainThreadQueries()
                    // recreate the database if necessary
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
