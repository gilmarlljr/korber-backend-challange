package com.challenge.challenge.controller.dataload.request;

import com.challenge.challenge.controller.exceptions.UnsupportedFileException;
import com.challenge.challenge.model.trip.GreenTrip;
import com.challenge.challenge.model.trip.YellowTrip;
import com.challenge.challenge.model.trip.Zone;

public enum DataType {
    ZONE(Zone.class), YELLOW(YellowTrip.class), GREEN(GreenTrip.class);
    public Class<?> clasz;

    DataType(Class<?> clasz) {
        this.clasz = clasz;
    }

    public static DataType fromString(String name) throws UnsupportedFileException {
        if (name.equalsIgnoreCase(ZONE.name())){
            return ZONE;
        }else if (name.equalsIgnoreCase(YELLOW.name())){
            return YELLOW;
        }else if (name.equalsIgnoreCase(GREEN.name())){
            return GREEN;
        }else {
            throw new UnsupportedFileException();
        }
    }
}
