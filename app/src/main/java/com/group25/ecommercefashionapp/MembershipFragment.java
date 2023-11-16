package com.group25.ecommercefashionapp;

import static com.group25.ecommercefashionapp.ActionItem.getActionItems;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class MembershipFragment extends Fragment implements OnItemClickListener {
    MainActivity mainActivity;
    Context context = null;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private MaterialCardView imageCardView;
    List<ActionItem> items;

    public MembershipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.membership, container, false);
        imageCardView = view.findViewById(R.id.imageCardView);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        // Initialize views and handle logic specific to this fragment
        recyclerView = view.findViewById(R.id.recyclerView);
        items = getActionItems();

        context= getActivity();
        mainActivity = (MainActivity) getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        CustomMembershipActionAdapter adapter = new CustomMembershipActionAdapter(items,  this);
//        recyclerView.addOnItemTouchListener((adapterView, view, i, l)
//                        -> viewChose.setText(
//                        getString(R.string.you_choose, items.get(i).getName())
//                )
//        );
        recyclerView.setAdapter(adapter);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // Adjust the translationY of the background view to create the parallax effect
            final int halfwayPoint = imageCardView.getHeight() / 2;

            float alpha = 1.0f - (float) scrollY / halfwayPoint;
            alpha = Math.max(0.0f, alpha); // Ensure alpha doesn't go below 0

            // Set the alpha value to the background image
            imageCardView.setAlpha(alpha);

            // Adjust the translationY of the background view to create the parallax effect
            imageCardView.setTranslationY(scrollY * 0.5f);
        });
        return view;
    }
    @Override
    public void onItemClick(ActionItem item) {

        Log.d("MainActivity", "onItemClick: " + item.getName());
    }
}