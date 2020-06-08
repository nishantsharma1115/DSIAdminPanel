package com.application.dsiadminpanel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class employeeDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        String post = getIntent().getStringExtra("post");

        if (post != null) {
            switch (post) {
                case "State Coordinator":
                    stateCoordinatorUi();
                    break;
                case "Zone Coordinator":
                    zoneCoordinatorUi();
                    break;
                case "Distributor":
                    distributorUi();
                    break;
                case "District Coordinator":
                    districtCoordinatorUi();
                    break;
                case "Block Coordinator":
                    blockCoordinatorUi();
                    break;
                default:
                    Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private void blockCoordinatorUi() {

    }

    private void districtCoordinatorUi() {

    }

    private void distributorUi() {

    }

    private void zoneCoordinatorUi() {

    }

    private void stateCoordinatorUi() {

    }
}
