package in.amtron.zoooperator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.amtron.zoooperator.R;
import in.amtron.zoooperator.helper.CommonHelper;
import in.amtron.zoooperator.helper.DialogHelper;
import in.amtron.zoooperator.helper.ResponseHelper;
import in.amtron.zoooperator.helper.SharePref;
import in.amtron.zoooperator.network.Api;
import in.amtron.zoooperator.network.Client;
import in.amtron.zoooperator.network.NetworkCheck;
import in.amtron.zoooperator.network.NetworkListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements NetworkListener {
    public static final String TAG = "LoginActivityLog";

    private BroadcastReceiver mNetworkReceiver;
    private SharePref pref;

    @BindView(R.id.et_mobile)
    EditText mobile;
    @BindView(R.id.et_password)
    EditText password;
    @BindView(R.id.btn_next)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        pref = SharePref.getInstance(this);

        createDialog();
        checkNetwork();
        DialogHelper.setNoInternetDialogCreate(this);

        btnLogin.setOnClickListener(v->login());
    }

    private void login(){
        String mobileNo = mobile.getText().toString();
        if(mobileNo.length() == 10){
            DialogHelper.showDialog();
            Client client = Api.getInstance().getClient();
            Call<JsonObject> call = client.login(
                    CommonHelper.getApiKey(),
                    CommonHelper.getIPAddress(),
                    CommonHelper.getOsVersion(),
                    CommonHelper.getImei(),
                    mobileNo,
                    password.getText().toString()
            );
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    DialogHelper.cancelDialog();
                    if(response.isSuccessful()){
                        ResponseHelper helper = new ResponseHelper(response.body());
                        if(helper.isStatusSuccessful()){
                            pref.put(SharePref.USER,helper.getDataAsString());
                            Intent i =new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                        }else{
                            showErrorDialog(helper.getErrorMsg());
                        }
                    }else{
                        showErrorDialog("Response Error Code "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    DialogHelper.cancelDialog();
                    showErrorDialog("Server Error. Please try again.");
                }
            });
        }else{
            DialogHelper.cancelableErrorDialog(LoginActivity.this,"Error","Please check your mobile number.");
        }

    }

    private void showErrorDialog(String msg){
        DialogHelper.cancelableErrorDialog(LoginActivity.this,"Error",msg);
    }

    private void createDialog(){
        DialogHelper.createDialog(this);
    }

    private void checkNetwork() {
        Log.d(TAG,"check network");
        mNetworkReceiver = new NetworkCheck(this);
        broadcastIntent();

    }

    private void broadcastIntent() {
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unregisterNetworkChanges() {
        if(mNetworkReceiver != null){
            try {
                unregisterReceiver(mNetworkReceiver);
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
            DialogHelper.showNoInternetDialog();
        }else{
            DialogHelper.cancelNoInternetDialog();
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "OnPause");
        super.onPause();
        DialogHelper.cancelDialog();
        DialogHelper.cancelNoInternetDialog();
        DialogHelper.setNull();
        unregisterNetworkChanges();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        createDialog();
        DialogHelper.setNoInternetDialogCreate(this);
        if(mNetworkReceiver == null){
            checkNetwork();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
}