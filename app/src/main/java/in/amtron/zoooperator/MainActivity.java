package in.amtron.zoooperator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.ui.BookingActivity;
import in.amtron.zoooperator.ui.BookingTicketActivity;
import in.amtron.zoooperator.ui.PaymentActivity;
import in.amtron.zoooperator.ui.SplashActivity;
import in.amtron.zoooperator.viewmodel.ModeViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityLog";

    private ModeViewModel model;
    private String currentMode = "online";

    private AppDatabase database;

    private TextView tvMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);

        database = AppDatabase.getDatabase(getApplicationContext());
//        List<Booking> bookings = database.bookingDao().getBookings();
//        Log.i(TAG,new Gson().toJson(bookings));

        tvMode = findViewById(R.id.tv_mode);

//        model = new ViewModelProvider(this).get(ModeViewModel.class);
//        // Create the observer which updates the UI.
//        final Observer<String> nameObserver = new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable final String newName) {
//                // Update the UI, in this case, a TextView.
//                currentMode = newName;
//                Log.i(TAG, newName);
//                changeViewMode();
//            }
//        };
//
//        findViewById(R.id.btn_check_server).setOnClickListener(v->{
//            if(currentMode.equals("online")){
//                currentMode = "offline";
//            }else{
//                currentMode = "online";
//            }
//            model.getCurrentMode().setValue(currentMode);
//        });
//
//        findViewById(R.id.btn_book).setOnClickListener(v->{
//            Intent i = new Intent(this, BookingTicketActivity.class);
//            startActivity(i);
//        });
//
//        findViewById(R.id.btn_backup).setOnClickListener(v1->{
//            Dexter.withContext(this)
//                    .withPermissions(
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                    )
//                    .withListener(new MultiplePermissionsListener() {
//                        @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
////                            getDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "backup/");
//                            String path = Environment.getExternalStorageDirectory()+ File.separator+"backup"+File.separator;
//                            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//                            new Backup.Init()
//                                    .database(database)
//                                    .path(path)
//                                    .fileName("filename.txt") //optional
//                                    .onWorkFinishListener(new OnWorkFinishListener() {
//                                        @Override
//                                        public void onFinished(boolean success, String message) {
//                                            // do anything
//                                            Toast.makeText(getApplicationContext(),"Backup Success",Toast.LENGTH_SHORT).show();
//                                            Log.i(TAG, path+"  Status - "+String.valueOf(success)+" - "+message);
//                                        }
//                                    })
//                                    .execute();
//                        }
//                        @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
//                    }).check();
//
//        });

    }

    private void changeViewMode() {
        tvMode.setText(currentMode);
    }
}