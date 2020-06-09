package com.application.dsiadminpanel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.util.HashMap;
import java.util.Map;

import static com.application.dsiadminpanel.Common.Constants.DB;

public class employeeDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityEmployeeDashboardBinding binding;
    dataViewModel viewModel;
    FirebaseUser user;
    Employee employee = new Employee();
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_dashboard);
        viewModel = new ViewModelProvider(this).get(dataViewModel.class);
        user = FirebaseAuth.getInstance().getCurrentUser();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                saveLocationToDataBase(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //When User Disable or Enable the Location Permission
            }

            @Override
            public void onProviderEnabled(String s) {
                //When Enabled
            }

            @Override
            public void onProviderDisabled(String s) {
                //When Disabled
            }
        };

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
        binding.userProfile.setOnClickListener(this);
    }

    private void updateUi(Employee employee) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                saveLocationToDataBase(location);
            }
        }

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
            default:
                throw new IllegalStateException("Unexpected value: " + employee.getPost());
        }
    }

    private void saveLocationToDataBase(Location location) {
        Map<String, Object> employeeLocation = new HashMap<>();

        employeeLocation.put("latitude", String.valueOf(location.getLatitude()));
        employeeLocation.put("longitude", String.valueOf(location.getLongitude()));
        employeeLocation.put("userId", employee.getUserId());
        employeeLocation.put("post", employee.getPost());
        employeeLocation.put("name", employee.getName());

        DB.child("Location").child(employee.getUserId()).updateChildren(employeeLocation);
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
        } else if (view.getId() == R.id.userProfile) {
            Intent intent = new Intent(employeeDashboardActivity.this, employeeProfileActivity.class);
            intent.putExtra("userId", employee.getUserId());
            startActivity(intent);
        }
    }

    private void showNextCoordinators(Employee employee) {
        Intent intent;
        switch (employee.getPost()) {
            case "State Coordinator":
                intent = new Intent(this, zoneCoordinatorListActivity.class);
                intent.putExtra("stateCoordinatorId", employee.getEmployeeId());
                startActivity(intent);
                break;

            case "Zone Coordinator":
                intent = new Intent(this, distributorListActivity.class);
                intent.putExtra("zoneCoordinatorId", employee.getEmployeeId());
                startActivity(intent);
                break;

            case "Distributor":
                intent = new Intent(this, districtCoordinatorListActivity.class);
                intent.putExtra("distributorId", employee.getEmployeeId());
                startActivity(intent);
                break;
            case "District Coordinator":
                intent = new Intent(this, blockCoordinatorListActivity.class);
                intent.putExtra("districtCoordinatorId", employee.getEmployeeId());
                startActivity(intent);
                break;
            case "Block Coordinator":
                intent = new Intent(this, navPanchayatListActivity.class);
                intent.putExtra("blockCoordinatorId", employee.getEmployeeId());
                startActivity(intent);
                break;
            case "Nav Panchayat":
                intent = new Intent(this, customerListActivity.class);
                intent.putExtra("navPanchayatId", employee.getEmployeeId());
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + employee.getPost());
        }
    }
}
