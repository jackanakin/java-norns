package com.ark.norns.controller;

import com.ark.norns.application.Properties;
import com.ark.norns.entity.SifCollector;
import com.ark.norns.entity.entityView.SifCollectorView;
import com.ark.norns.enumerated.Status;
import com.ark.norns.service.SifCollectorService;
import com.ark.norns.specification.SifCollectorSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Iterator;

@RestController
@RequestMapping(value = "/api/configuration/")
public class ConfigurationController {
    @Autowired
    private SifCollectorService sifCollectorService;

    @RequestMapping(value = "remove-collector", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeCollector(@RequestParam Long id) {
        sifCollectorService.removeEntity(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "listFilter-sifcollector", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listFilterSifCollector() {
        Specification<SifCollector> specification = SifCollectorSpecification.selectMenuFilter(Status.ENABLED);

        Iterator<SifCollector> sifCollectorSet = sifCollectorService.getSifCollectorDAO().getSifCollectorRepository()
                .findAll(specification, new Sort(Sort.Direction.ASC, "description")).iterator();

        return ResponseEntity.status(HttpStatus.OK).body(sifCollectorSet);
    }

    @RequestMapping(value = "list-sifcollector", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listAllSifCollector() {
        Iterator<SifCollector> sifCollectorSet = sifCollectorService.getSifCollectorDAO().getSifCollectorRepository()
                .findAll(new Sort(Sort.Direction.ASC, "description")).iterator();

        return ResponseEntity.status(HttpStatus.OK).body(sifCollectorSet);
    }

    @RequestMapping(value = "save-collector", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveCollector(@Valid @RequestBody SifCollectorView sifCollectorView, BindingResult result) {
        sifCollectorService.getSifCollectorValidator().validateView(sifCollectorView, result);
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        } else {
            SifCollector sifCollector = sifCollectorView.buildEntity();
            sifCollector = sifCollectorService.persistEntity(sifCollector);

            if (sifCollector == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(sifCollector);
            }
        }
    }

    @RequestMapping(value = "get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get() {
        String response = "{" +
                "\"datasourceUrl\": \"" + Properties.datasourceUrl + "\"" +
                ",\"datasourceUsername\": \"" + Properties.datasourceUsername + "\"" +
                ",\"applicationName\": \"" + Properties.applicationName + "\"" +
                ",\"resourcePath\": \"" + Properties.resourcePath + "\"" +
                "}";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
