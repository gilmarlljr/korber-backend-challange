package com.challenge.challenge.services.trips;

import com.challenge.challenge.controller.ErrorResponse;
import com.challenge.challenge.controller.Response;
import com.challenge.challenge.controller.trips.response.YellowListResponse;
import com.challenge.challenge.database.repository.trip.GreenTripRepository;
import com.challenge.challenge.database.repository.trip.YellowTripRepository;
import com.challenge.challenge.database.repository.trip.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TripsService {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private GreenTripRepository greenTripRepository;

    @Autowired
    private YellowTripRepository yellowTripRepository;

    public Response getLastFiveZones(TopOder order) {
        return new ErrorResponse().addError("Not Implemented");
    }

    public Response getTripsByDate(Long zoneId, LocalDate date) {
        var response = zoneRepository.findCountsByZoneAndDate(zoneId, Date.valueOf(date)).stream().findFirst().orElseThrow();
        response.setDate(date.format(DateTimeFormatter.ISO_DATE));
        return response;
    }

    public Response getListYellow(int page, int pageSize, String sortBy, String sortDir) {
        try {
            Sort sort = Sort.by(sortBy);
            sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable pageable = PageRequest.of(page, pageSize, sort);
            return new YellowListResponse(yellowTripRepository.findAll(pageable));
        } catch (Exception e) {
            return new ErrorResponse().addError("Unexpected Error " + e.getMessage());
        }

    }

    public enum TopOder {
        PICKUPS, DROPOFFS
    }
}
