package com.application.dsiadminpanel;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.application.dsiadminpanel.Common.Constants;
import com.application.dsiadminpanel.ViewModel.authViewModel;
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.application.dsiadminpanel.databinding.ActivityGenerateEmployeeBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.application.dsiadminpanel.Common.Constants.DB;

public class generateEmployeeActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityGenerateEmployeeBinding binding;
    authViewModel authViewModel;
    PendingIntent sendPi, deliveredPi;
    BroadcastReceiver smsSendReceiver, smsDeliveredReceiver;
    String SEND = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_generate_employee);
        employee = (Employee) getIntent().getSerializableExtra("employee");

        if (employee != null) {
            binding.setEmail(employee.getEmail());
        }

        binding.btnGenerateEmployee.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSendReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(generateEmployeeActivity.this, "OTP Send", Toast.LENGTH_SHORT).show();
                }
            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

        registerReceiver(smsSendReceiver, new IntentFilter(SEND));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSendReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_generateEmployee) {
            if (!binding.edtPassword.getText().toString().equals(binding.edtConfirmPassword.getText().toString())) {
                Toast.makeText(generateEmployeeActivity.this, "Password and Confirm Password is not same", Toast.LENGTH_LONG).show();
            } else if (binding.edtPassword.getText().toString().length() < 8) {
                Toast.makeText(generateEmployeeActivity.this, "Password length should be greater than 8", Toast.LENGTH_LONG).show();
            } else {
                generateEmployee();
            }
        }
    }

    private void generateEmployee() {
        authViewModel = new ViewModelProvider(this).get(authViewModel.class);

        if (employee != null) {
            authViewModel.vieModelSignUp(employee.getEmail(), binding.edtPassword.getText().toString()).observe(this, new Observer<RequestCall>() {
                @Override
                public void onChanged(RequestCall requestCall) {
                    if (requestCall.getStatus() == Constants.OPERATION_IN_PROGRESS) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        binding.backgroundLayout.setAlpha((float) 0.4);
                    } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Finished")) {
                        employee.setUserId(requestCall.getUserId());
                        registerEmployee(requestCall.getUserId());
                        binding.progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        binding.backgroundLayout.setAlpha(1);
                    }
                }
            });
        }
    }

    private void registerEmployee(String userId) {
        DB.child("Employee").child(userId).setValue(employee)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sendSms(employee);
                        removeEmployee();
                    }
                });
    }

    private void sendSms(Employee employee) {

        sendPi = PendingIntent.getBroadcast(this, 0, new Intent(SEND), 0);
        deliveredPi = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+91" + employee.getMobile(), null, "Congratulations! Your Application has been accepted. You are now appointed as " + employee.getPost() + " . Your Email id is " + employee.getEmail() + " and your password is " + binding.edtPassword.getText().toString() + "" + " . Use this Credential to login into DSI. Thank you :)", sendPi, deliveredPi);
    }

    private void removeEmployee() {
        authViewModel.removeEmployee(employee.getEmployeeId()).observe(this, new Observer<RequestCall>() {
            @Override
            public void onChanged(RequestCall requestCall) {
                if (requestCall.getStatus() == Constants.OPERATION_IN_PROGRESS) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    binding.backgroundLayout.setAlpha((float) 0.4);
                } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Removed")) {
                    Intent intent = new Intent(generateEmployeeActivity.this, adminDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
