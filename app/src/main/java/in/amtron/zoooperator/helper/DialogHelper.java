package in.amtron.zoooperator.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import in.amtron.zoooperator.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DialogHelper {

    public static ProgressDialog dialog = null;
    private static AlertDialog noInternetDialog = null;

    public static void setNull(){
        dialog = null;
        noInternetDialog = null;
    }

    public static void createDialog(Context context){
        if(dialog != null){
            dialog = null;
        }
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading. Please wait...");
    }
    public static void showDialog(){
        if(dialog != null) {
            dialog.show();
        }
    }

    public static void cancelDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
        //dialog = null;
    }

    public static ProgressDialog getInstance(){
        return dialog;
    }

    public static void showNoInternetDialog(){
        if(noInternetDialog != null){
            noInternetDialog.show();
        }

    }

    public static void cancelNoInternetDialog(){
        if(noInternetDialog != null) {
            noInternetDialog.dismiss();
        }
    }

    public static void setNoInternetDialogCreate(Context context){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = li.inflate(R.layout.layout_dialog_no_internet,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        noInternetDialog = dialogBuilder.create();


    }

    public static void showServerError(Activity activity, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("Server Error")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert);
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(getActivity(), HomeActivity.class);
//                        startActivity(intent);
//                    }
//                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void cancelableErrorDialog(Activity activity, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_error)
                .setNegativeButton(android.R.string.no,null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void cancelableSuccessDialog(Activity activity, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_check)
                .setNegativeButton(android.R.string.no,null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}
