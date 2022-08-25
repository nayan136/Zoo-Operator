package in.amtron.zoooperator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.amtron.zoooperator.R;
import in.amtron.zoooperator.helper.CommonHelper;
import in.amtron.zoooperator.helper.ResponseHelper;
import in.amtron.zoooperator.helper.SharePref;
import in.amtron.zoooperator.network.Api;
import in.amtron.zoooperator.network.Client;
import in.amtron.zoooperator.network.NetworkCheck;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity{

    public static final String TAG = "SplashActivityLog";

    private SharePref pref;
    private boolean isInternetAvailable;

    @BindView(R.id.iv_loading)
    ImageView imageView;
    @BindView(R.id.tv_network_status)
    TextView tvNetworkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        pref = SharePref.getInstance(this);

        Glide.with(this).load(R.drawable.loading).into(imageView);

        checkIfInternetAvailable();
    }

    private void checkIfInternetAvailable() {
        final Handler handler = new Handler(Looper.getMainLooper());
        isInternetAvailable = NetworkCheck.isInternetAvailable(this);
        if(isInternetAvailable){
            tvNetworkStatus.setText("Network Found. Fetching data from the server ...");
            getMasterData();
        }else{
            tvNetworkStatus.setText("No Network Found. Proceed with Offline Mode.");
            if(isMasterDataAvailable()){
                if(isLoggedIn()){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToHomeActivity();
                        }
                    }, 2000);
                }else{
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToLoginActivity();
                        }
                    }, 2000);
                    tvNetworkStatus.setText("Please check your internet connection and login.");
                }
            }else{
                tvNetworkStatus.setText("Master data is not available. Please check your internet connection.");
            }
        }
    }

    private boolean isLoggedIn(){
        return !pref.get(SharePref.USER).equals("");
    }

    private boolean isMasterDataAvailable(){
        return !pref.get(SharePref.DATA).equals("");
    }

    private void getMasterData() {
        Log.i(TAG, "get master data");
        Client client = Api.getInstance().getClient();
        Call<JsonObject> call = client.getMasterData(
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
                    ResponseHelper helper = new ResponseHelper(response.body());
                    if(helper.isStatusSuccessful()){
                        pref.put(SharePref.DATA, helper.getDataAsString());
                        goToAnotherActivity("",false);

                    }else{
                        goToAnotherActivity(helper.getErrorMsg(),true);
                    }
                }else{
                    goToAnotherActivity("Error Code - "+response.code(),true);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i(TAG,"response is failed - "+t.getMessage());
                goToAnotherActivity("Error - "+t.getMessage(),true);
            }
        });

    }

    private void goToAnotherActivity(String msg, boolean willWait){
        boolean isUserLoggedIn = !pref.get(SharePref.USER).equals("");
        boolean isMasterDataAvailable = !pref.get(SharePref.DATA).equals("");
        Log.i(TAG, "isUserLoggedIn - "+String.valueOf(isUserLoggedIn));
        if(isMasterDataAvailable){
            if(!isUserLoggedIn){
                if(willWait){
                    tvNetworkStatus.setText(msg);
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToLoginActivity();
                        }
                    }, 2000);
                }else{
                    goToLoginActivity();
                }

            }else{
                if(willWait){
                    tvNetworkStatus.setText(msg);
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToHomeActivity();
                        }
                    }, 2000);
                }else{
                    goToHomeActivity();
                }

            }
        }else{
            if(msg.equals("")){
                msg = "Master data is not available. Please check your internet connection.";
            }
            tvNetworkStatus.setText(msg);
        }
    }

    private void goToHomeActivity() {
        Log.i(TAG,"In Home Activity");
        Intent i =new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(i);
    }

    private void goToLoginActivity(){
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
    }



//    @Override
//    public void setDialog(boolean value) {
//        Log.i(TAG, String.valueOf(value));
//        if(value){
//            tvNetworkStatus.setText("No Network Found. Proceed with Offline Mode.");
//        }else{
//            tvNetworkStatus.setText("Network Found. Fetching data from the server ...");
//            //getMasterData();
//        }
//    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}