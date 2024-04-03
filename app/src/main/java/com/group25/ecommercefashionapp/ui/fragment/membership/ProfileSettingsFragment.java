package com.group25.ecommercefashionapp.ui.fragment.membership;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.cache.UserCache;
import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;
import com.group25.ecommercefashionapp.utilities.TokenUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        if (UserStatus.currentUser != null && UserCache.getInstance().containsUser(UserStatus.currentUser.getEmail())) {
            UserStatus.currentUser = UserCache.getInstance().getUser(UserStatus.currentUser.getEmail());
            updateUI(UserStatus.currentUser);
        } else {
            UserRepository.getInstance().fetchUserDetails(TokenUtils.bearerToken(UserStatus.access_token.token), getContext(), new Callback<UserProfile>() {
                @Override
                public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                    if (response.isSuccessful()) {
                        UserStatus.currentUser = response.body();
                        UserCache.getInstance().addUser(UserStatus.currentUser.getEmail(), UserStatus.currentUser);
                        updateUI(UserStatus.currentUser);
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        logoutCardView.setOnClickListener(v -> {
            UserStatus._isLoggedIn = false;
            UserStatus.currentUser = null;
            UserStatus.access_token = null;
            sharedPreferences = new MySharedPreferences(getActivity().getApplication());
            sharedPreferences.updateUserLoginStatus();

            mainActivity.navController.navigate(R.id.membershipBotNav);
        });

        mainActivity.updateNavigationBarState(R.id.membershipBotNav);
        return view;
    }

    public void updateUI(UserProfile user) {
        emailTextview.setText(String.format("Name: %s", user.getEmail()));
        phoneTextview.setText(String.format("Phone: %s", user.getPhoneNumber()));
    }

}
