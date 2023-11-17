package com.group25.ecommercefashionapp.ui.fragment.membership;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.MainActivity;
import com.group25.ecommercefashionapp.R;

public class ProfileSettingsFragment extends Fragment {
    MaterialToolbar toolbar;
    MainActivity mainActivity;
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

        toolbar.setNavigationOnClickListener(v -> mainActivity.navController.popBackStack());

        mainActivity.updateNavigationBarState(R.id.membershipBotNav);
        return view;
    }
}