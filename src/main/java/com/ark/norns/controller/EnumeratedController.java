package com.ark.norns.controller;

import com.ark.norns.enumerated.IntervalKind;
import com.ark.norns.enumerated.SNMPVersion;
import com.ark.norns.enumerated.SensorKind;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/api/enumerated/")
public class EnumeratedController {

    @RequestMapping(value = "list-sensorkind", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listAllSensorKing() {
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(SensorKind.values()));
    }

    @RequestMapping(value = "list/snmp", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listAllSnmp() {
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(SNMPVersion.values()));
    }

    @RequestMapping(value = "list/interval", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listAllInterval() {
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(IntervalKind.values()));
    }
}
