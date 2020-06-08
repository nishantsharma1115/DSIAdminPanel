package com.application.dsiadminpanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.application.dsiadminpanel.Common.Constants;
import com.application.dsiadminpanel.ViewModel.dataViewModel;
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.application.dsiadminpanel.databinding.ActivityPendingEmployeeProfileBinding;

import static com.application.dsiadminpanel.Common.Constants.DB;

public class pendingEmployeeProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPendingEmployeeProfileBinding binding;
    dataViewModel viewModel;
    Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pending_employee_profile);
        viewModel = new ViewModelProvider(this).get(dataViewModel.class);

        viewModel.getPendingEmployee(getIntent().getStringExtra("employeeId")).observe(this, new Observer<RequestCall>() {
            @Override
            public void onChanged(RequestCall requestCall) {
                if (requestCall.getStatus() == Constants.OPERATION_IN_PROGRESS) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.backgroundLayout.setAlpha((float) 0.4);
                } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Finished")) {
                    employee = requestCall.getEmployee();
                    String address = employee.getHouse_no() + " " + employee.getStreet_no() + " " + employee.getBlock() + " " + employee.getLandmark() + " " + employee.getVillage() + " " + employee.getPostOffice() + " " + employee.getPoliceStation() + " " + employee.getCity() + " " + employee.getDistrict() + " " + employee.getPinCode();
                    binding.setFullAddress(address);
                    binding.setEmployee(requestCall.getEmployee());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.backgroundLayout.setAlpha(1);
                } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("No data Found")) {
                    Toast.makeText(pendingEmployeeProfileActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_FAILURE) {
                    Toast.makeText(pendingEmployeeProfileActivity.this, requestCall.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.acceptButton.setOnClickListener(this);
        binding.declineButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.accept_button) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Are you Sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == DialogInterface.BUTTON_POSITIVE) {
                                //When Admin Accept Employee
                                Intent intent = new Intent(pendingEmployeeProfileActivity.this, generateEmployeeActivity.class);
                                intent.putExtra("employee", employee);
                                startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == DialogInterface.BUTTON_NEGATIVE) {
                                dialogInterface.dismiss();
                            }
                        }
                    })
                    .show();

        } else if (view.getId() == R.id.decline_button) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Are you Sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == DialogInterface.BUTTON_POSITIVE) {
                                //When Admin Reject Employee
                                DB.child("Pending Form").child(employee.getEmployeeId()).removeValue();
                                Toast.makeText(pendingEmployeeProfileActivity.this, "Employee Form Rejected", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == DialogInterface.BUTTON_NEGATIVE) {
                                dialogInterface.dismiss();
                            }
                        }
                    })
                    .show();
        }
    }
}
