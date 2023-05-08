package com.challenge.challenge.controller.trips.response;

import com.challenge.challenge.controller.Response;
import com.challenge.challenge.model.trip.YellowTrip;
import org.springframework.data.domain.Page;

public record YellowListResponse(Page<YellowTrip> yellowTripPage) implements Response {
}
