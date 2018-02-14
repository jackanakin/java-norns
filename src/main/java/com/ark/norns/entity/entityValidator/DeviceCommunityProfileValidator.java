package com.ark.norns.entity.entityValidator;

import com.ark.norns.entity.entityView.DeviceCommunityProfileView;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DeviceCommunityProfileValidator implements Validator {
    @Override
    public boolean supports(Class entity) {
        return DeviceCommunityProfileView.class.equals(entity);
    }

    public void validate(Object target, Errors errors) {
        throw new UnsupportedOperationException();
    }

    public void validateView(Object target, Errors errors) {
        DeviceCommunityProfileView deviceCommunityProfileView = (DeviceCommunityProfileView) target;
        if (deviceCommunityProfileView.getDescription() == null || deviceCommunityProfileView.getDescription().length() <= 0) {
            errors.reject("device_community_description", "O campo 'Descrição' deve ser maior que 1 (hum)!");
        }
    }
}
