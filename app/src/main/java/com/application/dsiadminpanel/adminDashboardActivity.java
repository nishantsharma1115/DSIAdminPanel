package com.application.dsiadminpanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.application.dsiadminpanel.Common.Constants;
import com.application.dsiadminpanel.ViewModel.dataViewModel;
import com.application.dsiadminpanel.dataClass.CountCoordinators;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.application.dsiadminpanel.databinding.ActivityAdminDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;

public class adminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAdminDashboardBinding binding;
    dataViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard);
        viewModel = new ViewModelProvider(this).get(dataViewModel.class);

        viewModel.getCountCoordinator().observe(this, new Observer<RequestCall>() {
            @Override
            public void onChanged(RequestCall requestCall) {
                if (requestCall.getStatus() == Constants.OPERATION_IN_PROGRESS) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.adminDashboard.setAlpha((float) 0.4);
                } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Finished")) {
                    binding.setCount(requestCall.getCount());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.adminDashboard.setAlpha(1);
                } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("No data Found")) {
                    binding.setCount(new CountCoordinators());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.adminDashboard.setAlpha(1);
                }
            }
        });

        binding.showPendingForm.setOnClickListener(this);
        binding.showStateCoordinator.setOnClickListener(this);
        binding.showZoneCoordinator.setOnClickListener(this);
        binding.showDistributor.setOnClickListener(this);
        binding.showDistrictCoordinator.setOnClickListener(this);
        binding.showBlockCoordinator.setOnClickListener(this);
        binding.showNavPanchayat.setOnClickListener(this);
        binding.showCustomers.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.showPendingForm) {
            startActivity(new Intent(adminDashboardActivity.this, pendingFormListActivity.class));
        } else if (view.getId() == R.id.showStateCoordinator) {
            startActivity(new Intent(adminDashboardActivity.this, stateCoordinatorListActivity.class));
        } else if (view.getId() == R.id.showZoneCoordinator) {
            Intent intent = new Intent(adminDashboardActivity.this, zoneCoordinatorListActivity.class);
            intent.putExtra("stateCoordinatorId", "All");
            startActivity(intent);
        } else if (view.getId() == R.id.showZoneCoordinator) {
            Intent intent = new Intent(adminDashboardActivity.this, blockCoordinatorListActivity.class);
            intent.putExtra("districtCoordinatorId", "All");
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, loginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
