package com.challenge.challenge.controller.trips.response;

import com.challenge.challenge.controller.Response;

import java.time.LocalDate;

public class ZoneTripsResponse implements Response {
    private String zoneName;
    private String date;
    private long pickupCount;
    private long dropoffCount;

    public ZoneTripsResponse(String zoneName, long pickupCount, long dropoffCount) {
        this.zoneName = zoneName;
        this.pickupCount = pickupCount;
        this.dropoffCount = dropoffCount;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getPickupCount() {
        return pickupCount;
    }

    public void setPickupCount(long pickupCount) {
        this.pickupCount = pickupCount;
    }

    public long getDropoffCount() {
        return dropoffCount;
    }

    public void setDropoffCount(long dropoffCount) {
        this.dropoffCount = dropoffCount;
    }
}
