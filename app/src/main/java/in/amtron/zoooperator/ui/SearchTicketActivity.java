package in.amtron.zoooperator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.amtron.zoooperator.R;
import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.helper.Util;
import in.amtron.zoooperator.helper.Validator;

public class SearchTicketActivity extends AppCompatActivity {

    @BindView(R.id.tv_no_ticket)
    TextView tvNoTicket;
    @BindView(R.id.et_ticket_no)
    EditText etTicketNo;
    @BindView(R.id.btn_search)
    Button btnSearch;

    @BindView(R.id.ll_container)
    LinearLayout llContainer;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ticket);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Ticket");

        database = AppDatabase.getDatabase(getApplicationContext());

        btnSearch.setOnClickListener(v->{
            Validator validator = new Validator();
            validator.setData(etTicketNo,"required");
            if(validator.validate()){
                Booking booking = searchTicket(etTicketNo.getText().toString());
                updateView(booking);
            }
        });
    }

    private void updateView(Booking booking) {
        llContainer.removeAllViews();
        if(booking == null){
            tvNoTicket.setVisibility(View.VISIBLE);
        }else{
            tvNoTicket.setVisibility(View.GONE);
//            ViewGroup viewGroup = findViewById(android.R.id.content);
//            View view = LayoutInflater.from(this).inflate(R.layout.layout_ticket_history, viewGroup, false);

            LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.layout_ticket_history, null);

            TextView tvTicketNo, tvDate, tvAmount, tvIndian, tvForeigner, tvPayment;
            Button btnUpdatePayment, btnPrintTicket;

            tvTicketNo = itemView.findViewById(R.id.tv_ticket_no);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvIndian = itemView.findViewById(R.id.tv_ind_details);
            tvForeigner = itemView.findViewById(R.id.tv_fore_details);
            tvPayment = itemView.findViewById(R.id.tv_payment);

            btnUpdatePayment = itemView.findViewById(R.id.btn_update_payment);
            btnPrintTicket = itemView.findViewById(R.id.btn_print_ticket);

            // set View
            tvTicketNo.setText(booking.getTicketNo());
            tvDate.setText(booking.getDate());
            tvAmount.setText(Util.moneyFormat(booking.getPayableAmount()));

            tvPayment.setText(booking.getPayment() == 1?"Paid":"Not Paid");

            String indian = "A("+booking.getIndianAdults()+")  C("+booking.getIndianChildrens()+")  S("+booking.getIndianStudents()+")  ";
            indian += "Cam("+booking.getIndianStillCameras()+")  DSLR("+booking.getIndianSlrCameras()+")  V("+booking.getIndianVideoCameras()+")";
            tvIndian.setText(indian);

            String foreigner = "A("+booking.getForeignAdults()+")  C("+booking.getForeignChildrens()+")  S("+booking.getForeignStudents()+")  ";
            foreigner += "Cam("+booking.getForeignStillCameras()+")  DSLR("+booking.getForeignSlrCameras()+")  V("+booking.getForeignVideoCameras()+")";
            tvForeigner.setText(foreigner);

            if(booking.getPayment() == 1){
                btnPrintTicket.setVisibility(View.VISIBLE);
                btnUpdatePayment.setVisibility(View.GONE);
            }else{
                btnPrintTicket.setVisibility(View.GONE);
                btnUpdatePayment.setVisibility(View.VISIBLE);
            }

            String json = new Gson().toJson(booking);

            btnPrintTicket.setOnClickListener(v->{
                Intent i = new Intent(SearchTicketActivity.this, TicketActivity.class);
                i.putExtra("booking",json);
                startActivity(i);
            });

            btnUpdatePayment.setOnClickListener(v->{
                Intent i = new Intent(SearchTicketActivity.this, PaymentActivity.class);
                i.putExtra("booking",json);
                startActivity(i);
            });

            // ****** end view ******

            llContainer.addView(itemView);
        }
    }

    private Booking searchTicket(String ticketNo) {
        return database.bookingDao().getBooking(ticketNo);
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