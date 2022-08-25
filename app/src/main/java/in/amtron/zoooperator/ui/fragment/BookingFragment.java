package in.amtron.zoooperator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.amtron.zoooperator.R;
import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.helper.CalculatePrice;
import in.amtron.zoooperator.helper.DateHelper;
import in.amtron.zoooperator.helper.SharePref;
import in.amtron.zoooperator.helper.Validator;
import in.amtron.zoooperator.model.PriceDetails;
import in.amtron.zoooperator.model.ServicePriceDetails;
import in.amtron.zoooperator.ui.PaymentActivity;
import in.amtron.zoooperator.viewmodel.ModeViewModel;

public class BookingFragment extends Fragment {

    private static final String TAG = "BookingFragmentLog";

    private EditText txtPhoneNumber;
    private RadioGroup rgNation;
    private RadioButton rbIndian, rbForeign;
    private TextView txtAdultPrice, txtChildrenPrice, txtStudentPrice, txtCameraPrice, txtDSLRPrice, txtVideoPrice, txtTotalAdultNo, txtTotalChildrenNo,
            txtTotalStudentNo, txtTotalCameraNo, txtTotalDSLRNo, txtTotalVideoNo, totalVisitorPrice, totalCameraPrice, totalAmount;
    private Button adultFigureNegate, adultFigureAdd, childrenFigureNegate, childrenFigureAdd, studentFigureNegate, studentFigureAdd,
            cameraFigureNegate, cameraFigureAdd, dslrFigureNegate, dslrFigureAdd, videoFigureNegate, videoFigureAdd, btnConfirm;

    private TextView tvIndianDetails, tvForeignerDetails;

    private ServicePriceDetails servicePriceDetails;
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
    private ModeViewModel model;
    private int currentMode = 0;
    private SharePref pref;

    public BookingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Booking");

        database = AppDatabase.getDatabase(getActivity().getApplicationContext());

        pref = SharePref.getInstance(getActivity());

        model = new ViewModelProvider(this).get(ModeViewModel.class);
        final Observer<Integer> nameObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer newName) {
                // Update the UI, in this case, a TextView.
                currentMode = newName;
                pref.put(SharePref.CURRENT_MODE, currentMode);
            }
        };

        model.getCurrentMode().observe(this, nameObserver);
        currentMode = pref.getInt(SharePref.CURRENT_MODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_booking, container, false);
        createPrice();
        setView(view);
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

        btnConfirm.setOnClickListener(v1->{
//            Log.i(TAG, "Mode - "+currentMode);
            saveOffline();
        });

        return view;
    }

    private void saveOnline(){

    }

    private void saveOffline(){

        Validator validator = new Validator();
        validator.setData(txtPhoneNumber,"required,mobile");

        if(validator.validate()){
            int totalVisitors = totalIndianAdult+totalIndianChildren+totalIndianStudent+totalForeignerAdult+totalForeignerChildren+totalForeignerStudent;
            CalculatePrice calculatePrice = new CalculatePrice(amount,totalVisitors,servicePriceDetails);

            if(calculatePrice.getTotalAmount() > 0){
                String today = DateHelper.mysqlDateFormat(DateHelper.getToday());
                String ticketNo = "ASZ"+String.valueOf(System.currentTimeMillis());

                Booking b = new Booking();
                b.setTicketNo(ticketNo);
                b.setDate(today);
                b.setMobile(txtPhoneNumber.getText().toString());

                b.setIndianAdults(totalIndianAdult);
                b.setIndianChildrens(totalIndianChildren);
                b.setIndianStudents(totalIndianStudent);
                b.setIndianStillCameras(totalIndianCams);
                b.setIndianSlrCameras(totalIndianDSLR);
                b.setIndianVideoCameras(totalIndianVideo);

                b.setForeignAdults(totalForeignerAdult);
                b.setForeignChildrens(totalForeignerChildren);
                b.setForeignStudents(totalForeignerStudent);
                b.setForeignStillCameras(totalForeignerCams);
                b.setForeignSlrCameras(totalForeignerDSLR);
                b.setForeignVideoCameras(totalForeignerVideo);

                b.setPayment(0);
                b.setServer(0);

                b.setPrice(amount);
//            b.setIpg_percent(1.5);
                b.setIpg_amount(calculatePrice.getIpgAmount());
                b.setGst_amount(calculatePrice.getGstAmount());
                b.setService_chg(calculatePrice.getServiceCharge());
                b.setPayableAmount(calculatePrice.getTotalAmount());

                Gson gson = new Gson();
                String json = gson.toJson(b);
//            pref.put(SharePref.BOOKING,json);

                Long id = database.bookingDao().addBooking(b);

//            pref.put(SharePref.BOOKING_ID,id);
                if(id != null && id>0){
                    Intent i = new Intent(getActivity(), PaymentActivity.class);
                    i.putExtra("booking",json);
//                i.putExtra("booking_id",id);
                    startActivity(i);
                }
            }

        }


    }

    private void createPrice() {
        Gson gson = new Gson();
        servicePriceDetails = gson.fromJson(pref.get(SharePref.DATA),ServicePriceDetails.class);
        priceList = servicePriceDetails.getPrice();

//        PriceDetails p1 = new PriceDetails("I",30.00,10.00,20.00,100.00,200.00,300.00);
//        PriceDetails p2 = new PriceDetails("F",100.00,50.00,30.00,200.00,300.00,500.00);
//        priceList.add(p1);
//        priceList.add(p2);
    }

    private void setView(View v) {

        tvIndianDetails = v.findViewById(R.id.tv_ind_details);
        tvForeignerDetails = v.findViewById(R.id.tv_fore_details);

        txtPhoneNumber = v.findViewById(R.id.phoneNumber);
        rgNation = v.findViewById(R.id.rgNation);
        rbIndian = v.findViewById(R.id.rbIndian);
        rbForeign = v.findViewById(R.id.rbForeign);
        txtAdultPrice = v.findViewById(R.id.txtAdultPrice);
        txtChildrenPrice = v.findViewById(R.id.txtChildrenPrice);
        txtStudentPrice = v.findViewById(R.id.txtStudentPrice);
        txtCameraPrice = v.findViewById(R.id.txtCameraPrice);
        txtDSLRPrice = v.findViewById(R.id.txtDSLRPrice);
        txtVideoPrice = v.findViewById(R.id.txtVideoPrice);
        txtTotalAdultNo = v.findViewById(R.id.txtTotalAdultNo);
        txtTotalChildrenNo = v.findViewById(R.id.txtTotalChildrenNo);
        txtTotalStudentNo = v.findViewById(R.id.txtTotalStudentNo);
        txtTotalCameraNo = v.findViewById(R.id.txtTotalCameraNo);
        txtTotalDSLRNo = v.findViewById(R.id.txtTotalDSLRNo);
        txtTotalVideoNo = v.findViewById(R.id.txtTotalVideoNo);
        totalVisitorPrice = v.findViewById(R.id.totalVisitors);
        totalCameraPrice = v.findViewById(R.id.totalCamera);
        totalAmount = v.findViewById(R.id.totalAmount);
        adultFigureNegate = v.findViewById(R.id.adultFigureNegate);
        adultFigureAdd = v.findViewById(R.id.adultFigureAdd);
        childrenFigureNegate = v.findViewById(R.id.childrenFigureNegate);
        childrenFigureAdd = v.findViewById(R.id.childrenFigureAdd);
        studentFigureNegate = v.findViewById(R.id.studentFigureNegate);
        studentFigureAdd = v.findViewById(R.id.studentFigureAdd);
        cameraFigureNegate = v.findViewById(R.id.cameraFigureNegate);
        cameraFigureAdd = v.findViewById(R.id.cameraFigureAdd);
        dslrFigureNegate = v.findViewById(R.id.dslrFigureNegate);
        dslrFigureAdd = v.findViewById(R.id.dslrFigureAdd);
        videoFigureNegate = v.findViewById(R.id.videoFigureNegate);
        videoFigureAdd = v.findViewById(R.id.videoFigureAdd);
        btnConfirm = v.findViewById(R.id.btnConfirm);

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