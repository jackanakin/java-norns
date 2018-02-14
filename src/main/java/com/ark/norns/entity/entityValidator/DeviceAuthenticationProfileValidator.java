package com.ark.norns.entity.entityValidator;

import com.ark.norns.entity.entityView.DeviceAuthenticationProfileView;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DeviceAuthenticationProfileValidator implements Validator {
    @Override
    public boolean supports(Class entity) {
        return DeviceAuthenticationProfileView.class.equals(entity);
    }

    public void validate(Object target, Errors errors) {
        throw new UnsupportedOperationException();
    }

    public void validateView(Object target, Errors errors) {
        DeviceAuthenticationProfileView deviceAuthenticationProfileView = (DeviceAuthenticationProfileView) target;
        if (deviceAuthenticationProfileView.getDescription() == null || deviceAuthenticationProfileView.getDescription().length() <= 0) {
            errors.reject("device_auth_description", "O campo 'Descrição' deve ser maior que 1 (hum)!");
        }
    }
}
