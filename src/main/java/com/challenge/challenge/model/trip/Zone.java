package com.challenge.challenge.model.trip;

import com.challenge.challenge.model.Model;
import com.challenge.challenge.services.dataload.fileparser.csv.Csv;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Csv(delimiter = ",")
public class Zone implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Csv(column = 0)
    private Long id;
    @Csv(column = 1)
    private String borough;
    @Csv(column = 2)
    private String zone;
    @Csv(column = 3)
    private String serviceZone;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getServiceZone() {
        return serviceZone;
    }

    public void setServiceZone(String serviceZone) {
        this.serviceZone = serviceZone;
    }
}