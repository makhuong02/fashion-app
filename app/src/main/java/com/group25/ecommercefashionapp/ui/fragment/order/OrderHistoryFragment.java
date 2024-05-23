package com.group25.ecommercefashionapp.ui.fragment.order;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.OrderHistoryItemAdapter;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.layoutmanager.LinearLayoutManagerWrapper;
import com.group25.ecommercefashionapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment {
    TextView orderCountTextView;
    RecyclerView orderHistoryRecyclerView;
    MaterialCardView noOrderHistoryCardView;
    AppCompatButton returnToMembershipButton;
    OrderHistoryItemAdapter adapter;
    List<OrderHistoryItem> orderHistoryList = new ArrayList<>();
    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        initViews(view);

        returnToMembershipButton.setOnClickListener(v -> {
            getMainActivityInstance().navController.popBackStack();
            getActivity().onBackPressed();
        });

        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, true);
        orderHistoryRecyclerView.setLayoutManager(linearLayoutManagerWrapper);

        adapter = new OrderHistoryItemAdapter(orderHistoryList, getContext());
        orderHistoryRecyclerView.setAdapter(adapter);

        setUpSwipeRefresh();

        fetchOrderHistory();

        return view;
    }

    public void fetchOrderHistory() {
        UserRepository.getInstance().getOrders(new Callback<List<OrderHistoryItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<OrderHistoryItem>> call, @NonNull Response<List<OrderHistoryItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderHistoryList = response.body();
                    updateUI();
                } else {
                    showNoOrderHistory();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderHistoryItem>> call, @NonNull Throwable throwable) {
                showNoOrderHistory();
            }
        });
    }

    private void updateUI() {
        if (orderHistoryList.isEmpty()) {
            noOrderHistoryCardView.setVisibility(View.VISIBLE);
            orderHistoryRecyclerView.setVisibility(View.GONE);
        } else {
            noOrderHistoryCardView.setVisibility(View.GONE);
            orderHistoryRecyclerView.setVisibility(View.VISIBLE);
        }
        orderCountTextView.setText(getString(R.string.text_order_history_count_item, orderHistoryList.size()));
        adapter.updateOrderHistoryList(orderHistoryList);
    }

    private void showNoOrderHistory() {
        noOrderHistoryCardView.setVisibility(View.VISIBLE);
        orderHistoryRecyclerView.setVisibility(View.GONE);
        orderCountTextView.setText(getString(R.string.text_order_history_count_item, 0));
    }

    private void setUpSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchOrderHistory();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void initViews(View view){
        orderCountTextView = view.findViewById(R.id.text_order_history_count);
        orderHistoryRecyclerView = view.findViewById(R.id.order_history_recycler_view);
        noOrderHistoryCardView = view.findViewById(R.id.no_order_card_view);
        returnToMembershipButton = view.findViewById(R.id.return_to_membership_button);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
    }
}