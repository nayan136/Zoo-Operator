package in.amtron.zoooperator.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import in.amtron.zoooperator.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DialogHelperNew {

    private static DialogHelperNew instance = null;

    public static  ProgressDialog dialog = null;

    private final Context ctx;
    private AlertDialog.Builder dialogBuilder = null;
    private  AlertDialog noInternetDialog = null;
    private static AlertDialog showServerErrorDialog = null;
    private static AlertDialog cancelableErrorDialog = null;

    private DialogHelperNew(Context context) {
        ctx = context;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading. Please wait...");
        dialogBuilder = new AlertDialog.Builder(context);
    }

    public void removeInstance(){
        cancelDialog();
        cancelNoInternetDialog();
        dismissShowServerError();
        dismissCancelableErrorDialog();

        instance = null;
        dialog = null;
        dialogBuilder = null;
        noInternetDialog = null;
        showServerErrorDialog = null;
        cancelableErrorDialog = null;
    }

    public static DialogHelperNew getInstance(Context ctx) {
        if (instance == null) {
            synchronized (DialogHelperNew.class) {
                if (instance == null) {
                    instance = new DialogHelperNew(ctx);
                }
            }
        }
        return instance;
    }

    public void showDialog(){
        if(dialog != null && instance != null){
            dialog.show();
        }
    }


    public void cancelDialog(){
        if(instance != null && dialog != null){
            dialog.dismiss();
        }
        //dialog = null;
    }

    public ProgressDialog getInstance(){
        return dialog;
    }


//    public void showNoInternetDialog(){
//
//        LayoutInflater li = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View dialogView = li.inflate(R.layout.layout_dialog_no_internet,null);
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.setCancelable(false);
//        noInternetDialog = dialogBuilder.create();
//        if(!noInternetDialog.isShowing()){
//            noInternetDialog.show();
//        }
//    }

    public void cancelNoInternetDialog(){
        if(noInternetDialog != null && instance!=null) {
            noInternetDialog.dismiss();
        }
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
        showServerErrorDialog = builder.create();
        showServerErrorDialog.setCanceledOnTouchOutside(false);
        showServerErrorDialog.show();
    }

    public static void dismissShowServerError(){
        if(showServerErrorDialog != null && instance != null){
            showServerErrorDialog.dismiss();
        }
    }

    public static void cancelableErrorDialog(Activity activity, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_error)
                .setNegativeButton(android.R.string.no,null);
        cancelableErrorDialog = builder.create();
        cancelableErrorDialog.setCanceledOnTouchOutside(false);
        cancelableErrorDialog.show();
    }

    public static void dismissCancelableErrorDialog(){
        if(cancelableErrorDialog != null && instance != null){
            cancelableErrorDialog.dismiss();
        }
    }

}
