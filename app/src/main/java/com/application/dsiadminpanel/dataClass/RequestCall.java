package com.application.dsiadminpanel.dataClass;

import java.util.ArrayList;

public class RequestCall {
    private int status;
    private String message;
    private Employee employee;
    private ArrayList<Employee> pendingEmployees, stateCoordinators, zoneCoordinators, distributors, districtCoordinators, blockCoordinators, navPanchayat, customers;
    private String userId;
    private String post;
    private CountCoordinators count;
    private int customerCount;

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public CountCoordinators getCount() {
        return count;
    }

    public void setCount(CountCoordinators count) {
        this.count = count;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Employee> getPendingEmployees() {
        return pendingEmployees;
    }

    public ArrayList<Employee> getStateCoordinators() {
        return stateCoordinators;
    }

    public void setStateCoordinators(ArrayList<Employee> stateCoordinators) {
        this.stateCoordinators = stateCoordinators;
    }

    public ArrayList<Employee> getZoneCoordinators() {
        return zoneCoordinators;
    }

    public void setZoneCoordinators(ArrayList<Employee> zoneCoordinators) {
        this.zoneCoordinators = zoneCoordinators;
    }

    public ArrayList<Employee> getDistributors() {
        return distributors;
    }

    public void setDistributors(ArrayList<Employee> distributors) {
        this.distributors = distributors;
    }

    public ArrayList<Employee> getDistrictCoordinators() {
        return districtCoordinators;
    }

    public void setDistrictCoordinators(ArrayList<Employee> districtCoordinators) {
        this.districtCoordinators = districtCoordinators;
    }

    public ArrayList<Employee> getBlockCoordinators() {
        return blockCoordinators;
    }

    public void setBlockCoordinators(ArrayList<Employee> blockCoordinators) {
        this.blockCoordinators = blockCoordinators;
    }

    public ArrayList<Employee> getNavPanchayat() {
        return navPanchayat;
    }

    public void setNavPanchayat(ArrayList<Employee> navPanchayat) {
        this.navPanchayat = navPanchayat;
    }

    public ArrayList<Employee> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Employee> customers) {
        this.customers = customers;
    }

    public void setPendingEmployees(ArrayList<Employee> pendingEmployees) {
        this.pendingEmployees = pendingEmployees;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
