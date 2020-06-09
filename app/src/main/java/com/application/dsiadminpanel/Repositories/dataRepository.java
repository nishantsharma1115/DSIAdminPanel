package com.application.dsiadminpanel.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.application.dsiadminpanel.Common.Constants;
import com.application.dsiadminpanel.dataClass.CountCoordinators;
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.application.dsiadminpanel.Common.Constants.DB;

public class dataRepository {
    private final MutableLiveData<RequestCall> downloadMutableLiveData;

    public dataRepository() {
        this.downloadMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<RequestCall> getPendingEmployee(String employeeId) {
        final RequestCall r = new RequestCall();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setEmployee(new Employee());
        downloadMutableLiveData.setValue(r);

        DB.child("Pending Form").child(employeeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setEmployee(employee);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });
        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getPendingEmployeeList() {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> pendingEmployees = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setPendingEmployees(pendingEmployees);
        downloadMutableLiveData.setValue(r);

        DB.child("Pending Form").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        pendingEmployees.add(current);
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setPendingEmployees(pendingEmployees);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getEmployeeDetails(final String userId) {
        final RequestCall r = new RequestCall();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setEmployee(new Employee());
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setEmployee(employee);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });
        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getCountCoordinators() {
        final RequestCall r = new RequestCall();
        final CountCoordinators count = new CountCoordinators();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setCount(count);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int countSC = 0, countZC = 0, countD = 0, countDC = 0, countBC = 0, countNP = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee employee = ds.getValue(Employee.class);
                        if (employee != null) {
                            switch (employee.getPost()) {
                                case "State Coordinator":
                                    countSC++;
                                    break;
                                case "Zone Coordinator":
                                    countZC++;
                                    break;
                                case "Distributor":
                                    countD++;
                                    break;
                                case "District Coordinator":
                                    countDC++;
                                    break;
                                case "Block Coordinator":
                                    countBC++;
                                    break;
                                case "Nav Panchayat":
                                    countNP++;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    count.setStateCoordinator(countSC);
                    count.setZoneCoordinator(countZC);
                    count.setDistributor(countD);
                    count.setDistrictCoordinator(countDC);
                    count.setBlockCoordinator(countBC);
                    count.setNavPanchayat(countNP);
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setCount(count);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });
        return downloadMutableLiveData;

    }

    public MutableLiveData<RequestCall> getStateCoordinators() {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> stateCoordinators = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setStateCoordinators(stateCoordinators);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("State Coordinator")) {
                                stateCoordinators.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setStateCoordinators(stateCoordinators);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getZoneCoordinators(final String stateCoordinatorId) {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> zoneCoordinators = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setZoneCoordinators(zoneCoordinators);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Zone Coordinator") && current.getStateCoordinatorId().equals(stateCoordinatorId)) {
                                zoneCoordinators.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setZoneCoordinators(zoneCoordinators);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getAllZoneCoordinator() {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> zoneCoordinators = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setZoneCoordinators(zoneCoordinators);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Zone Coordinator")) {
                                zoneCoordinators.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    Log.d("User", zoneCoordinators.get(0).getEmail());
                    r.setZoneCoordinators(zoneCoordinators);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getDistributors(final String zoneCoordinatorId) {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> distributors = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setDistributors(distributors);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Distributor") && current.getZoneCoordinatorId().equals(zoneCoordinatorId)) {
                                distributors.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setDistributors(distributors);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getAllDistributors() {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> distributors = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setDistributors(distributors);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Distributor")) {
                                distributors.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setDistributors(distributors);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getDistrictCoordinators(final String distributorId) {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> districtCoordinators = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setDistrictCoordinators(districtCoordinators);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("District Coordinator") && current.getDistributorId().equals(distributorId)) {
                                districtCoordinators.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setDistrictCoordinators(districtCoordinators);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getAllDistrictCoordinators() {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> districtCoordinators = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setDistrictCoordinators(districtCoordinators);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("District Coordinator")) {
                                districtCoordinators.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setDistrictCoordinators(districtCoordinators);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getBlockCoordinators(final String districtCoordinatorId) {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> blockCoordinators = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setBlockCoordinators(blockCoordinators);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Block Coordinator") && current.getDistrictCoordinatorId().equals(districtCoordinatorId)) {
                                blockCoordinators.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setBlockCoordinators(blockCoordinators);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getAllBlockCoordinator() {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> blockCoordinators = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setBlockCoordinators(blockCoordinators);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Block Coordinator")) {
                                blockCoordinators.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setBlockCoordinators(blockCoordinators);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getNavPanchayats(final String blockCoordinatorId) {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> navPanchayats = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setNavPanchayat(navPanchayats);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Nav Panchayat") && current.getBlockCoordinatorId().equals(blockCoordinatorId)) {
                                navPanchayats.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setNavPanchayat(navPanchayats);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getAllNavPanchayat() {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> navPanchayats = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setNavPanchayat(navPanchayats);
        downloadMutableLiveData.setValue(r);

        DB.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Nav Panchayat")) {
                                navPanchayats.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setNavPanchayat(navPanchayats);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getCustomers(final String navPanchayatId) {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> customers = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setCustomers(customers);
        downloadMutableLiveData.setValue(r);

        DB.child("Customers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Customer") && current.getNavPanchayatId().equals(navPanchayatId)) {
                                customers.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setCustomers(customers);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> getAllCustomers() {
        final RequestCall r = new RequestCall();
        final ArrayList<Employee> customers = new ArrayList<>();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setCustomers(customers);
        downloadMutableLiveData.setValue(r);

        DB.child("Customers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Employee current = ds.getValue(Employee.class);
                        if (current != null) {
                            if (current.getPost().equals("Customer")) {
                                customers.add(current);
                            }
                        }
                    }
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setCustomers(customers);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });

        return downloadMutableLiveData;
    }
}
