package com.ark.norns.entity.entityValidator;

import com.ark.norns.entity.entityView.SifCollectorView;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SifCollectorValidator implements Validator {
    @Override
    public boolean supports(Class entity) {
        return SifCollectorView.class.equals(entity);
    }

    public void validate(Object target, Errors errors) {
        throw new UnsupportedOperationException();
    }

    public void validateView(Object target, Errors errors) {
        SifCollectorView sifCollectorView = (SifCollectorView) target;
        if (sifCollectorView.getIdentifier() == null || sifCollectorView.getIdentifier().length() <= 0) {
            errors.reject("identifier", "O campo 'Identificador' deve ser maior que 1 (hum)!");
        }
        if (sifCollectorView.getDescription() == null || sifCollectorView.getDescription().length() <= 0) {
            errors.reject("description", "O campo 'Descrição' deve ser maior que 1 (hum)!");
        }
        if (sifCollectorView.getDatabaseUrl() == null || sifCollectorView.getDatabaseUrl().length() <= 0) {
            errors.reject("databaseUrl", "O campo 'URL do banco de dados' é inválido!");
        }
    }
}
