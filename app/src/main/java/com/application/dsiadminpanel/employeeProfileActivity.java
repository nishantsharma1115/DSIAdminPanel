package com.application.dsiadminpanel;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.application.dsiadminpanel.Common.Constants;
import com.application.dsiadminpanel.ViewModel.dataViewModel;
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.application.dsiadminpanel.databinding.ActivityEmployeeProfileBinding;

public class employeeProfileActivity extends AppCompatActivity {

    ActivityEmployeeProfileBinding binding;
    dataViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_profile);
        viewModel = new ViewModelProvider(this).get(dataViewModel.class);

        String userId = getIntent().getStringExtra("userId");

        if (userId != null) {
            viewModel.getEmployeeDetails(userId).observe(this, new Observer<RequestCall>() {
                @Override
                public void onChanged(RequestCall requestCall) {
                    if (requestCall.getStatus() == Constants.OPERATION_IN_PROGRESS) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.backgroundLayout.setAlpha((float) 0.4);
                    } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Finished")) {
                        Employee employee = requestCall.getEmployee();
                        String address = employee.getHouse_no() + " " + employee.getStreet_no() + " " + employee.getBlock() + " " + employee.getLandmark() + " " + employee.getVillage() + " " + employee.getPostOffice() + " " + employee.getPoliceStation() + " " + employee.getCity() + " " + employee.getDistrict() + " " + employee.getPinCode();
                        binding.setFullAddress(address);
                        binding.setEmployee(employee);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.backgroundLayout.setAlpha(1);
                    } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("No data Found")) {
                        Toast.makeText(employeeProfileActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_FAILURE) {
                        Toast.makeText(employeeProfileActivity.this, requestCall.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
