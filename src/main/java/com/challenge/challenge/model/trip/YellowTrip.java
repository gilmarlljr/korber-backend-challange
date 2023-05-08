package com.challenge.challenge.model.trip;

import com.challenge.challenge.model.Model;
import com.challenge.challenge.services.dataload.fileparser.parquet.Parquet;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {@Index(columnList = "DOLocationID DESC"), @Index(columnList = "PULocationID DESC")})
public class YellowTrip implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Parquet("tpep_pickup_datetime")
    private LocalDateTime pickupDatetime;
    @Parquet("tpep_dropoff_datetime")
    private LocalDateTime dropoffDatetime;
    @Parquet("PULocationID")
    private Long PULocationID;
    @Parquet("DOLocationID")
    private Long DOLocationID;

    @Override
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPickupDatetime() {
        return pickupDatetime;
    }

    public void setPickupDatetime(LocalDateTime pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public LocalDateTime getDropoffDatetime() {
        return dropoffDatetime;
    }

    public void setDropoffDatetime(LocalDateTime dropoffDatetime) {
        this.dropoffDatetime = dropoffDatetime;
    }

    public Long getPULocationID() {
        return PULocationID;
    }

    public void setPULocationID(Long PULocationID) {
        this.PULocationID = PULocationID;
    }

    public Long getDOLocationID() {
        return DOLocationID;
    }

    public void setDOLocationID(Long DOLocationID) {
        this.DOLocationID = DOLocationID;
    }
}