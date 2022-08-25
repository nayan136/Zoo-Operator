package in.amtron.zoooperator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import in.amtron.zoooperator.R;
import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.model.PriceDetails;

public class BookingActivity extends AppCompatActivity {

    private static final String TAG = "BookingActivityLog";

    private EditText txtPhoneNumber;
    private RadioGroup rgNation;
    private RadioButton rbIndian, rbForeign;
    private TextView txtAdultPrice, txtChildrenPrice, txtStudentPrice, txtCameraPrice, txtDSLRPrice, txtVideoPrice, txtTotalAdultNo, txtTotalChildrenNo,
            txtTotalStudentNo, txtTotalCameraNo, txtTotalDSLRNo, txtTotalVideoNo, totalVisitorPrice, totalCameraPrice, totalAmount;
    private Button adultFigureNegate, adultFigureAdd, childrenFigureNegate, childrenFigureAdd, studentFigureNegate, studentFigureAdd,
            cameraFigureNegate, cameraFigureAdd, dslrFigureNegate, dslrFigureAdd, videoFigureNegate, videoFigureAdd, btnConfirm;

    private List<PriceDetails> priceList = new ArrayList<PriceDetails>();
    private PriceDetails selectedPrice;
    private double visitors = 0;
    private double camera = 0;
    private double amount = 0;
    private int totalAdult = 0;
    private int totalChildren = 0;
    private int totalStudent = 0;
    private int totalCams = 0;
    private int totalDSLR = 0;
    private int totalVideo = 0;

    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        database = AppDatabase.getDatabase(getApplicationContext());

        // TODO: remove later
        createPrice();
        setView();
        changeToIndian();
        rgNation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbIndian){
                    changeToIndian();

                }else {
                    changeToForeigner();
                }
                setVisitors();
                setCameras();
            }
        });

        setVisitors();
        setCameras();

        btnConfirm.setOnClickListener(v->{
            String ticketNo = "OFFLINE"+System.currentTimeMillis();
//            Booking b = new Booking("2021-10-03",txtPhoneNumber.getText().toString(),ticketNo,selectedPrice.getType(),totalAdult,totalChildren,totalStudent,totalCams,totalDSLR,totalVideo,"HGTRRT45RED");
//            Long id = database.bookingDao().addBooking(b);
//            Log.i(TAG, String.valueOf(id));
        });

    }

    private void createPrice() {
        PriceDetails p1 = new PriceDetails("I",30.00,10.00,20.00,100.00,200.00,300.00);
        PriceDetails p2 = new PriceDetails("F",100.00,50.00,30.00,200.00,300.00,500.00);
        priceList.add(p1);
        priceList.add(p2);
    }

    private void setView() {
        txtPhoneNumber = findViewById(R.id.phoneNumber);
        rgNation = findViewById(R.id.rgNation);
        rbIndian = findViewById(R.id.rbIndian);
        rbForeign = findViewById(R.id.rbForeign);
        txtAdultPrice = findViewById(R.id.txtAdultPrice);
        txtChildrenPrice = findViewById(R.id.txtChildrenPrice);
        txtStudentPrice = findViewById(R.id.txtStudentPrice);
        txtCameraPrice = findViewById(R.id.txtCameraPrice);
        txtDSLRPrice = findViewById(R.id.txtDSLRPrice);
        txtVideoPrice = findViewById(R.id.txtVideoPrice);
        txtTotalAdultNo = findViewById(R.id.txtTotalAdultNo);
        txtTotalChildrenNo = findViewById(R.id.txtTotalChildrenNo);
        txtTotalStudentNo = findViewById(R.id.txtTotalStudentNo);
        txtTotalCameraNo = findViewById(R.id.txtTotalCameraNo);
        txtTotalDSLRNo = findViewById(R.id.txtTotalDSLRNo);
        txtTotalVideoNo = findViewById(R.id.txtTotalVideoNo);
        totalVisitorPrice = findViewById(R.id.totalVisitors);
        totalCameraPrice = findViewById(R.id.totalCamera);
        totalAmount = findViewById(R.id.totalAmount);
        adultFigureNegate = findViewById(R.id.adultFigureNegate);
        adultFigureAdd = findViewById(R.id.adultFigureAdd);
        childrenFigureNegate = findViewById(R.id.childrenFigureNegate);
        childrenFigureAdd = findViewById(R.id.childrenFigureAdd);
        studentFigureNegate = findViewById(R.id.studentFigureNegate);
        studentFigureAdd = findViewById(R.id.studentFigureAdd);
        cameraFigureNegate = findViewById(R.id.cameraFigureNegate);
        cameraFigureAdd = findViewById(R.id.cameraFigureAdd);
        dslrFigureNegate = findViewById(R.id.dslrFigureNegate);
        dslrFigureAdd = findViewById(R.id.dslrFigureAdd);
        videoFigureNegate = findViewById(R.id.videoFigureNegate);
        videoFigureAdd = findViewById(R.id.videoFigureAdd);
        btnConfirm = findViewById(R.id.btnConfirm);

    }


    private void changeToIndian(){

        // select price
        for(PriceDetails p: priceList){
            if(p.getType().equals("I")){
                selectedPrice = p;
                break;
            }
        }

        calculatePrice();
    }

    private void changeToForeigner(){
        // select price
        for(PriceDetails p: priceList){
            if(p.getType().equals("F")){
                selectedPrice = p;
                break;
            }
        }

        calculatePrice();
    }

    private void setVisitors(){
        txtAdultPrice.setText("Adult (₹  "+((int)selectedPrice.getAdultPrice())+") :");
        txtChildrenPrice.setText("Children (₹ "+((int)selectedPrice.getChildrenPrice())+") :");
        txtStudentPrice.setText("Student (₹ "+((int)selectedPrice.getStudentPrice())+") :");

        adultFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalAdult++;
                txtTotalAdultNo.setText(String.valueOf(totalAdult));
                calculatePrice();
            }
        });

        adultFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalAdult = txtTotalAdultNo.getText().toString();
                if (!txtTotalAdult.equals("0")){
                    totalAdult--;
                    txtTotalAdultNo.setText(String.valueOf(totalAdult));
                    calculatePrice();
                }
            }
        });

        childrenFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalChildren++;
                txtTotalChildrenNo.setText(String.valueOf(totalChildren));
                calculatePrice();
            }
        });

        childrenFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalChildren = txtTotalChildrenNo.getText().toString();
                if (!txtTotalChildren.equals("0")){
                    totalChildren--;
                    txtTotalChildrenNo.setText(String.valueOf(totalChildren));
                    calculatePrice();
                }
            }
        });

        studentFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalStudent++;
                txtTotalStudentNo.setText(String.valueOf(totalStudent));
                calculatePrice();
            }
        });

        studentFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalStudent = txtTotalStudentNo.getText().toString();
                if (!txtTotalStudent.equals("0")){
                    totalStudent--;
                    txtTotalStudentNo.setText(String.valueOf(totalStudent));
                   calculatePrice();
                }
            }
        });
    }

    private void setCameras(){
        txtCameraPrice.setText("Camera (₹ "+((int)selectedPrice.getStillPrice())+") :");
        txtDSLRPrice.setText("DSLR (₹ "+((int)selectedPrice.getDslrPrice())+") :");
        txtVideoPrice.setText("Video (₹ "+((int)selectedPrice.getVideoPrice())+") :");

        cameraFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalCams++;
                txtTotalCameraNo.setText(String.valueOf(totalCams));
                calculatePrice();
            }
        });

        cameraFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalCamera = txtTotalCameraNo.getText().toString();
                if (!txtTotalCamera.equals("0")){
                    totalCams--;
                    txtTotalCameraNo.setText(String.valueOf(totalCams));
                    calculatePrice();
                }
            }
        });

        dslrFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalDSLR++;
                txtTotalDSLRNo.setText(String.valueOf(totalDSLR));
                calculatePrice();
            }
        });

        dslrFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalDSLR = txtTotalDSLRNo.getText().toString();
                if (!txtTotalDSLR.equals("0")){
                    totalDSLR--;
                    txtTotalDSLRNo.setText(String.valueOf(totalDSLR));
                    calculatePrice();
                }
            }
        });

        videoFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalVideo++;
                txtTotalVideoNo.setText(String.valueOf(totalVideo));
                calculatePrice();
            }
        });

        videoFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalVideo = txtTotalVideoNo.getText().toString();
                if (!txtTotalVideo.equals("0")){
                    totalVideo--;
                    txtTotalVideoNo.setText(String.valueOf(totalVideo));
                    calculatePrice();
                }
            }
        });
    }

    private void calculatePrice(){
        totalAdult = Integer.parseInt(txtTotalAdultNo.getText().toString());
        totalChildren = Integer.parseInt(txtTotalChildrenNo.getText().toString());
        totalStudent = Integer.parseInt(txtTotalStudentNo.getText().toString());
        totalCams= Integer.parseInt(txtTotalCameraNo.getText().toString());
        totalDSLR = Integer.parseInt(txtTotalDSLRNo.getText().toString());
        totalVideo = Integer.parseInt(txtTotalVideoNo.getText().toString());

        visitors = (selectedPrice.getAdultPrice()*totalAdult)+(selectedPrice.getChildrenPrice() * totalChildren) + (selectedPrice.getStudentPrice()*totalStudent);
        camera = (selectedPrice.getStillPrice()*totalCams) + (selectedPrice.getDslrPrice()*totalDSLR) + (selectedPrice.getVideoPrice()*totalVideo);
        amount = visitors + camera;

        updatePriceView();
    }

    private void updatePriceView() {
        totalVisitorPrice.setText(String.valueOf(visitors));
        totalCameraPrice.setText(String.valueOf(camera));
        totalAmount.setText(String.valueOf(amount));
    }


}