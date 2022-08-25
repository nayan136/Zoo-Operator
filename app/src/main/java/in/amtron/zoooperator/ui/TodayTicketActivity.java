package in.amtron.zoooperator.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.amtron.zoooperator.R;
import in.amtron.zoooperator.adapter.OnRecyclerViewItemClickListener;
import in.amtron.zoooperator.adapter.TicketHistoryAdapter;
import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.helper.DateHelper;
import in.amtron.zoooperator.helper.SharePref;

public class TodayTicketActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    private TicketHistoryAdapter adapter;
    private List<Booking> bookingList = new ArrayList<>();
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_ticket);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Today's Tickets");

        database = AppDatabase.getDatabase(getApplicationContext());
        bookingList = database.bookingDao().getTodayBookings(DateHelper.mysqlDateFormat(DateHelper.getToday()));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        adapter = new TicketHistoryAdapter(bookingList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClickListener(int position, String type) {

        Booking booking = bookingList.get(position);
        Gson gson = new Gson();
        String json = gson.toJson(booking);
        if(type.equals("print")){
            Intent i = new Intent(TodayTicketActivity.this, TicketActivity.class);
            i.putExtra("booking",json);
            startActivity(i);
        }else if(type.equals("payment")){
            Intent i = new Intent(TodayTicketActivity.this, PaymentActivity.class);
            i.putExtra("booking",json);
            startActivity(i);
        }

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