package com.ark.norns.dao;

import com.ark.norns.entity.SifCollector;
import com.ark.norns.repository.SifCollectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SifCollectorDAO extends _DAO<SifCollector, Long> {
    @Autowired
    protected SifCollectorDAO(SifCollectorRepository sifCollectorRepository) {
        super(sifCollectorRepository);
    }

    public SifCollectorRepository getSifCollectorRepository() {
        return (SifCollectorRepository) super.getBaseRepository();
    }
}
