package com.myapp.whoishome;

public class Device {
    String macAddress;
    String ipAddress;
    String name;
    String leaseTimeRemaining;

    // constructor
    public Device(){

    }

    // constructor with parameters
    public Device(String macAddress, String ipAddress, String name, String leaseTimeRemaining) {
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.name = name;
        this.leaseTimeRemaining = leaseTimeRemaining;
    }

    // set methods
    public void setMacAddress(String macAddress){
        this.macAddress = macAddress;
    }

    public void setIpAddress(String ipAddress){
        this.ipAddress = ipAddress;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setLeaseTimeRemaining(String leaseTimeRemaining){
        this.leaseTimeRemaining = leaseTimeRemaining;
    }

    public String getMacAddress() {
        return macAddress;
    }

    //
    @Override
    public String toString() {
        return this.name + " (" + this.ipAddress + ") Online: " + this.leaseTimeRemaining;
    }
}
