package com.application.dsiadminpanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.dsiadminpanel.R;
import com.application.dsiadminpanel.blockCoordinatorListActivity;
import com.application.dsiadminpanel.customerListActivity;
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.distributorListActivity;
import com.application.dsiadminpanel.districtCoordinatorListActivity;
import com.application.dsiadminpanel.employeeProfileActivity;
import com.application.dsiadminpanel.navPanchayatListActivity;
import com.application.dsiadminpanel.zoneCoordinatorListActivity;

import java.util.ArrayList;

public class coordinatorAdapter extends RecyclerView.Adapter<coordinatorAdapter.ViewHolder> {
    private ArrayList<Employee> coordinatorEmployee;
    private Context context;
    private String coordinatorType;

    public coordinatorAdapter(Context context, String coordinatorType, ArrayList<Employee> coordinatorEmployee) {
        this.coordinatorEmployee = coordinatorEmployee;
        this.context = context;
        this.coordinatorType = coordinatorType;
    }

    @NonNull
    @Override
    public coordinatorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_coordinator_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final coordinatorAdapter.ViewHolder holder, int position) {

        final Employee currentEmployee = coordinatorEmployee.get(position);

        holder.tv_name.setText(currentEmployee.getName());
        holder.tv_email.setText(currentEmployee.getEmail());
        holder.tv_phone.setText(currentEmployee.getMobile());

        switch (coordinatorType) {
            case "State Coordinator":
                holder.showNextCoordinator.setText(R.string.show_zone_coordinator);
                break;
            case "Zone Coordinator":
                holder.showNextCoordinator.setText(R.string.show_distributor);
                break;
            case "Distributor":
                holder.showNextCoordinator.setText(R.string.show_district_coordinator);
                break;
            case "District Coordinator":
                holder.showNextCoordinator.setText(R.string.show_block_coordinator);
                break;
            case "Block Coordinator":
                holder.showNextCoordinator.setText(R.string.show_nav_panchayat);
                break;
            case "Nav Panchayat":
                holder.showNextCoordinator.setText(R.string.show_customers);
                break;
            case "Customer":
                holder.showNextCoordinator.setVisibility(View.GONE);
                break;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), employeeProfileActivity.class);
                intent.putExtra("userId", currentEmployee.getUserId());
                context.startActivity(intent);
            }
        });

        holder.showNextCoordinator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (coordinatorType) {
                    case "State Coordinator":
                        intent = new Intent(view.getContext(), zoneCoordinatorListActivity.class);
                        intent.putExtra("stateCoordinatorId", currentEmployee.getEmployeeId());
                        context.startActivity(intent);
                        break;
                    case "Zone Coordinator":
                        intent = new Intent(view.getContext(), distributorListActivity.class);
                        intent.putExtra("zoneCoordinatorId", currentEmployee.getEmployeeId());
                        context.startActivity(intent);
                        break;
                    case "Distributor":
                        intent = new Intent(view.getContext(), districtCoordinatorListActivity.class);
                        intent.putExtra("distributorId", currentEmployee.getEmployeeId());
                        context.startActivity(intent);
                        break;
                    case "District Coordinator":
                        intent = new Intent(view.getContext(), blockCoordinatorListActivity.class);
                        intent.putExtra("districtCoordinatorId", currentEmployee.getEmployeeId());
                        context.startActivity(intent);
                        break;
                    case "Block Coordinator":
                        intent = new Intent(view.getContext(), navPanchayatListActivity.class);
                        intent.putExtra("blockCoordinatorId", currentEmployee.getEmployeeId());
                        context.startActivity(intent);
                        break;
                    case "Nav Panchayat":
                        intent = new Intent(view.getContext(), customerListActivity.class);
                        intent.putExtra("navPanchayatId", currentEmployee.getEmployeeId());
                        context.startActivity(intent);
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return coordinatorEmployee.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_phone, tv_email;
        CardView cardView;
        Button showNextCoordinator;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_mobileNumber);
            tv_email = itemView.findViewById(R.id.tv_email);
            cardView = itemView.findViewById(R.id.cardView);
            showNextCoordinator = itemView.findViewById(R.id.showNextCoordinator);
        }
    }
}
