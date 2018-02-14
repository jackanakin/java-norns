package com.ark.norns.service;

import com.ark.norns.dao.SifCollectorDAO;
import com.ark.norns.entity.SifCollector;
import com.ark.norns.entity.entityValidator.SifCollectorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SifCollectorService {
    @Autowired
    private SifCollectorValidator sifCollectorValidator;
    @Autowired
    private SifCollectorDAO sifCollectorDAO;

    public void removeEntity(Long id){
        getSifCollectorDAO().delete(getSifCollectorDAO().findOne(id));
    }

    public SifCollector persistEntity(SifCollector sifCollector) {
        return getSifCollectorDAO().save(sifCollector);
    }

    public SifCollectorValidator getSifCollectorValidator() {
        return sifCollectorValidator;
    }

    public SifCollectorDAO getSifCollectorDAO() {
        return sifCollectorDAO;
    }
}
