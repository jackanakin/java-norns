package com.ark.norns.dao;

import com.ark.norns.dataStructure.MibFile;
import com.ark.norns.repository.MibFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MibFileDAO extends _DAO<MibFile, Long> {
    @Autowired
    protected MibFileDAO(MibFileRepository mibFileRepository) {
        super(mibFileRepository);
    }

    public MibFileRepository getMibFileRepository() {
        return (MibFileRepository) super.getBaseRepository();
    }
}
