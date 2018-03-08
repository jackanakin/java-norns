package com.ark.norns.service;

import com.ark.norns.dao.MibFileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MibFileService {
    @Autowired
    private MibFileDAO mibFileDAO;

    public MibFileDAO getMibFileDAO() {
        return mibFileDAO;
    }
}
