package com.challenge.challenge.controller.dataload;

import com.challenge.challenge.controller.Response;
import com.challenge.challenge.controller.dataload.request.DataLoadRequest;
import com.challenge.challenge.services.dataload.DataLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/v1")
public class DataLoadController {
    @Autowired
    private DataLoadService dataLoadService;
    // get all employees
    @PostMapping("/dataload")
    public ResponseEntity<Response> startDataload(@RequestBody DataLoadRequest dataLoadRequest) {
        var response = dataLoadService.start(dataLoadRequest);
        return ResponseEntity.accepted().body(response);
    }
}