package com.application.dsiadminpanel;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.dsiadminpanel.ViewModel.dataViewModel;
import com.application.dsiadminpanel.adapters.pendingApplicationAdapter;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.application.dsiadminpanel.databinding.ActivityPendingFormListBinding;

import java.util.Objects;

public class pendingFormListActivity extends AppCompatActivity {

    ActivityPendingFormListBinding binding;
    dataViewModel viewModel;
    pendingApplicationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pending_form_list);
        viewModel = new ViewModelProvider(this).get(dataViewModel.class);

        viewModel.getPendingEmployeeList().observe(this, new Observer<RequestCall>() {
            @Override
            public void onChanged(RequestCall requestCall) {
                if (requestCall.getStatus() == 0) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else if (requestCall.getStatus() == 1 && requestCall.getMessage().equals("Finished")) {
                    binding.progressBar.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                } else if (requestCall.getStatus() == 1 && requestCall.getMessage().equals("No data Found")) {
                    Toast.makeText(pendingFormListActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                } else if (requestCall.getStatus() == -1) {
                    Toast.makeText(pendingFormListActivity.this, "Failed while processing data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new pendingApplicationAdapter(this, Objects.requireNonNull(viewModel.getPendingEmployeeList().getValue()).getPendingEmployees());
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayout);
        binding.recyclerView.setAdapter(mAdapter);
    }
}
