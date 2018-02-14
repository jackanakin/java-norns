package com.ark.norns.controller;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.entity.entityView.DeviceView;
import com.ark.norns.entity.entityView.SensorView;
import com.ark.norns.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/device/")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private SifCollectorService sifCollectorService;

    @Autowired
    private DeviceAuthenticationProfileService authenticationProfileService;

    @Autowired
    private DeviceCommunityProfileService communityProfileService;

    @RequestMapping(value = "getById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@RequestParam Long id) {
        Device device = deviceService.eagerFetchDevice(deviceService.getDeviceRepository().findOne(id));
        if (device == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            DeviceView deviceView = device.buildView(sensorService.buildViewList(device.getSensors()));
            return ResponseEntity.status(HttpStatus.OK).body(deviceView);
        }
    }

    @RequestMapping(value = "page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Device> getPageable(@RequestParam int size, int page) {
        Page<Device> listElements = deviceService.getDeviceRepository().findAll(new PageRequest(page, size));
        for (Device device: listElements.getContent()){
            device.setSensors(null);
        }
        return listElements;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveDevice(@Valid @RequestBody DeviceView deviceView, BindingResult result) {
        deviceService.getDeviceValidator().validateView(deviceView, result);
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        } else {
            Device device = deviceView.buildEntity();
            Set<Sensor> sensorList = sensorService.buildEntityList(deviceView.getSensorList(), null);
            device.setSensors(sensorList);
            device.setSifCollector(sifCollectorService.getSifCollectorDAO().findOne(deviceView.getSifCollectorId()));

            if (deviceView.getAuthenticatioProfileId() != null) {
                device.setDeviceAuthenticationProfile(authenticationProfileService.getDeviceAuthenticationProfileDAO().findOne(deviceView.getAuthenticatioProfileId()));
            }
            if (deviceView.getCommunityProfileId() != null) {
                device.setDeviceCommunityProfile(communityProfileService.getDeviceCommunityProfileDAO().findOne(deviceView.getCommunityProfileId()));
            }
            device = deviceService.persistDevice(device);

            if (device == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(device);
            }
        }
    }

    @RequestMapping(value = "create-sensor", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addSensor(@Valid @RequestBody SensorView sensorView, BindingResult result) {
        sensorService.getSensorValidator().validateView(sensorView, result);
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        } else {
            sensorView.buildView();
            return ResponseEntity.status(HttpStatus.OK).body(sensorView);
        }
    }
}
