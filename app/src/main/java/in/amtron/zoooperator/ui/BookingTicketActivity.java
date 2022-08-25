package in.amtron.zoooperator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.amtron.zoooperator.R;
import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.model.PriceDetails;

public class BookingTicketActivity extends AppCompatActivity {

    private static final String TAG = "BookingTicketActLog";

    private EditText txtPhoneNumber;
    private RadioGroup rgNation;
    private RadioButton rbIndian, rbForeign;
    private TextView txtAdultPrice, txtChildrenPrice, txtStudentPrice, txtCameraPrice, txtDSLRPrice, txtVideoPrice, txtTotalAdultNo, txtTotalChildrenNo,
            txtTotalStudentNo, txtTotalCameraNo, txtTotalDSLRNo, txtTotalVideoNo, totalVisitorPrice, totalCameraPrice, totalAmount;
    private Button adultFigureNegate, adultFigureAdd, childrenFigureNegate, childrenFigureAdd, studentFigureNegate, studentFigureAdd,
            cameraFigureNegate, cameraFigureAdd, dslrFigureNegate, dslrFigureAdd, videoFigureNegate, videoFigureAdd, btnConfirm;

    private TextView tvIndianDetails, tvForeignerDetails;

    private List<PriceDetails> priceList = new ArrayList<PriceDetails>();
    private PriceDetails selectedPrice;
    private double visitors = 0;
    private double camera = 0;
    private double amount = 0;
    private int totalIndianAdult = 0;
    private int totalIndianChildren = 0;
    private int totalIndianStudent = 0;
    private int totalIndianCams = 0;
    private int totalIndianDSLR = 0;
    private int totalIndianVideo = 0;

    private int totalForeignerAdult = 0;
    private int totalForeignerChildren = 0;
    private int totalForeignerStudent = 0;
    private int totalForeignerCams = 0;
    private int totalForeignerDSLR = 0;
    private int totalForeignerVideo = 0;

    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_ticket);

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

//        btnConfirm.setOnClickListener(v->{
//            String ticketNo = "OFFLINE"+System.currentTimeMillis();
//            Booking b = new Booking("2021-10-03",txtPhoneNumber.getText().toString(),ticketNo,selectedPrice.getType(),totalAdult,totalChildren,totalStudent,totalCams,totalDSLR,totalVideo,"HGTRRT45RED");
//            Long id = database.bookingDao().addBooking(b);
//            Log.i(TAG, String.valueOf(id));
//        });

    }

    private void createPrice() {
        PriceDetails p1 = new PriceDetails("I",30.00,10.00,20.00,100.00,200.00,300.00);
        PriceDetails p2 = new PriceDetails("F",100.00,50.00,30.00,200.00,300.00,500.00);
        priceList.add(p1);
        priceList.add(p2);
    }

    private void setView() {

        tvIndianDetails = findViewById(R.id.tv_ind_details);
        tvForeignerDetails = findViewById(R.id.tv_fore_details);

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

//        totalAdult = Integer.parseInt(txtTotalAdultNo.getText().toString());
//        totalChildren = Integer.parseInt(txtTotalChildrenNo.getText().toString());
//        totalStudent = Integer.parseInt(txtTotalStudentNo.getText().toString());

        int totalAdult = 0;
        int totalChildren = 0;
        int totalStudent = 0;
        if(selectedPrice.getType().equals("I")){
            totalAdult = totalIndianAdult;
            totalChildren = totalIndianChildren;
            totalStudent = totalIndianStudent;
        }else{
            totalAdult = totalForeignerAdult;
            totalChildren = totalForeignerChildren;
            totalStudent = totalForeignerStudent;
        }
        txtTotalAdultNo.setText(String.valueOf(totalAdult));
        txtTotalChildrenNo.setText(String.valueOf(totalChildren));
        txtTotalStudentNo.setText(String.valueOf(totalStudent));

        adultFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalAdult = 0;
                if(selectedPrice.getType().equals("I")){
                    totalAdult = ++totalIndianAdult;
                }else{
                    totalAdult = ++totalForeignerAdult;
                }
//                totalAdult++;
                txtTotalAdultNo.setText(String.valueOf(totalAdult));
                calculatePrice();
            }
        });

        adultFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalAdult = txtTotalAdultNo.getText().toString();
                if (!txtTotalAdult.equals("0")){
                    int totalAdult = 0;
                    if(selectedPrice.getType().equals("I")){
                        totalAdult = --totalIndianAdult;
                    }else{
                        totalAdult = --totalForeignerAdult;
                    }
                    txtTotalAdultNo.setText(String.valueOf(totalAdult));
                    calculatePrice();
                }
            }
        });

        childrenFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalChildren = 0;
                if(selectedPrice.getType().equals("I")){
                    totalChildren = ++totalIndianChildren;
                }else{
                    totalChildren = ++totalForeignerChildren;
                }
//                totalChildren++;
                txtTotalChildrenNo.setText(String.valueOf(totalChildren));
                calculatePrice();
            }
        });

        childrenFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalChildren = txtTotalChildrenNo.getText().toString();
                if (!txtTotalChildren.equals("0")){
                    int totalChildren = 0;
                    if(selectedPrice.getType().equals("I")){
                        totalChildren = -- totalIndianChildren;
                    }else{
                        totalChildren = -- totalForeignerChildren;
                    }
//                    totalChildren--;
                    txtTotalChildrenNo.setText(String.valueOf(totalChildren));
                    calculatePrice();
                }
            }
        });

        studentFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalStudent = 0;
                if(selectedPrice.getType().equals("I")){
                    totalStudent = ++ totalIndianStudent;
                }else{
                    totalStudent = ++ totalForeignerStudent;
                }
//                totalStudent++;
                txtTotalStudentNo.setText(String.valueOf(totalStudent));
                calculatePrice();
            }
        });

        studentFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalStudent = txtTotalStudentNo.getText().toString();
                if (!txtTotalStudent.equals("0")){
                    int totalStudent = 0;
                    if(selectedPrice.getType().equals("I")){
                        totalStudent = --totalIndianStudent;
                    }else{
                        totalStudent = --totalForeignerStudent;
                    }
//                    totalStudent--;
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

//        totalCams= Integer.parseInt(txtTotalCameraNo.getText().toString());
//        totalDSLR = Integer.parseInt(txtTotalDSLRNo.getText().toString());
//        totalVideo = Integer.parseInt(txtTotalVideoNo.getText().toString());

        int totalCams = 0;
        int totalDSLR = 0;
        int totalVideo = 0;
        if(selectedPrice.getType().equals("I")){
            totalCams = totalIndianCams;
            totalDSLR = totalIndianDSLR;
            totalVideo = totalIndianVideo;
        }else{
            totalCams = totalForeignerCams;
            totalDSLR = totalForeignerDSLR;
            totalVideo = totalForeignerVideo;
        }
        txtTotalCameraNo.setText(String.valueOf(totalCams));
        txtTotalDSLRNo.setText(String.valueOf(totalDSLR));
        txtTotalVideoNo.setText(String.valueOf(totalVideo));

        cameraFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalCams = 0;
                if(selectedPrice.getType().equals("I")){
                    totalCams = ++totalIndianCams;
                }else{
                    totalCams = ++totalForeignerCams;
                }
//                totalCams++;
                txtTotalCameraNo.setText(String.valueOf(totalCams));
                calculatePrice();
            }
        });

        cameraFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalCamera = txtTotalCameraNo.getText().toString();
                if (!txtTotalCamera.equals("0")){
                    int totalCams = 0;
                    if(selectedPrice.getType().equals("I")){
                        totalCams = --totalIndianCams;
                    }else{
                        totalCams = --totalForeignerCams;
                    }
//                    totalCams--;
                    txtTotalCameraNo.setText(String.valueOf(totalCams));
                    calculatePrice();
                }
            }
        });

        dslrFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalDSLR = 0;
                if(selectedPrice.getType().equals("I")){
                    totalDSLR = ++totalIndianDSLR;
                }else{
                    totalDSLR = ++totalForeignerDSLR;
                }
//                totalDSLR++;
                txtTotalDSLRNo.setText(String.valueOf(totalDSLR));
                calculatePrice();
            }
        });

        dslrFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalDSLR = txtTotalDSLRNo.getText().toString();
                if (!txtTotalDSLR.equals("0")){
                    int totalDSLR = 0;
                    if(selectedPrice.getType().equals("I")){
                        totalDSLR = --totalIndianDSLR;
                    }else{
                        totalDSLR = --totalForeignerDSLR;
                    }
//                    totalDSLR--;
                    txtTotalDSLRNo.setText(String.valueOf(totalDSLR));
                    calculatePrice();
                }
            }
        });

        videoFigureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalVideo = 0;
                if(selectedPrice.getType().equals("I")){
                    totalVideo = ++totalIndianVideo;
                }else{
                    totalVideo = ++totalForeignerVideo;
                }
//                totalVideo++;
                txtTotalVideoNo.setText(String.valueOf(totalVideo));
                calculatePrice();
            }
        });

        videoFigureNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTotalVideo = txtTotalVideoNo.getText().toString();
                if (!txtTotalVideo.equals("0")){
                    int totalVideo = 0;
                    if(selectedPrice.getType().equals("I")){
                        totalVideo = --totalIndianVideo;
                    }else{
                        totalVideo = --totalForeignerVideo;
                    }
//                    totalVideo--;
                    txtTotalVideoNo.setText(String.valueOf(totalVideo));
                    calculatePrice();
                }
            }
        });
    }

    private void calculatePrice(){
//        totalAdult = Integer.parseInt(txtTotalAdultNo.getText().toString());
//        totalChildren = Integer.parseInt(txtTotalChildrenNo.getText().toString());
//        totalStudent = Integer.parseInt(txtTotalStudentNo.getText().toString());
//        totalCams= Integer.parseInt(txtTotalCameraNo.getText().toString());
//        totalDSLR = Integer.parseInt(txtTotalDSLRNo.getText().toString());
//        totalVideo = Integer.parseInt(txtTotalVideoNo.getText().toString());

        double indianVisitorPrice  = 0;
        double totalCameraPrice = 0;
        double foreignVisitorPrice = 0;
        double foreignCameraPrice = 0;
        for(PriceDetails p: priceList){
            if(p.getType().equals("I")){
                indianVisitorPrice = p.getAdultPrice()*totalIndianAdult+p.getStudentPrice()*totalIndianStudent+p.getChildrenPrice()*totalIndianChildren;
                totalCameraPrice = p.getStillPrice()*totalIndianCams+p.getDslrPrice()*totalIndianDSLR+p.getVideoPrice()*totalIndianVideo;
            }else{
                foreignVisitorPrice = p.getAdultPrice()*totalForeignerAdult+p.getStudentPrice()*totalForeignerStudent+p.getChildrenPrice()*totalForeignerChildren;
                foreignCameraPrice = p.getStillPrice()*totalForeignerCams+p.getDslrPrice()*totalForeignerDSLR+p.getVideoPrice()*totalForeignerVideo;

            }
        }
//        visitors = (selectedPrice.getAdultPrice()*totalAdult)+(selectedPrice.getChildrenPrice() * totalChildren) + (selectedPrice.getStudentPrice()*totalStudent);
//        camera = (selectedPrice.getStillPrice()*totalCams) + (selectedPrice.getDslrPrice()*totalDSLR) + (selectedPrice.getVideoPrice()*totalVideo);
        visitors = indianVisitorPrice+foreignVisitorPrice;
        camera = totalCameraPrice+foreignCameraPrice;
        amount = visitors + camera;

        updatePriceView();
        updateTicketDetails();
    }

    private void updateTicketDetails() {
        String indian = "A("+totalIndianAdult+")  C("+totalIndianChildren+")  S("+totalIndianStudent+")  ";
        indian += "Cam("+totalIndianCams+")  DSLR("+totalIndianDSLR+")  V("+totalIndianVideo+")";
        tvIndianDetails.setText(indian);

        String foreigner = "A("+totalForeignerAdult+")  C("+totalForeignerChildren+")  S("+totalForeignerStudent+")  ";
        foreigner += "Cam("+totalForeignerCams+")  DSLR("+totalForeignerDSLR+")  V("+totalForeignerVideo+")";
        tvForeignerDetails.setText(foreigner);
    }

    private void updatePriceView() {
        totalVisitorPrice.setText(String.valueOf(visitors));
        totalCameraPrice.setText(String.valueOf(camera));
        totalAmount.setText(String.valueOf(amount));
    }


}