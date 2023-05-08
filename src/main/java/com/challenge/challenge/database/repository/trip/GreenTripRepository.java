package com.challenge.challenge.database.repository.trip;

import com.challenge.challenge.model.trip.GreenTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreenTripRepository extends JpaRepository<GreenTrip,Long> {

}
