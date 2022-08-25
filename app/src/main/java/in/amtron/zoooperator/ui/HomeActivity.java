package in.amtron.zoooperator.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.amtron.zoooperator.R;
import in.amtron.zoooperator.ui.fragment.AccountFragment;
import in.amtron.zoooperator.ui.fragment.BookingFragment;
import in.amtron.zoooperator.ui.fragment.HomeFragment;
import in.amtron.zoooperator.ui.fragment.ProfileFragment;
import in.amtron.zoooperator.viewmodel.ModeViewModel;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivityLog";

    private BroadcastReceiver mNetworkReceiver;
    private ModeViewModel model;
    private int currentMode = 0;
    private long pressedTime;

    Fragment fragment;

//    @BindView(R.id.tv_mode)
//    TextView tvMode;
    @BindView(R.id.bottom_nav)
    BottomNavigationView navigation;
//    @BindView(R.id.btn_check_server)
//    Button btnCheckServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        navigation = findViewById(R.id.bottom_nav);
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(bundle.getString("fragment").equals("1")){
                loadFragment(new BookingFragment(),1);
            }else if(bundle.getString("fragment").equals("2")){
                loadFragment(new ProfileFragment(),2);
            }
        }else{
            loadFragment(new HomeFragment(),0);
        }


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        loadFragment(fragment,0);
                        return true;
                    case R.id.navigation_booking:
                        fragment = new BookingFragment();
                        loadFragment(fragment,1);
                        return true;
                    case R.id.navigation_profile:
                        fragment = new AccountFragment();
                        loadFragment(fragment,2);
                        return true;
                }
                return false;
            }
        });

//        checkNetwork();

//        model = new ViewModelProvider(this).get(ModeViewModel.class);
//        final Observer<Integer> nameObserver = new Observer<Integer>() {
//            @Override
//            public void onChanged(@Nullable final Integer newName) {
//                // Update the UI, in this case, a TextView.
//                currentMode = newName;
//                viewMode();
//            }
//        };
//
//        model.getCurrentMode().observe(this, nameObserver);

    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                //super.onBackPressed();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void loadFragment(Fragment fragment, int position) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        navigation.getMenu().getItem(position).setChecked(true);
        transaction.addToBackStack(null);
        transaction.commit();
    }


//    private void viewMode() {
//        String val = Util.getCurrentMode(currentMode);
//        if(currentMode == 1){
//            tvMode.setBackgroundColor(Color.GREEN);
//        }else{
//            tvMode.setBackgroundColor(Color.RED);
//        }
//        tvMode.setText(val);
//        tvMode.setVisibility(View.VISIBLE);
//    }
//
//    private void checkNetwork() {
//        Log.d(TAG,"check network");
//        mNetworkReceiver = new NetworkCheck(this);
//        broadcastIntent();
//
//    }
//
//    private void broadcastIntent() {
//        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//    }
//
//    protected void unregisterNetworkChanges() {
//        if(mNetworkReceiver != null){
//            try {
//                unregisterReceiver(mNetworkReceiver);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            }
//            mNetworkReceiver = null;
//        }
//
//    }
//    @Override
//    public void setDialog(boolean value) {
//        Log.i(TAG, String.valueOf(value));
//        if(value){
//            model.getCurrentMode().setValue(0);
////            btnCheckServer.setVisibility(View.GONE);
//        }else{
////            btnCheckServer.setVisibility(View.VISIBLE);
//            checkServerStatus();
//        }
//    }
//
//    private void checkServerStatus() {
//        Client client = Api.getInstance().getClient();
//        Call<JsonObject> call = client.getServerStatus(
//                Common.getApiKey(),
//                Common.getIPAddress(),
//                Common.getOsVersion(),
//                Common.getImei()
//        );
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
////                Log.i(TAG, response.body().toString());
//                if(response.isSuccessful()){
//                    model.getCurrentMode().setValue(1);
//                }else{
//                    model.getCurrentMode().setValue(2);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.i(TAG,"response is failed - "+t.getMessage());
//                model.getCurrentMode().setValue(2);
//            }
//        });
//    }
//
//    @Override
//    protected void onPause() {
//        Log.i(TAG, "OnPause");
//        super.onPause();
//        unregisterNetworkChanges();
//    }
//
//    @Override
//    protected void onResume() {
//        Log.i(TAG, "onResume");
//        super.onResume();
//        if(mNetworkReceiver == null){
//            checkNetwork();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterNetworkChanges();
//    }

}