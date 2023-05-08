package com.challenge.challenge.database.repository.trip;

import com.challenge.challenge.model.trip.YellowTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface YellowTripRepository extends JpaRepository<YellowTrip,Long> {
}
