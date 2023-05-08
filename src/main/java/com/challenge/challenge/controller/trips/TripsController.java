package com.challenge.challenge.controller.trips;

import com.challenge.challenge.controller.ErrorResponse;
import com.challenge.challenge.controller.Response;
import com.challenge.challenge.controller.dataload.response.DataLoadResponse;
import com.challenge.challenge.services.trips.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@CrossOrigin(origins = "http://localhost:8081")

@RestController
@RequestMapping("/v1")
public class TripsController {
    @Autowired
    private TripsService dataLoadService;

    // get all employees
    @GetMapping("/top-zones")
    public ResponseEntity<Response> getLastFiveZones(@Param("order") String order) {
        var topOrder = TripsService.TopOder.PICKUPS;
        if (order.equalsIgnoreCase(TripsService.TopOder.PICKUPS.name())) {
            topOrder = TripsService.TopOder.PICKUPS;
        } else if (order.equalsIgnoreCase(TripsService.TopOder.DROPOFFS.name())) {
            topOrder = TripsService.TopOder.DROPOFFS;
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponse().addError("order needs to be PICKUPS or DROPOFFS"));
        }
        return ResponseEntity.accepted().body(dataLoadService.getLastFiveZones(topOrder));
    }

    @GetMapping("/zone-trips")
    public ResponseEntity<Response> getTripsByDate(@Param("zone") Long zone, @Param("date") String date) {
        var localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        return ResponseEntity.ok().body(dataLoadService.getTripsByDate(zone, localDate));
    }

    @GetMapping("/list-yellow")
    public ResponseEntity<Response> getListYellow(@Param("page") int page,
                                                  @Param("pageSize") int pageSize,
                                                  @Param("sortBy") String sortBy,
                                                  @Param("sortDir") String sortDir) {
        return ResponseEntity.ok().body(dataLoadService.getListYellow(page, pageSize, sortBy, sortDir));
    }
}
