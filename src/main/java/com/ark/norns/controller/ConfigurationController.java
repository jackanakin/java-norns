package com.ark.norns.controller;

import com.ark.norns.application.Properties;
import com.ark.norns.entity.SifCollector;
import com.ark.norns.entity.entityView.SifCollectorView;
import com.ark.norns.service.SifCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/configuration/")
public class ConfigurationController {
    @Autowired
    private SifCollectorService sifCollectorService;

    @RequestMapping(value = "list-all", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listAllInterval() {
        Set<SifCollectorView> sifCollectorViewSet = new HashSet<>();
        sifCollectorService.getSifCollectorDAO().findAll().stream().forEach(
                sifCollector -> {
                    sifCollectorViewSet.add(sifCollector.buildView());
                }
        );
        return ResponseEntity.status(HttpStatus.OK).body(sifCollectorViewSet);
    }

    @RequestMapping(value = "save-collector", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveCollector(@Valid @RequestBody SifCollectorView sifCollectorView, BindingResult result) {
        sifCollectorService.getSifCollectorValidator().validateView(sifCollectorView, result);
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        } else {
            SifCollector sifCollector = sifCollectorView.buildEntity(sifCollectorView);
            sifCollector = sifCollectorService.persistEntity(sifCollector);

            if (sifCollector == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(sifCollector.buildView());
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
