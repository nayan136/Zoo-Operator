package in.amtron.zoooperator.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.amtron.zoooperator.R;
import in.amtron.zoooperator.adapter.OnRecyclerViewItemClickListener;
import in.amtron.zoooperator.adapter.TicketHistoryAdapter;
import in.amtron.zoooperator.data.Common;
import in.amtron.zoooperator.database.AppDatabase;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.helper.SharePref;

public class TicketHistoryActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    private static final String TAG = "TicketHistoryActLog";

    private TicketHistoryAdapter adapter;
    private List<Booking> bookingList = new ArrayList<>();
    private AppDatabase database;

    private SharePref pref;

    private int pageNo = 0;
    private Boolean stopLoading = false;
    private Boolean isScrolling = false;
    private Integer currentItems, totalItems, scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ticket History");

        pref = SharePref.getInstance(this);

        database = AppDatabase.getDatabase(getApplicationContext());
        bookingList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        adapter = new TicketHistoryAdapter(bookingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        fetchData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if(!stopLoading && isScrolling && (currentItems+scrollOutItems == totalItems)){
                    isScrolling = false;
                    // data fetch
                    fetchData();

                }
            }
        });
    }

    private void fetchData(){
        int limit = Common.MAX_DB_LIMIT;
        int offset = pageNo == 0?0:(pageNo*limit-1);
        pageNo++;
        List<Booking> bookings = database.bookingDao().getBookings(limit,offset);
        if(bookings.size() < Common.MAX_DB_LIMIT){
            stopLoading = true;
        }
        for (Booking b: bookings){
            adapter.addBooking(b);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickListener(int position, String type) {

        Booking booking = bookingList.get(position);
        Gson gson = new Gson();
        String json = gson.toJson(booking);
        if(type.equals("print")){
            Intent i = new Intent(TicketHistoryActivity.this, TicketActivity.class);
            i.putExtra("booking",json);
            startActivity(i);
        }else if(type.equals("payment")){
            pref.put(SharePref.BOOKING,json);
            Intent i = new Intent(TicketHistoryActivity.this, PaymentActivity.class);
            i.putExtra("booking",json);
            startActivity(i);
        }

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