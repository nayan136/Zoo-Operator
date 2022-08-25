package in.amtron.zoooperator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.amtron.zoooperator.R;
import in.amtron.zoooperator.ui.SearchTicketActivity;
import in.amtron.zoooperator.ui.TicketHistoryActivity;
import in.amtron.zoooperator.ui.TodayTicketActivity;

public class AccountFragment extends Fragment {

    @BindView(R.id.ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;
    @BindView(R.id.ll_today_history)
    LinearLayout llTodayHistory;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Account");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this,v);
        llProfile.setOnClickListener(v1->{
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, new ProfileFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        llHistory.setOnClickListener(v2->{
            Intent i = new Intent(getActivity(), TicketHistoryActivity.class);
            startActivity(i);
        });

        llTodayHistory.setOnClickListener(v3->{
            Intent i = new Intent(getActivity(), TodayTicketActivity.class);
            startActivity(i);
        });

        llSearch.setOnClickListener(v4->{
            Intent i = new Intent(getActivity(), SearchTicketActivity.class);
            startActivity(i);
        });

        return v;
    }
}