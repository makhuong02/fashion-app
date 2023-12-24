package com.group25.ecommercefashionapp.ui.fragment.membership;

import static com.group25.ecommercefashionapp.data.ActionItem.getActionItems;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.ActionItem;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.activity.MapsActivity;

import java.util.List;

public class MembershipFragment extends Fragment implements OnItemClickListener {
    MainActivity mainActivity;
    Context context = null;
    private NestedScrollView nestedScrollView;
    private BottomNavigationView bottomNavigationView;
    private MaterialCardView imageCardView, profileCardView, settingsCardView, orderHistoryCardView;
    private CardView storeLocationCardView;
    private View view = null;
    List<ActionItem> items;
    float alpha = 1.0f;
    private final Handler myHandler = new Handler();

    public MembershipFragment() {
        // Required empty public constructor
    }

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.membership, container, false);

        }
        imageCardView = view.findViewById(R.id.imageCardView);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        profileCardView = view.findViewById(R.id.profileCardView);
        settingsCardView = view.findViewById(R.id.settingsCardView);
        orderHistoryCardView = view.findViewById(R.id.orderHistoryCardView);
        storeLocationCardView = view.findViewById(R.id.storeLocatorCardView);

        storeLocationCardView.setOnClickListener(v -> {
            LatLng storeLocation = new LatLng(10.762050, 106.681830);
            String storeName = "ChicCloth .Co";
            Intent intent = new Intent(this.getActivity(), MapsActivity.class);
            intent.putExtra("storeLocation", storeLocation);
            intent.putExtra("storeName", storeName);
            startActivity(intent);
        });

        items = getActionItems();

        context = getActivity();
        mainActivity = (MainActivity) getActivity();
        bottomNavigationView = mainActivity.findViewById(R.id.bottomNavigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

        // profileCardView.setOnClickListener(v -> mainActivity.navController.navigate(R.id.action_membershipBotNav_to_profileSettings));
        profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserStatus._isLoggedIn){
                    mainActivity.navController.navigate(R.id.action_membershipBotNav_to_profileSettings);
                }
                else {
                    mainActivity.navController.navigate(R.id.loginActivity);
                }
            }
        });

        orderHistoryCardView.setOnClickListener(v -> {
            if (UserStatus._isLoggedIn){
                mainActivity.navController.navigate(R.id.orderHistoryActivity);
            }
            else {
                mainActivity.navController.navigate(R.id.loginActivity);
            }
        });

        settingsCardView.setOnClickListener(v -> mainActivity.navController.navigate(R.id.settings));

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // Adjust the translationY of the background view to create the parallax effect
            final int halfwayPoint = imageCardView.getHeight() / 2;


//            if(scrollY > halfwayPoint) {
//                myHandler.post(hideImageCardView);
//            }
//            else{
//                myHandler.post(getHideImageCardView);
//            }
            alpha = 1.0f - (float) scrollY / halfwayPoint;
            alpha = Math.max(0.0f, alpha); // Ensure alpha doesn't go below 0

            // Set the alpha value to the background image
            imageCardView.setAlpha(alpha);

            // Adjust the translationY of the background view to create the parallax effect
            imageCardView.setTranslationY(scrollY * 0.5f);
        });
        return view;
    }

    private final Runnable hideImageCardView = new Runnable() {
        @Override
        public void run() {
            try {
                if (alpha > 0.0f) {
                    float step = 0.01f;
                    alpha -= step;

                    imageCardView.setAlpha(alpha);

                    myHandler.postDelayed(this, 50);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };
    private final Runnable getHideImageCardView = new Runnable() {
        @Override
        public void run() {
            try {
                if (alpha < 1.0f) {
                    float step = 0.01f;
                    alpha += step;

                    imageCardView.setAlpha(alpha);

                    myHandler.postDelayed(this, 50);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Override
    public void onItemClick(View view, Item item) {
        Log.d("MainActivity", "onItemClick: " + item.getName());
    }
}