package in.amtron.zoooperator.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.amtron.zoooperator.QRGenerator.QRGContents;
import in.amtron.zoooperator.QRGenerator.QRGEncoder;
import in.amtron.zoooperator.R;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.helper.DateHelper;
import in.amtron.zoooperator.helper.Util;
import in.amtron.zoooperator.helper.Validator;

public class TicketActivity extends AppCompatActivity {

    private static final String TAG = "TicketActivityLog";

    private Booking booking;

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_ticket_number)
    TextView tvTicketNumber;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_ind_details)
    TextView tvIndianDetails;
    @BindView(R.id.tv_fore_details)
    TextView tvForeignDetails;
    @BindView(R.id.tv_fare)
    TextView tvFare;
    @BindView(R.id.tv_convenient)
    TextView tvConvenient;
    @BindView(R.id.totalAmount)
    TextView tvTotalAmount;
    @BindView(R.id.tv_payment)
    TextView tvPayment;

    @BindView(R.id.btn_print)
    Button btnPrint;
    @BindView(R.id.btn_new)
    Button btnNew;

    @BindView(R.id.iv_qr)
    ImageView ivQr;

    @BindView(R.id.ll_ticket)
    LinearLayout llTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Ticket");

        ButterKnife.bind(this);

        Intent i = getIntent();
        String json = i.getStringExtra("booking");
        Gson gson = new Gson();
        booking = gson.fromJson(json,Booking.class);

//        Log.i(TAG, booking.getOrderId());
//        if(i != null){
//            String json = i.getStringExtra("booking");
//            Gson gson = new Gson();
//            booking = gson.fromJson(json,Booking.class);
//        }else{
//            Intent intent = new Intent(this, TicketHistoryActivity.class);
//            startActivity(intent);
//        }
//
        createView();

        btnPrint.setOnClickListener(v->{
            viewToImage(llTicket);
        });

        btnNew.setOnClickListener(v1->{
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("fragment","1");
            startActivity(intent);
        });

        if(canPrint() && booking.getPayment() == 1){
            btnPrint.setVisibility(View.VISIBLE);
        }else{
            btnPrint.setVisibility(View.GONE);
        }

    }

    private Boolean canPrint(){
        return DateHelper.mysqlDateFormat(DateHelper.getToday()).equals(booking.getDate());
    }

    private void createAndSetQrCode(String code){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        QRGEncoder qrgEncoder = new QRGEncoder(code, null, QRGContents.Type.TEXT, 500);
        Bitmap bitmap = qrgEncoder.getBitmap();
        ivQr.setImageBitmap(bitmap);
    }

    private void viewToImage(View view){
//        Log.i(TAG, String.valueOf(view.getWidth()));
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//        photoPrinter.setOrientation(PrintHelper.ORIENTATION_PORTRAIT);
        photoPrinter.printBitmap("droids.jpg - test print", returnedBitmap);

    }

    private void createView() {

        tvDate.setText("DATE: "+DateHelper.mysqlDateFormatToReadableFormat(booking.getDate()));
        tvTicketNumber.setText("Ticket No: "+booking.getTicketNo());
        tvMobile.setText("Mobile: "+booking.getMobile());

        String indian = "A("+booking.getIndianAdults()+")  C("+booking.getIndianChildrens()+")  S("+booking.getIndianStudents()+")  ";
        indian += "Cam("+booking.getIndianStillCameras()+")  DSLR("+booking.getIndianSlrCameras()+")  V("+booking.getIndianVideoCameras()+")";
        tvIndianDetails.setText(indian);

        String foreigner = "A("+booking.getForeignAdults()+")  C("+booking.getForeignChildrens()+")  S("+booking.getForeignStudents()+")  ";
        foreigner += "Cam("+booking.getForeignStillCameras()+")  DSLR("+booking.getForeignSlrCameras()+")  V("+booking.getForeignVideoCameras()+")";
        tvForeignDetails.setText(foreigner);

        tvFare.setText(Util.moneyFormat(booking.getPrice()));

        double cc = booking.getPayableAmount() - booking.getPrice();

        tvConvenient.setText(Util.moneyFormat(cc));
        tvTotalAmount.setText(Util.moneyFormat(booking.getPayableAmount()));

        if(booking.getPayment() == 1){
            tvPayment.setText("Payment : Paid");
            btnPrint.setVisibility(View.VISIBLE);
        }else{
            tvPayment.setText("Payment : Not Paid");
            btnPrint.setVisibility(View.GONE);
        }

        createAndSetQrCode(booking.getTicketNo());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
//                Intent i = new Intent(this, HomeActivity.class);
//                i.putExtra("fragment","1");
//                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}