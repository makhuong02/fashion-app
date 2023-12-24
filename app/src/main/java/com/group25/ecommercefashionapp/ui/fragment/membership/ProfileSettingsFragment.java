package com.group25.ecommercefashionapp.ui.fragment.membership;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;
import com.group25.ecommercefashionapp.R;

public class ProfileSettingsFragment extends Fragment {
    MaterialToolbar toolbar;
    MainActivity mainActivity;
    CardView logoutCardView;
    TextView emailTextview, phoneTextview;
    MySharedPreferences sharedPreferences;

    public ProfileSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_settings, container, false);

        // Initialize views and handle logic specific to this fragment
        mainActivity = (MainActivity) getActivity();
        toolbar = view.findViewById(R.id.topAppBar);
        logoutCardView = view.findViewById(R.id.logoutCardView);
        emailTextview = view.findViewById(R.id.textviewEmail);
        phoneTextview = view.findViewById(R.id.textviewPhone);

        toolbar.setNavigationOnClickListener(v -> mainActivity.navController.popBackStack());

        emailTextview.setText("Name: " + UserStatus.currentUser.getEmail());
        phoneTextview.setText("Phone: " + UserStatus.currentUser.getPhoneNumber());

        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserStatus._isLoggedIn = false;
                UserStatus.currentUser = null;
                UserStatus.access_token = null;
                sharedPreferences = new MySharedPreferences(getActivity().getApplication());
                sharedPreferences.updateUserLoginStatus();

                mainActivity.navController.navigate(R.id.membershipBotNav);
            }
        });

        mainActivity.updateNavigationBarState(R.id.membershipBotNav);
        return view;
    }
}
