package com.application.dsiadminpanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.application.dsiadminpanel.Common.Constants;
import com.application.dsiadminpanel.ViewModel.dataViewModel;
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.application.dsiadminpanel.databinding.ActivityEmployeeDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class employeeDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityEmployeeDashboardBinding binding;
    dataViewModel viewModel;
    FirebaseUser user;
    Employee employee = new Employee();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_dashboard);
        viewModel = new ViewModelProvider(this).get(dataViewModel.class);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            viewModel.getEmployeeDetails(user.getUid()).observe(this, new Observer<RequestCall>() {
                @Override
                public void onChanged(RequestCall requestCall) {
                    if (requestCall.getStatus() == Constants.OPERATION_IN_PROGRESS) {
                        binding.dashboardProgressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        binding.dashboardBackground.setAlpha((float) 0.4);
                    } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Finished")) {
                        employee = requestCall.getEmployee();
                        updateUi(employee);
                        binding.dashboardProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        binding.dashboardBackground.setAlpha(1);
                    }
                }
            });
        }

        binding.btnViewAll.setOnClickListener(this);
    }

    private void updateUi(Employee employee) {
        binding.setEmployee(employee);
        switch (employee.getPost()) {
            case "State Coordinator":
                binding.btnViewAll.setText(R.string.show_zone_coordinator);
                break;
            case "Zone Coordinator":
                binding.btnViewAll.setText(R.string.show_distributor);
                break;
            case "Distributor":
                binding.btnViewAll.setText(R.string.show_district_coordinator);
                break;
            case "District Coordinator":
                binding.btnViewAll.setText(R.string.show_block_coordinator);
                break;
            case "Block Coordinator":
                binding.btnViewAll.setText(R.string.show_nav_panchayat);
                break;
            case "Nav Panchayat":
                binding.btnViewAll.setText(R.string.show_customers);
                break;
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_viewAll) {
            showNextCoordinators(employee);
        }
    }

    private void showNextCoordinators(Employee employee) {
        switch (employee.getPost()) {
            case "State Coordinator": {
                Intent intent = new Intent(this, zoneCoordinatorListActivity.class);
                intent.putExtra("stateCoordinatorId", employee.getEmployeeId());
                startActivity(intent);
                break;
            }
            case "Zone Coordinator": {
                Intent intent = new Intent(this, distributorListActivity.class);
                intent.putExtra("zoneCoordinatorId", employee.getEmployeeId());
                startActivity(intent);
                break;
            }
            case "Distributor": {
                Intent intent = new Intent(this, districtCoordinatorListActivity.class);
                intent.putExtra("distributorId", employee.getEmployeeId());
                startActivity(intent);
                break;
            }
            case "District Coordinator": {
                Intent intent = new Intent(this, blockCoordinatorListActivity.class);
                intent.putExtra("districtCoordinatorId", employee.getEmployeeId());
                startActivity(intent);
                break;
            }
            case "Block Coordinator": {
                Intent intent = new Intent(this, navPanchayatListActivity.class);
                intent.putExtra("blockCoordinatorId", employee.getEmployeeId());
                startActivity(intent);
                break;
            }
            case "Nav Panchayat": {
                Intent intent = new Intent(this, customerListActivity.class);
                intent.putExtra("navPanchayatId", employee.getEmployeeId());
                startActivity(intent);
                break;
            }
        }
    }
}
