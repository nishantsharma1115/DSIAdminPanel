package com.application.dsiadminpanel.dataClass;

public class CountCoordinators {
    private int stateCoordinator, zoneCoordinator, Distributor, districtCoordinator, blockCoordinator, navPanchayat, customer;

    public CountCoordinators() {
        stateCoordinator = 0;
        zoneCoordinator = 0;
        districtCoordinator = 0;
        Distributor = 0;
        blockCoordinator = 0;
        navPanchayat = 0;
        customer = 0;
    }

    public int getStateCoordinator() {
        return stateCoordinator;
    }

    public void setStateCoordinator(int stateCoordinator) {
        this.stateCoordinator = stateCoordinator;
    }

    public int getZoneCoordinator() {
        return zoneCoordinator;
    }

    public void setZoneCoordinator(int zoneCoordinator) {
        this.zoneCoordinator = zoneCoordinator;
    }

    public int getDistributor() {
        return Distributor;
    }

    public void setDistributor(int distributor) {
        Distributor = distributor;
    }

    public int getDistrictCoordinator() {
        return districtCoordinator;
    }

    public void setDistrictCoordinator(int districtCoordinator) {
        this.districtCoordinator = districtCoordinator;
    }

    public int getBlockCoordinator() {
        return blockCoordinator;
    }

    public void setBlockCoordinator(int blockCoordinator) {
        this.blockCoordinator = blockCoordinator;
    }

    public int getNavPanchayat() {
        return navPanchayat;
    }

    public void setNavPanchayat(int navPanchayat) {
        this.navPanchayat = navPanchayat;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }
}
