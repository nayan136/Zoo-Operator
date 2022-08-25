package in.amtron.zoooperator.ui.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.amtron.zoooperator.R;
import in.amtron.zoooperator.data.Common;
import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Backup;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.database.OnWorkFinishListener;
import in.amtron.zoooperator.helper.CommonHelper;
import in.amtron.zoooperator.helper.DateHelper;
import in.amtron.zoooperator.helper.DialogHelper;
import in.amtron.zoooperator.helper.ResponseHelper;
import in.amtron.zoooperator.helper.SharePref;
import in.amtron.zoooperator.helper.Util;
import in.amtron.zoooperator.model.BookingDetails;
import in.amtron.zoooperator.model.OfflineBooking;
import in.amtron.zoooperator.model.TodayBookingDetails;
import in.amtron.zoooperator.model.User;
import in.amtron.zoooperator.network.Api;
import in.amtron.zoooperator.network.Client;
import in.amtron.zoooperator.network.NetworkCheck;
import in.amtron.zoooperator.network.NetworkListener;
import in.amtron.zoooperator.viewmodel.ModeViewModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements NetworkListener {

    private static final String TAG = "HomeFragmentLog";

    private BroadcastReceiver mNetworkReceiver = null;
    private ModeViewModel model;
    private int currentMode = 0;
    private int currentChunkId = 0;
    private SharePref pref;
    private AppDatabase database;

    private User user;

    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.ll_sync)
    LinearLayout llSync;
    @BindView(R.id.tv_total_paid_ticket)
    TextView tvTotalPaidTicket;
    @BindView(R.id.tv_total_visitors)
    TextView tvTotalVisitors;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.tv_total_not_paid_ticket)
    TextView tvTotalNotPaidTicket;
    @BindView(R.id.tv_total_offline)
    TextView tvTotalOffline;
    @BindView(R.id.btn_sync)
    Button btnSync;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

        database = AppDatabase.getDatabase(getActivity().getApplicationContext());

        pref = SharePref.getInstance(getActivity());
        user = new Gson().fromJson(pref.get(SharePref.USER),User.class);
        checkNetwork();

        Log.i(TAG, "User - "+pref.get(SharePref.USER));

        model = new ViewModelProvider(this).get(ModeViewModel.class);
        final Observer<Integer> nameObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer newName) {
                // Update the UI, in this case, a TextView.
                currentMode = newName;
                pref.put(SharePref.CURRENT_MODE, currentMode);
                viewMode();
            }
        };

        final Observer<Integer> checkIdObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer value) {
                currentChunkId = value;
                syncDataToServer();
            }
        };

        model.getCurrentMode().observe(this, nameObserver);
        model.getChunkNo().observe(this, checkIdObserver);

    }

    private void checkStoragePermissionAndBackup(String json){
        Dexter.withContext(getActivity())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
//                            getDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "backup/");
                        String path = Environment.getExternalStorageDirectory()+ File.separator+"backup"+File.separator;
                        String fileName = DateHelper.getCurrentTime()+"("+currentChunkId+")"+".txt";
                        new Backup.Init()
                                .database(database)
                                .table("Booking")
                                .path(path)
                                .fileName(fileName) //optional
                                .onWorkFinishListener(new OnWorkFinishListener() {
                                    @Override
                                    public void onFinished(boolean success, String message) {
                                        // do anything
//                                        Toast.makeText(getActivity(),"Backup Success",Toast.LENGTH_SHORT).show();
//                                        Log.i(TAG, path+"  Status - "+String.valueOf(success)+" - "+message);
                                    }
                                })
                                .saveToFile(json);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();

    }

    private void getOfflineData(){
        List<Booking> bookings = database.bookingDao().getOfflineBooking();
        tvTotalOffline.setText(String.valueOf(bookings.size()));
    }

    private void syncDataToServer() {
        List<Booking> bookings = database.bookingDao().getOfflineBooking(Common.MAX_CHUNK_SIZE);
        if(bookings.size() > 0){
            sendToServer(bookings);
        }else{

            DialogHelper.cancelableSuccessDialog(getActivity(),"Sync Status","Data has been sync successfully");
        }
        getOfflineData();
    }

    private void sendToServer(List<Booking> bookings){
        JsonArray json = new Gson().toJsonTree(bookings).getAsJsonArray();;

        Client client = Api.getInstance().getClient();
        OfflineBooking of = new OfflineBooking(
                CommonHelper.getApiKey(),
                CommonHelper.getIPAddress(),
                CommonHelper.getOsVersion(),
                CommonHelper.getImei(),
                String.valueOf(user.getId()),
                json
        );
        Log.i(TAG,"data - "+new Gson().toJson(of));
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new Gson().toJson(of)));
        Call<JsonObject> call = client.syncData(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    ResponseHelper helper = new ResponseHelper(response.body());
                    if(helper.isStatusSuccessful()){
                        List<Long> ids = new ArrayList<>();
                        for(Booking b : bookings){
                            ids.add(b.getTableId());
                        }
                        checkStoragePermissionAndBackup(new Gson().toJson(bookings));
                        database.bookingDao().updateServerStatus(ids,1);
                        currentChunkId ++;
                        model.getChunkNo().setValue(currentChunkId);
                    }else{
                        DialogHelper.cancelableErrorDialog(getActivity(),"Sync Error",helper.getErrorMsg());
                    }
                }else{
                    DialogHelper.cancelableErrorDialog(getActivity(),"Sync Error","Error - "+response.code());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i(TAG,"response is failed - "+t.getMessage());
                DialogHelper.cancelableErrorDialog(getActivity(),"Sync Error","Error - "+t.getMessage());
            }
        });
    }

    private void todayTicketCountView(){
        TodayBookingDetails bd = database.bookingDao().getTodayBookingDetails(DateHelper.mysqlDateFormat(DateHelper.getToday()));
        Log.i(TAG, DateHelper.mysqlDateFormat(DateHelper.getToday()));
//        int todayPaidCount = database.bookingDao().getTodayPaymentBookingCount(DateHelper.mysqlDateFormat(DateHelper.getToday()));
        int todayNotPaidCount = database.bookingDao().getTodayNotPaymentBookingCount(DateHelper.mysqlDateFormat(DateHelper.getToday()));

        tvTotalPaidTicket.setText(String.valueOf(bd.getCount()));
        tvTotalVisitors.setText(String.valueOf(bd.getVisitors()));
        tvTotalAmount.setText(Util.moneyFormat(bd.getAmount()));
        tvTotalNotPaidTicket.setText(String.valueOf(todayNotPaidCount));
    }

    private void viewMode() {
        String val = Util.getCurrentMode(currentMode);
        if(currentMode == 1){
            tvMode.setBackgroundColor(Color.GREEN);
        }else{
            tvMode.setBackgroundColor(Color.RED);
        }
        tvMode.setText(val);
        tvMode.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        todayTicketCountView();
        getOfflineData();
        btnSync.setOnClickListener(v->{
            if(currentMode == 1){
                syncDataToServer();
            }else{
                DialogHelper.cancelableErrorDialog(getActivity(),"Server Status","The internet is not available or there is a problem with the server. Please try later.");
            }

        });
//        btnBackup.setOnClickListener(v1->{
//            checkStoragePermissionAndBackup();
//        });
        return view;
    }

    private void checkNetwork() {
        Log.d(TAG,"check network");
        mNetworkReceiver = new NetworkCheck(this);
        broadcastIntent();

    }

    private void broadcastIntent() {
        getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unregisterNetworkChanges() {
        if(mNetworkReceiver != null){
            try {
                getActivity().unregisterReceiver(mNetworkReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            mNetworkReceiver = null;
        }

    }
    @Override
    public void setDialog(boolean value) {
        Log.i(TAG, String.valueOf(value));
        if(value){
            model.getCurrentMode().setValue(0);
//            btnCheckServer.setVisibility(View.GONE);
        }else{
//            btnCheckServer.setVisibility(View.VISIBLE);
            checkServerStatus();
        }
    }

    private void checkServerStatus() {
        Client client = Api.getInstance().getClient();
        Call<JsonObject> call = client.getServerStatus(
                CommonHelper.getApiKey(),
                CommonHelper.getIPAddress(),
                CommonHelper.getOsVersion(),
                CommonHelper.getImei()
        );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.i(TAG, response.body().toString());
                if(response.isSuccessful()){
                    model.getCurrentMode().setValue(1);
                }else{
                    model.getCurrentMode().setValue(2);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i(TAG,"response is failed - "+t.getMessage());
                model.getCurrentMode().setValue(2);
            }
        });
    }

    @Override
    public void onPause() {
        Log.i(TAG, "OnPause");
        super.onPause();
        unregisterNetworkChanges();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        if(mNetworkReceiver == null){
            checkNetwork();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
}