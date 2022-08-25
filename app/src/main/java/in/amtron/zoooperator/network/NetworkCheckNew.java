package in.amtron.zoooperator.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetworkCheckNew extends BroadcastReceiver {

    private static final String TAG = "NetworkCheck_log";

    private static NetworkCheck instance = null;

    private NetworkListener mListener;

    public NetworkCheckNew(NetworkListener mListener) {
        this.mListener = mListener;
    }


    public boolean isInternetAvailable(Context context){
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null){
            Log.d(TAG,"no internet connection");
            return false;
        }else{
            if(info.isConnected()){
                Log.d(TAG," internet connection available...");
                return true;
            }else{
                Log.d(TAG," internet connection");
                return true;
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try
        {
            if (isOnline(context)) {
                if(mListener != null){
                    mListener.setDialog(false);
                }
                Log.d(TAG, "Online Connect Intenet ");
            } else {
                if(mListener != null){
                    mListener.setDialog(true);
                }else{
                    Log.d(TAG, "Conectivity Failure !!! ");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            Log.i(TAG, String.valueOf(netInfo != null && netInfo.isConnected()));
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}