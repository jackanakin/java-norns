package com.ark.norns.entity.entityValidator;

import com.ark.norns.entity.entityView.SensorView;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValidator implements Validator {
    @Override
    public boolean supports(Class entity) {
        return SensorView.class.equals(entity);
    }

    public void validate(Object target, Errors errors) {
        throw new java.lang.UnsupportedOperationException();
    }

    public void validateView(Object target, Errors errors) {
        SensorView sensor = (SensorView) target;
        if (sensor.getName() == null || sensor.getName().length() <= 1) {
            errors.reject("sensor_name", "O campo 'Nome' deve ser maior que 1 (hum)");
        }
        if (sensor.getOid() == null || sensor.getOid().length() <= 1) {
            errors.reject("sensor_oid", "O campo 'OID' deve ser maior que 1 (hum)");
        }
        if (sensor.getInterval() == null || sensor.getInterval().length() <= 0) {
            errors.reject("sensor_interval", "Deve ser selecionado o 'Intervalo'");
        }
    }
}
