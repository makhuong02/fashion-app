package com.group25.ecommercefashionapp.ui.fragment.order;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.OrderHistoryItemAdapter;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.layoutmanager.LinearLayoutManagerWrapper;

import java.util.List;

public class OrderHistoryFragment extends Fragment {
    TextView orderCountTextView;
    RecyclerView orderHistoryRecyclerView;
    MaterialCardView noOrderHistoryCardView;
    AppCompatButton returnToMembershipButton;
    OrderHistoryItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UserInteraction UserInteraction = getMainActivityInstance().userInteraction;
        List<OrderHistoryItem> orderHistoryList = UserInteraction.getOrderList();

        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        orderCountTextView = view.findViewById(R.id.text_order_history_count);
        orderHistoryRecyclerView = view.findViewById(R.id.order_history_recycler_view);
        noOrderHistoryCardView = view.findViewById(R.id.no_order_card_view);
        returnToMembershipButton = view.findViewById(R.id.return_to_membership_button);

        if (orderHistoryList.size() == 0) {
            noOrderHistoryCardView.setVisibility(View.VISIBLE);
            orderHistoryRecyclerView.setVisibility(View.GONE);
        }
        else {
            noOrderHistoryCardView.setVisibility(View.GONE);
            orderHistoryRecyclerView.setVisibility(View.VISIBLE);
        }

        orderCountTextView.setText(getString(R.string.text_order_history_count_item, orderHistoryList.size()));

        returnToMembershipButton.setOnClickListener(v -> {
            getMainActivityInstance().navController.popBackStack();
            getActivity().onBackPressed();
        });

        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, true);
        orderHistoryRecyclerView.setLayoutManager(linearLayoutManagerWrapper);

        adapter = new OrderHistoryItemAdapter(orderHistoryList, getContext());
        orderHistoryRecyclerView.setAdapter(adapter);

        return view;
    }
}
