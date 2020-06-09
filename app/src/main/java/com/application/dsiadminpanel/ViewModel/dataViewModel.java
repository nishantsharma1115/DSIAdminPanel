package com.application.dsiadminpanel.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.dsiadminpanel.Repositories.dataRepository;
import com.application.dsiadminpanel.dataClass.RequestCall;

public class dataViewModel extends ViewModel {
    private final dataRepository repository;

    public dataViewModel() {
        repository = new dataRepository();
    }

    public MutableLiveData<RequestCall> getPendingEmployee(String employeeId) {
        return repository.getPendingEmployee(employeeId);
    }

    public MutableLiveData<RequestCall> getPendingEmployeeList() {
        return repository.getPendingEmployeeList();
    }

    public MutableLiveData<RequestCall> getEmployeeDetails(String userId) {
        return repository.getEmployeeDetails(userId);
    }

    public MutableLiveData<RequestCall> getCountCoordinator() {
        return repository.getCountCoordinators();
    }

    public MutableLiveData<RequestCall> getStateCoordinatorList() {
        return repository.getStateCoordinators();
    }

    public MutableLiveData<RequestCall> getZoneCoordinatorList(String stateCoordinatorId) {
        return repository.getZoneCoordinators(stateCoordinatorId);
    }

    public MutableLiveData<RequestCall> getAllZoneCoordinator() {
        return repository.getAllZoneCoordinator();
    }

    public MutableLiveData<RequestCall> getDistributor(String zoneCoordinatorId) {
        return repository.getDistributors(zoneCoordinatorId);
    }

    public MutableLiveData<RequestCall> getAllDistributor() {
        return repository.getAllDistributors();
    }

    public MutableLiveData<RequestCall> getDistrictCoordinator(String distributorId) {
        return repository.getDistrictCoordinators(distributorId);
    }

    public MutableLiveData<RequestCall> getAllDistrictCoordinator() {
        return repository.getAllDistrictCoordinators();
    }

    public MutableLiveData<RequestCall> getBlockCoordinatorList(String districtCoordinatorId) {
        return repository.getBlockCoordinators(districtCoordinatorId);
    }

    public MutableLiveData<RequestCall> getAllBlockCoordinator() {
        return repository.getAllBlockCoordinator();
    }

    public MutableLiveData<RequestCall> getAllNavPanchayat() {
        return repository.getAllNavPanchayat();
    }

    public MutableLiveData<RequestCall> getNavPanchayats(String blockCoordinatorId) {
        return repository.getNavPanchayats(blockCoordinatorId);
    }

    public MutableLiveData<RequestCall> getAllCustomers() {
        return repository.getAllCustomers();
    }

    public MutableLiveData<RequestCall> getCustomers(String navPanchayatId) {
        return repository.getCustomers(navPanchayatId);
    }
}
