package com.challenge.challenge.controller.trips.response;

import com.challenge.challenge.controller.Response;

import java.util.ArrayList;
import java.util.List;

public class TopZonesResponse implements Response {
    private String zone;
    private long pickupCount;
    private long dropoffCount;

    public TopZonesResponse(String zone, long pickupCount, long dropoffCount) {
        this.zone = zone;
        this.pickupCount = pickupCount;
        this.dropoffCount = dropoffCount;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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
