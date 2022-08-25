package in.amtron.zoooperator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.amtron.zoooperator.R;
import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.database.Payment;
import in.amtron.zoooperator.helper.SharePref;
import in.amtron.zoooperator.helper.Util;
import in.amtron.zoooperator.helper.Validator;

public class PaymentActivity extends AppCompatActivity {

    private SharePref pref;
    private Booking booking;
//    private Long bookingId;
    private AppDatabase database;

    private Long paymentId;

    @BindView(R.id.tv_ticket_no)
    TextView tvTicketNo;
//    @BindView(R.id.tv_ind_details)
//    TextView tvIndianDetails;
//    @BindView(R.id.tv_fore_details)
//    TextView tvForeignerDetails;
    @BindView(R.id.tv_price)
    TextView tvPrice;
//
//    @BindView(R.id.tv_ipg)
//    TextView tvIpg;
    @BindView(R.id.tv_service)
    TextView tvService;
//    @BindView(R.id.tv_gst)
//    TextView tvGst;
    @BindView(R.id.tv_payable)
    TextView tvPayable;

    @BindView(R.id.ll_ticket_container)
    LinearLayout llTicketContainer;

    @BindView(R.id.btn_qr_scan)
    Button btnScan;
//    @BindView(R.id.btn_start_pay)
//    Button btnStartPay;

    @BindView(R.id.et_order_no)
    EditText etOrderNo;
    @BindView(R.id.btn_add)
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");

        ButterKnife.bind(this);

        database = AppDatabase.getDatabase(getApplicationContext());

        pref = SharePref.getInstance(this);

        Gson gson = new Gson();
//        booking = gson.fromJson(pref.get(SharePref.BOOKING),Booking.class);
//        bookingId = pref.getLong(SharePref.BOOKING_ID);

        Intent i = getIntent();
        if(i != null){
            booking = gson.fromJson(i.getStringExtra("booking"),Booking.class);
//            bookingId = i.getLongExtra("booking_id",0);
        }else{
//            Intent intent = new Intent(this, )
        }

        tvTicketNo.setText(booking.getTicketNo());

        updateTicketDetails();
        updatePriceDetails();

        btnScan.setOnClickListener(v->{
            Intent i1 = new Intent(this,QrScannerActivity.class);
            i1.putExtra("booking", new Gson().toJson(booking));
            startActivity(i1);
        });

        btnAdd.setOnClickListener(v2->{
            Validator v = new Validator();
            v.setData(etOrderNo,"required,orderId");

            if(v.validate()){
                String orderId = etOrderNo.getText().toString();
                Booking b = database.bookingDao().getBooking(booking.getTicketNo());
                if(b != null) {
                    Booking checkOrder = database.bookingDao().getSimilarOrderId(orderId);
                    if (checkOrder == null) {
                        b.setOrderId(orderId);
                        b.setPayment(1);
                        if(b.getOrderId() == null){
                            Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT).show();
                        }else{
                            database.bookingDao().updateBooking(b);

                            Intent i1 = new Intent(PaymentActivity.this, TicketActivity.class);
                            i1.putExtra("booking", new Gson().toJson(b));
                            startActivity(i1);
                        }

                    }
                }
            }
        });
    }

    private void updatePriceDetails() {
        tvPrice.setText(Util.moneyFormat1(booking.getPrice()));
//        tvIpg.setText(Util.moneyFormat1(booking.getIpg_amount()));
        tvService.setText(Util.moneyFormat1(booking.getTotalServiceCharge()));
//        tvGst.setText(Util.moneyFormat1(booking.getGst_amount()));
        tvPayable.setText(Util.moneyFormat1(booking.getPayableAmount()));
    }

    private void updateTicketDetails() {

        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(booking.hasIndian()){
            View v = inflater.inflate(R.layout.layout_ticket_details, null);
            createTicketDetailsView(v,true);
        }

        if(booking.hasForeigner()){
            View v1 = inflater.inflate(R.layout.layout_ticket_details, null);
            createTicketDetailsView(v1,false);
        }


//        String indian = "A("+booking.getIndianAdults()+")  C("+booking.getIndianChildrens()+")  S("+booking.getIndianStudents()+")  ";
//        indian += "Cam("+booking.getIndianStillCameras()+")  DSLR("+booking.getIndianSlrCameras()+")  V("+booking.getIndianVideoCameras()+")";
//        tvIndianDetails.setText(indian);
//
//        String foreigner = "A("+booking.getForeignAdults()+")  C("+booking.getForeignChildrens()+")  S("+booking.getForeignStudents()+")  ";
//        foreigner += "Cam("+booking.getForeignStillCameras()+")  DSLR("+booking.getForeignSlrCameras()+")  V("+booking.getForeignVideoCameras()+")";
//        tvForeignerDetails.setText(foreigner);
    }

    private void createTicketDetailsView(View v, Boolean isIndian){

        TextView tvVisitorType,tvAdult,tvChildren,tvStudent,tvCamera,tvDslr,tvVideo;

        tvVisitorType = v.findViewById(R.id.tv_visitor_type);
        tvAdult = v.findViewById(R.id.tv_adult);
        tvChildren = v.findViewById(R.id.tv_children);
        tvStudent = v.findViewById(R.id.tv_student);
        tvCamera = v.findViewById(R.id.tv_camera);
        tvDslr = v.findViewById(R.id.tv_dslr);
        tvVideo = v.findViewById(R.id.tv_video);

        String adult,children,student,camera,dslr,video = "0";
        String visitorType = "";

        if(isIndian){
            visitorType = "INDIAN";
            adult = String.valueOf(booking.getIndianAdults());
            children = String.valueOf(booking.getIndianChildrens());
            student = String.valueOf(booking.getIndianStudents());
            camera = String.valueOf(booking.getIndianStillCameras());
            dslr = String.valueOf(booking.getIndianSlrCameras());
            video = String.valueOf(booking.getIndianVideoCameras());
        }else{
            visitorType = "FOREIGNER";
            adult = String.valueOf(booking.getForeignAdults());
            children = String.valueOf(booking.getForeignChildrens());
            student = String.valueOf(booking.getForeignStudents());
            camera = String.valueOf(booking.getForeignStillCameras());
            dslr = String.valueOf(booking.getForeignSlrCameras());
            video = String.valueOf(booking.getForeignVideoCameras());
        }
        tvVisitorType.setText(visitorType);
        tvAdult.setText(adult);
        tvChildren.setText(children);
        tvStudent.setText(student);
        tvCamera.setText(camera);
        tvVideo.setText(video);
        tvDslr.setText(dslr);

        llTicketContainer.addView(v);
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