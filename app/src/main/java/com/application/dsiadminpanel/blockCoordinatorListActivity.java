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

import com.application.dsiadminpanel.ViewModel.dataViewModel;
import com.application.dsiadminpanel.adapters.coordinatorAdapter;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.application.dsiadminpanel.databinding.ActivityBlockCoordinatorListBinding;

import java.util.Objects;

public class blockCoordinatorListActivity extends AppCompatActivity {

    ActivityBlockCoordinatorListBinding binding;
    dataViewModel viewModel;
    coordinatorAdapter adapter;
    String showOnly;
    private static final String BLOCK_COORDINATOR = "Block Coordinator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_block_coordinator_list);
        viewModel = new ViewModelProvider(this).get(dataViewModel.class);

        showOnly = getIntent().getStringExtra("districtCoordinatorId");

        if (showOnly != null) {
            if (showOnly.equals("All")) {
                viewModel.getAllBlockCoordinator().observe(this, new Observer<RequestCall>() {
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
                            Toast.makeText(blockCoordinatorListActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        } else if (requestCall.getStatus() == -1) {
                            Toast.makeText(blockCoordinatorListActivity.this, "Failed while processing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                viewModel.getBlockCoordinatorList(showOnly).observe(this, new Observer<RequestCall>() {
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
                            Toast.makeText(blockCoordinatorListActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        } else if (requestCall.getStatus() == -1) {
                            Toast.makeText(blockCoordinatorListActivity.this, "Failed while processing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        binding.showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(blockCoordinatorListActivity.this, trackEmployeeOnMapActivity.class);
                intent.putExtra("Post", BLOCK_COORDINATOR);
                startActivity(intent);
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        if (showOnly != null) {
            if (showOnly.equals("All")) {
                if (Objects.requireNonNull(viewModel.getAllBlockCoordinator().getValue()).getBlockCoordinators() != null) {
                    binding.noDataFound.setVisibility(View.GONE);
                    adapter = new coordinatorAdapter(this, BLOCK_COORDINATOR, Objects.requireNonNull(viewModel.getAllBlockCoordinator().getValue()).getBlockCoordinators());
                    RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(this);
                    binding.recyclerView.setLayoutManager(linearLayout);
                    binding.recyclerView.setAdapter(adapter);
                } else {
                    binding.noDataFound.setVisibility(View.VISIBLE);
                }
            } else {
                if (Objects.requireNonNull(viewModel.getBlockCoordinatorList(showOnly).getValue()).getBlockCoordinators() != null) {
                    binding.noDataFound.setVisibility(View.GONE);
                    adapter = new coordinatorAdapter(this, BLOCK_COORDINATOR, Objects.requireNonNull(viewModel.getBlockCoordinatorList(showOnly).getValue()).getBlockCoordinators());
                    RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(this);
                    binding.recyclerView.setLayoutManager(linearLayout);
                    binding.recyclerView.setAdapter(adapter);
                } else {
                    binding.noDataFound.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
