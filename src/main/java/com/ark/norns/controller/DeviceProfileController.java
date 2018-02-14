package com.ark.norns.controller;

import com.ark.norns.entity.DeviceAuthenticationProfile;
import com.ark.norns.entity.DeviceCommunityProfile;
import com.ark.norns.entity.entityView.DeviceAuthenticationProfileView;
import com.ark.norns.entity.entityView.DeviceCommunityProfileView;
import com.ark.norns.enumerated.Status;
import com.ark.norns.service.DeviceAuthenticationProfileService;
import com.ark.norns.service.DeviceCommunityProfileService;
import com.ark.norns.specification.DeviceAuthenticationProfileSpecification;
import com.ark.norns.specification.DeviceCommunityProfileSpecification;
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
@RequestMapping(value = "/api/device-profile/")
public class DeviceProfileController {
    @Autowired
    private DeviceAuthenticationProfileService deviceAuthenticationProfileService;

    @Autowired
    private DeviceCommunityProfileService deviceCommunityProfileService;

    //////// COMMUNITY PROFILE
    @RequestMapping(value = "listFilter-community-profile", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listFilterCommunityProfile() {
        Specification<DeviceCommunityProfile> specification = DeviceCommunityProfileSpecification.selectMenuFilter(Status.ENABLED);

        Iterator<DeviceCommunityProfile> deviceCommunityProfileIterator = deviceCommunityProfileService.getDeviceCommunityProfileDAO().getDeviceCommunityProfileRepository()
                .findAll(specification, new Sort(Sort.Direction.ASC, "description")).iterator();

        return ResponseEntity.status(HttpStatus.OK).body(deviceCommunityProfileIterator);
    }

    @RequestMapping(value = "remove-community-profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeCommunityProfile(@RequestParam Long id) {
        deviceCommunityProfileService.removeEntity(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "list-community-profile", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listAllCommunityProfile() {
        Iterator<DeviceCommunityProfile> deviceCommunityProfileIterator = deviceCommunityProfileService.getDeviceCommunityProfileDAO().findAll().iterator();

        return ResponseEntity.status(HttpStatus.OK).body(deviceCommunityProfileIterator);
    }

    @RequestMapping(value = "save-community", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveCommunityProfile(@Valid @RequestBody DeviceCommunityProfileView deviceCommunityProfileView, BindingResult result) {
        deviceCommunityProfileService.getDeviceCommunityProfileValidator().validateView(deviceCommunityProfileView, result);
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        } else {
            DeviceCommunityProfile deviceCommunityProfile = deviceCommunityProfileView.buildEntity();
            deviceCommunityProfile = deviceCommunityProfileService.persistEntity(deviceCommunityProfile);

            if (deviceCommunityProfile == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(deviceCommunityProfile);
            }
        }
    }

    //////// AUTHENTICATION PROFILE
    @RequestMapping(value = "listFilter-authentication-profile", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listFilterAuthenticationProfile() {
        Specification<DeviceAuthenticationProfile> specification = DeviceAuthenticationProfileSpecification.selectMenuFilter(Status.ENABLED);

        Iterator<DeviceAuthenticationProfile> deviceAuthenticationProfileIterator = deviceAuthenticationProfileService.getDeviceAuthenticationProfileDAO().getDeviceAuthenticationProfileRepository()
                .findAll(specification, new Sort(Sort.Direction.ASC, "description")).iterator();

        return ResponseEntity.status(HttpStatus.OK).body(deviceAuthenticationProfileIterator);
    }

    @RequestMapping(value = "remove-authentication-profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeAuthenticationProfile(@RequestParam Long id) {
        deviceAuthenticationProfileService.removeEntity(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "list-authentication-profile", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listAllAuthenticationProfile() {
        Iterator<DeviceAuthenticationProfile> deviceAuthenticationProfileIterator = deviceAuthenticationProfileService.getDeviceAuthenticationProfileDAO().findAll().iterator();

        return ResponseEntity.status(HttpStatus.OK).body(deviceAuthenticationProfileIterator);
    }

    @RequestMapping(value = "save-authentication-profile", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveDeviceAuthenticationProfile(@Valid @RequestBody DeviceAuthenticationProfileView deviceAuthenticationProfileView, BindingResult result) {
        deviceAuthenticationProfileService.getDeviceAuthenticationProfileValidator().validateView(deviceAuthenticationProfileView, result);
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        } else {
            DeviceAuthenticationProfile deviceAuthenticationProfile = deviceAuthenticationProfileView.buildEntity();
            deviceAuthenticationProfile = deviceAuthenticationProfileService.persistEntity(deviceAuthenticationProfile);

            if (deviceAuthenticationProfile == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(deviceAuthenticationProfile);
            }
        }
    }
}
