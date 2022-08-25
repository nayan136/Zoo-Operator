package in.amtron.zoooperator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import in.amtron.zoooperator.R;
import in.amtron.zoooperator.helper.SharePref;
import in.amtron.zoooperator.model.User;
import in.amtron.zoooperator.ui.LoginActivity;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragmentLog";

    private SharePref pref;
    private User user;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");

        pref = SharePref.getInstance(getActivity());
        Log.i(TAG, pref.get(SharePref.USER));
        String json = pref.get(SharePref.USER);
        Gson gson = new Gson();
        user = gson.fromJson(json,User.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvMobile = view.findViewById(R.id.tv_mobile);
        Button logout = view.findViewById(R.id.btn_logout);

        tvName.setText(user.getName());
        tvMobile.setText(user.getMobile());

        logout.setOnClickListener(v->{
            pref.remove(SharePref.USER);
            getActivity().finishAffinity();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
        });


        return view;
    }
}