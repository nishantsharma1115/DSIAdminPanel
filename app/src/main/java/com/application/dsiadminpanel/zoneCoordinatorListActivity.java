package com.application.dsiadminpanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.dsiadminpanel.Common.Constants;
import com.application.dsiadminpanel.ViewModel.dataViewModel;
import com.application.dsiadminpanel.adapters.coordinatorAdapter;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.application.dsiadminpanel.databinding.ActivityZoneCoordinatorListBinding;

import java.util.Objects;

public class zoneCoordinatorListActivity extends AppCompatActivity {

    ActivityZoneCoordinatorListBinding binding;
    dataViewModel viewModel;
    coordinatorAdapter adapter;
    String showOnly;
    private static final String ZONE_COORDINATOR = "Zone Coordinator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_zone_coordinator_list);
        viewModel = new ViewModelProvider(this).get(dataViewModel.class);

        showOnly = getIntent().getStringExtra("stateCoordinatorId");

        if (showOnly != null && showOnly.equals("All")) {
            viewModel.getAllZoneCoordinator().observe(this, new Observer<RequestCall>() {
                @Override
                public void onChanged(RequestCall requestCall) {
                    if (requestCall.getStatus() == 0) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.backgroundLayout.setAlpha((float) 0.4);
                    } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Finished")) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.backgroundLayout.setAlpha(1);
                        adapter.notifyDataSetChanged();
                    } else if (requestCall.getStatus() == 1 && requestCall.getMessage().equals("No data Found")) {
                        Toast.makeText(zoneCoordinatorListActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    } else if (requestCall.getStatus() == -1) {
                        Toast.makeText(zoneCoordinatorListActivity.this, "Failed while processing data", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            binding.showOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(zoneCoordinatorListActivity.this, trackEmployeeOnMapActivity.class);
                    intent.putExtra("Post", ZONE_COORDINATOR);
                    startActivity(intent);
                }
            });
        } else {
            viewModel.getZoneCoordinatorList(showOnly).observe(this, new Observer<RequestCall>() {
                @Override
                public void onChanged(RequestCall requestCall) {
                    if (requestCall.getStatus() == 0) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.backgroundLayout.setAlpha((float) 0.4);
                    } else if (requestCall.getStatus() == 1 && requestCall.getMessage().equals("Finished")) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.backgroundLayout.setAlpha(1);
                        adapter.notifyDataSetChanged();
                    } else if (requestCall.getStatus() == 1 && requestCall.getMessage().equals("No data Found")) {
                        Toast.makeText(zoneCoordinatorListActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    } else if (requestCall.getStatus() == -1) {
                        Toast.makeText(zoneCoordinatorListActivity.this, "Failed while processing data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        binding.showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(zoneCoordinatorListActivity.this, trackEmployeeOnMapActivity.class);
                intent.putExtra("Post", ZONE_COORDINATOR);
                startActivity(intent);
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        if (showOnly != null) {
            if (showOnly.equals("All")) {
                adapter = new coordinatorAdapter(this, ZONE_COORDINATOR, Objects.requireNonNull(viewModel.getAllZoneCoordinator().getValue()).getZoneCoordinators());
                RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(this);
                binding.recyclerView.setLayoutManager(linearLayout);
                binding.recyclerView.setAdapter(adapter);
            } else {
                adapter = new coordinatorAdapter(this, ZONE_COORDINATOR, Objects.requireNonNull(viewModel.getZoneCoordinatorList(showOnly).getValue()).getZoneCoordinators());
                RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(this);
                binding.recyclerView.setLayoutManager(linearLayout);
                binding.recyclerView.setAdapter(adapter);
            }
        }
    }
}
