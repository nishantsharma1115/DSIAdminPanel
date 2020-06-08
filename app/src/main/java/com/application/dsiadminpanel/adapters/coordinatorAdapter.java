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
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.pendingEmployeeProfileActivity;

import java.util.ArrayList;

public class coordinatorAdapter extends RecyclerView.Adapter<coordinatorAdapter.ViewHolder> {
    private ArrayList<Employee> coordinatorEmployee;
    private Context context;

    public coordinatorAdapter(Context context, ArrayList<Employee> coordinatorEmployee) {
        this.coordinatorEmployee = coordinatorEmployee;
        this.context = context;
    }

    @NonNull
    @Override
    public coordinatorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_coordinator_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull coordinatorAdapter.ViewHolder holder, int position) {

        final Employee currentEmployee = coordinatorEmployee.get(position);

        holder.tv_name.setText(currentEmployee.getName());
        holder.tv_email.setText(currentEmployee.getEmail());
        holder.tv_phone.setText(currentEmployee.getMobile());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), pendingEmployeeProfileActivity.class);
                intent.putExtra("employeeId", currentEmployee.getEmployeeId());
                context.startActivity(intent);
            }
        });

        holder.showBlockCoordinator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), blockCoordinatorListActivity.class);
                intent.putExtra("stateCoordinatorId", currentEmployee.getEmployeeId());
                context.startActivity(intent);
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
        Button showBlockCoordinator;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_mobileNumber);
            tv_email = itemView.findViewById(R.id.tv_email);
            cardView = itemView.findViewById(R.id.cardView);
            showBlockCoordinator = itemView.findViewById(R.id.showBlockCoordinator);
        }
    }
}
