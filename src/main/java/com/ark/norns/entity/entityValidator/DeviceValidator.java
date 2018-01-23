package com.ark.norns.entity.entityValidator;

import com.ark.norns.entity.entityView.DeviceView;
import com.ark.norns.util.NetworkUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DeviceValidator implements Validator {
    @Override
    public boolean supports(Class entity) {
        return DeviceView.class.equals(entity);
    }

    public void validate(Object target, Errors errors) {
        throw new UnsupportedOperationException();
    }

    public void validateView(Object target, Errors errors) {
        DeviceView deviceView = (DeviceView) target;
        if (deviceView.getIpv4() == null || !NetworkUtil.validateIPv4(deviceView.getIpv4())) {
            errors.reject("device_ipv4", "O campo 'Endereço IPv4' deve ser um endereço IPv4 válido!");
        }
        if (deviceView.getPort() == null || deviceView.getPort() <= 0) {
            errors.reject("device_port", "O campo 'Porta (SNMP)' deve ser maior que 0 (zero)");
        }
    }
}
