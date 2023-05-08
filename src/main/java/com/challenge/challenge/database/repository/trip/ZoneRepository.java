package com.challenge.challenge.database.repository.trip;

import com.challenge.challenge.controller.trips.response.TopZonesResponse;
import com.challenge.challenge.controller.trips.response.ZoneTripsResponse;
import com.challenge.challenge.model.trip.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone,Long> {

    @Query("SELECT new com.challenge.challenge.controller.trips.response.ZoneTripsResponse(z.zone, COUNT(DISTINCT gp.id)+COUNT(DISTINCT yp.id), COUNT(DISTINCT gd.id)+COUNT(DISTINCT yd.id)) " +
            "FROM Zone z " +
            "LEFT JOIN GreenTrip gp ON z.id = gp.PULocationID AND FUNCTION('date_trunc', 'day', gp.pickupDatetime) = :date " +
            "LEFT JOIN GreenTrip gd ON z.id = gd.DOLocationID AND FUNCTION('date_trunc', 'day', gd.dropoffDatetime) = :date " +
            "LEFT JOIN YellowTrip yp ON z.id = yp.PULocationID AND FUNCTION('date_trunc', 'day', yp.pickupDatetime) = :date " +
            "LEFT JOIN YellowTrip yd ON z.id = yd.DOLocationID AND FUNCTION('date_trunc', 'day', yd.dropoffDatetime) = :date " +
            "WHERE z.id = :zoneId " +
            "GROUP BY z.zone " +
            "HAVING COUNT(DISTINCT gp.id)+COUNT(DISTINCT yp.id) > 0 OR COUNT(DISTINCT gd.id)+COUNT(DISTINCT yd.id) > 0")
    List<ZoneTripsResponse> findCountsByZoneAndDate(@Param("zoneId") Long zoneId,@Param("date") Date date);


    @Query("SELECT new com.challenge.challenge.controller.trips.response.TopZonesResponse(z.zone, COUNT(DISTINCT gp.id)+COUNT(DISTINCT yp.id), COUNT(DISTINCT gd.id)+COUNT(DISTINCT yd.id)) " +
            "FROM Zone z " +
            "LEFT JOIN GreenTrip gp ON z.id = gp.PULocationID AND FUNCTION('date_trunc', 'day', gp.pickupDatetime) = :date " +
            "LEFT JOIN GreenTrip gd ON z.id = gd.DOLocationID AND FUNCTION('date_trunc', 'day', gd.dropoffDatetime) = :date " +
            "LEFT JOIN YellowTrip yp ON z.id = yp.PULocationID AND FUNCTION('date_trunc', 'day', yp.pickupDatetime) = :date " +
            "LEFT JOIN YellowTrip yd ON z.id = yd.DOLocationID AND FUNCTION('date_trunc', 'day', yd.dropoffDatetime) = :date " +
            "WHERE z.id = :zoneId " +
            "GROUP BY z.zone " +
            "HAVING COUNT(DISTINCT gp.id)+COUNT(DISTINCT yp.id) > 0 OR COUNT(DISTINCT gd.id)+COUNT(DISTINCT yd.id) > 0")
    List<TopZonesResponse> findTopFive(@Param("zoneId") Long zoneId, @Param("date") Date date);

}