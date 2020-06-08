package com.application.dsiadminpanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.dsiadminpanel.R;
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.pendingEmployeeProfileActivity;

import java.util.ArrayList;

public class pendingApplicationAdapter extends RecyclerView.Adapter<pendingApplicationAdapter.ViewHolder> {
    private ArrayList<Employee> pendingEmployees;
    private Context context;

    public pendingApplicationAdapter(Context context, ArrayList<Employee> pendingEmployees) {
        this.pendingEmployees = pendingEmployees;
        this.context = context;
    }

    @NonNull
    @Override
    public pendingApplicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_employee_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final pendingApplicationAdapter.ViewHolder holder, int position) {
        final Employee currentEmployee = pendingEmployees.get(position);

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
    }

    @Override
    public int getItemCount() {
        return pendingEmployees.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_phone, tv_email;
        CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_mobileNumber);
            tv_email = itemView.findViewById(R.id.tv_email);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
