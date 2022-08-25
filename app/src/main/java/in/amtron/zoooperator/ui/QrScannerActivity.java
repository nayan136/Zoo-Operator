package in.amtron.zoooperator.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import in.amtron.zoooperator.R;
import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.helper.DialogHelper;

public class QrScannerActivity extends AppCompatActivity {

    private static final String TAG = "QrScannerActLog";
    private CodeScanner mCodeScanner;
    private AppDatabase database;

    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("QR Scanner");

        Intent i = getIntent();
        booking = new Gson().fromJson(i.getStringExtra("booking"),Booking.class);

        database = AppDatabase.getDatabase(getApplicationContext());

        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        mCodeScanner = new CodeScanner(this, scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QrScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

                        Log.i(TAG, result.getText());
                        Uri uri = Uri.parse(result.getText());
                        String orderId = uri.getQueryParameter("acquirementId");

                        Booking b = database.bookingDao().getBooking(booking.getTicketNo());
                        if(b != null){

                            Booking checkOrder = database.bookingDao().getSimilarOrderId(orderId);
                            if(checkOrder == null){
                                b.setOrderId(orderId);
                                b.setPayment(1);
                                database.bookingDao().updateBooking(b);

                                Intent i = new Intent(QrScannerActivity.this, TicketActivity.class);
                                i.putExtra("booking",new Gson().toJson(b));
                                startActivity(i);
                            }else{
                                String msg = "Order Id - "+orderId+" is previously used against Ticket No : "+checkOrder.getTicketNo();
//                                new AlertDialog.Builder(getApplicationContext())
//                                        .setTitle("Waning")
//                                        .setMessage(msg)
//                                        .setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                // Continue with delete operation
//                                            }
//                                        })
//                                        .setCancelable(false)
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                        .show();
                                DialogHelper.cancelableErrorDialog(QrScannerActivity.this, "Warning",msg);
                            }


                        }

                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestForCamera();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestForCamera();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void requestForCamera(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mCodeScanner.startPreview();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {

                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}