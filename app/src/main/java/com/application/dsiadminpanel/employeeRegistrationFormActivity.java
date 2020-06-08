package com.application.dsiadminpanel;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.application.dsiadminpanel.ViewModel.dataViewModel;
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.databinding.ActivityEmployeeRegistrationFormBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import static com.application.dsiadminpanel.Common.Constants.DB;

public class employeeRegistrationFormActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityEmployeeRegistrationFormBinding binding;
    Employee employee;
    boolean isEmployeeSet = false;
    Random rand = new Random();
    String phoneNumber;
    long employeeId;
    int OTP;
    PendingIntent sendPi, deliveredPi;
    BroadcastReceiver smsSendReceiver, smsDeliveredReceiver;
    String SEND = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    dataViewModel dataViewModel;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    MutableLiveData<String> postSelected = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_registration_form);
        dataViewModel = new ViewModelProvider(this).get(dataViewModel.class);
        employee = new Employee();

        binding.proceed.setOnClickListener(this);
        binding.imgCalender.setOnClickListener(this);
        binding.btnSendOtp.setOnClickListener(this);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String Day = String.valueOf(day), Month = String.valueOf(month);
                if (day < 10) {
                    Day = "0" + day;
                }
                if ((month) < 10) {
                    Month = "0" + month;
                }
                binding.edtDob.setText(new StringBuilder().append(Day).append("/").append(Integer.parseInt(Month) + 1).append("/").append(year));
            }
        };

        final ArrayAdapter<CharSequence> applyFor = ArrayAdapter.createFromResource(this, R.array.apply_for, R.layout.spinner_item);
        binding.spinnerApplyFor.setAdapter(applyFor);

        postSelected.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Select the post you are applying for")) {
                    binding.upperId.setVisibility(View.GONE);
                } else if (!s.equals("State Coordinator")) {
                    binding.upperId.setVisibility(View.VISIBLE);
                    switch (s) {
                        case "Zone Coordinator":
                            binding.upperId.setHint("Enter 9 digit State Coordinator ID");
                            break;
                        case "Distributor":
                            binding.upperId.setHint("Enter 9 digit Zone Coordinator ID");
                            break;
                        case "District Coordinator":
                            binding.upperId.setHint("Enter 9 digit Distributor ID");
                            break;
                        case "Block Coordinator":
                            binding.upperId.setHint("Enter 9 digit District Coordinator ID");
                            break;
                        default:
                            binding.upperId.setHint("Invalid Post.... Try again");
                            break;
                    }
                } else {
                    binding.upperId.setVisibility(View.GONE);
                }
            }
        });

        binding.spinnerApplyFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                postSelected.setValue(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(employeeRegistrationFormActivity.this, "Can not be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.proceed) {
            if (Objects.equals(postSelected.getValue(), "Select the post you are applying for")) {
                ((TextView) binding.spinnerApplyFor.getSelectedView()).setError("Required");
                (binding.spinnerApplyFor.getSelectedView()).requestFocus();
                Toast.makeText(employeeRegistrationFormActivity.this, "Can not be empty", Toast.LENGTH_SHORT).show();
            } else if (!Objects.equals(postSelected.getValue(), "State Coordinator")) {
                if (binding.upperId.getText().toString().isEmpty()) {
                    binding.upperId.setError("Required");
                    binding.upperId.requestFocus();
                } else {
                    binding.applyFor.setVisibility(View.GONE);
                    binding.signUpBackgroundLayout.setVisibility(View.VISIBLE);
                }
            } else {
                binding.applyFor.setVisibility(View.GONE);
                binding.signUpBackgroundLayout.setVisibility(View.VISIBLE);
            }
        } else if (view.getId() == R.id.btn_sendOtp) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                sendOtp();
            }
        } else if (view.getId() == R.id.img_calender) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(
                    this,
                    R.style.Theme_AppCompat_DayNight_Dialog,
                    mDateSetListener,
                    year, month + 1, day).show();
        }
    }

    private void sendOtp() {

        setEmployee();

        if (isEmployeeSet) {
            OTP = rand.nextInt(899999) + 100000;

            if (binding.edtMobile.getText().toString().startsWith("+91")) {
                phoneNumber = binding.edtMobile.getText().toString();
            } else {
                phoneNumber = "+91" + binding.edtMobile.getText().toString();
            }

            sendPi = PendingIntent.getBroadcast(this, 0, new Intent(SEND), 0);
            deliveredPi = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

            SmsManager sms = SmsManager.getDefault();
            Log.d("OTP", OTP +" "+ phoneNumber);
            sms.sendTextMessage(phoneNumber, null, OTP + " is your Verification Code", sendPi, deliveredPi);

            binding.signUpBackgroundLayout.setVisibility(View.GONE);
            binding.verifyLayout.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void setEmployee() {

        if (TextUtils.isEmpty(binding.edtName.getText())) {
            binding.edtName.setError("Required");
            binding.edtName.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtFatherName.getText())) {
            binding.edtFatherName.setError("Required");
            binding.edtFatherName.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtMotherName.getText())) {
            binding.edtMobile.setError("Required");
            binding.edtMotherName.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtDob.getText())) {
            binding.edtDob.setError("Required");
            binding.edtDob.requestFocus();
        } else if (!binding.male.isChecked() && !binding.female.isChecked()) {
            binding.tvGender.setError("Required");
            binding.tvGender.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtPanNo.getText())) {
            binding.edtPanNo.setError("Required");
            binding.edtPanNo.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtAadhaar.getText())) {
            binding.edtAadhaar.setError("Required");
            binding.edtAadhaar.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtVoter.getText())) {
            binding.edtVoter.setError("Required");
            binding.edtVoter.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtRation.getText())) {
            binding.edtRation.setError("Required");
            binding.edtRation.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtNationality.getText())) {
            binding.edtNationality.setError("Required");
            binding.edtNationality.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtHouseNo.getText())) {
            binding.edtHouseNo.setError("Required");
            binding.edtHouseNo.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtStreetNumber.getText())) {
            binding.edtStreetNumber.setError("Required");
            binding.edtStreetNumber.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtBlock.getText())) {
            binding.edtBlock.setError("Required");
            binding.edtBlock.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtLandmark.getText())) {
            binding.edtLandmark.setError("Required");
            binding.edtLandmark.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtVillage.getText())) {
            binding.edtVillage.setError("Required");
            binding.edtVillage.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtPostOffice.getText())) {
            binding.edtPostOffice.setError("Required");
            binding.edtPostOffice.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtPoliceSt.getText())) {
            binding.edtPoliceSt.setError("Required");
            binding.edtPoliceSt.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtDistrict.getText())) {
            binding.edtDistrict.setError("Required");
            binding.edtDistrict.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtCity.getText())) {
            binding.edtCity.setError("Required");
            binding.edtCity.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtPinCode.getText())) {
            binding.edtPinCode.setError("Required");
            binding.edtPinCode.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtMobile.getText())) {
            binding.edtMobile.setError("Required");
            binding.edtMobile.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtEmail.getText())) {
            binding.edtEmail.setError("Required");
            binding.edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches()) {
            binding.edtEmail.setError("Email Format is incorrect");
            binding.edtEmail.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtBankName.getText())) {
            binding.edtBankName.setError("Required");
            binding.edtBankName.requestFocus();
        } else if (TextUtils.isEmpty(binding.edtBranch.getText())) {
            binding.edtBranch.setError("Required");
            binding.edtBranch.requestFocus();
        } else {
            employeeId = rand.nextInt(899999999) + 100000000;
            employee.setEmployeeId(String.valueOf(employeeId));
            employee.setName(binding.edtName.getText().toString());
            employee.setFatherName(binding.edtFatherName.getText().toString());
            employee.setMotherName(binding.edtMotherName.getText().toString());
            employee.setDob(binding.edtDob.getText().toString());
            employee.setPanNo(binding.edtPanNo.getText().toString());
            employee.setAadhaarNo(binding.edtAadhaar.getText().toString());
            employee.setVoterNo(binding.edtVoter.getText().toString());
            employee.setRationNo(binding.edtRation.getText().toString());
            employee.setNationality(binding.edtNationality.getText().toString());
            employee.setHouse_no(binding.edtHouseNo.getText().toString());
            employee.setStreet_no(binding.edtStreetNumber.getText().toString());
            employee.setBlock(binding.edtBlock.getText().toString());
            employee.setLandmark(binding.edtLandmark.getText().toString());
            employee.setVillage(binding.edtVillage.getText().toString());
            employee.setPostOffice(binding.edtPostOffice.getText().toString());
            employee.setPoliceStation(binding.edtPoliceSt.getText().toString());
            employee.setDistrict(binding.edtDistrict.getText().toString());
            employee.setCity(binding.edtCity.getText().toString());
            employee.setPinCode(binding.edtPinCode.getText().toString());
            employee.setMobile(binding.edtMobile.getText().toString());
            employee.setEmail(binding.edtEmail.getText().toString());
            employee.setBankName(binding.edtBankName.getText().toString());
            employee.setBankBranch(binding.edtBranch.getText().toString());

            if (postSelected.getValue() != null) {
                switch (postSelected.getValue()) {
                    case "State Coordinator":
                        employee.setPost(postSelected.getValue());
                        break;
                    case "Zone Coordinator":
                        employee.setPost(postSelected.getValue());
                        employee.setStateCoordinatorId(binding.upperId.getText().toString());
                        break;
                    case "Distributor":
                        employee.setPost(postSelected.getValue());
                        employee.setZoneCoordinatorId(binding.upperId.getText().toString());
                        break;
                    case "District Coordinator":
                        employee.setPost(postSelected.getValue());
                        employee.setDistributorId(binding.upperId.getText().toString());
                        break;
                    case "Block Coordinator":
                        employee.setPost(postSelected.getValue());
                        employee.setDistrictCoordinatorId(binding.upperId.getText().toString());
                        break;
                }
            }
            if (binding.male.isChecked()) {
                employee.setGender(binding.male.getText().toString());
            } else if (binding.female.isChecked()) {
                employee.setGender(binding.female.getText().toString());
            }

            isEmployeeSet = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSendReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(employeeRegistrationFormActivity.this, "OTP Send", Toast.LENGTH_SHORT).show();
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

    public void goBack(View view) {
        binding.verifyLayout.setVisibility(View.GONE);
        binding.signUpBackgroundLayout.setVisibility(View.VISIBLE);
    }

    public void verifyOtp(View view) {
        if (binding.edtVerifyOtp.getText().toString().equals(String.valueOf(OTP))) {
            DB.child("Pending Form").child(String.valueOf(employeeId)).setValue(employee)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage(binding.edtMobile.getText().toString(), null, "Thanks for Registering for DSI.", sendPi, deliveredPi);
                            Intent intent = new Intent(employeeRegistrationFormActivity.this, loginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(employeeRegistrationFormActivity.this, "Processing Failed......", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
