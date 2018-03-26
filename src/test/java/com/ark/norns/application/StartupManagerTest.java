package com.ark.norns.application;

import com.ark.norns.TestUnit;
import com.ark.norns.dao.MibFileDAO;
import com.ark.norns.dataStructure.MibFile;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Sql({"classpath:db/drop_data.sql", "classpath:db/db_inserts.sql"})
public class StartupManagerTest extends TestUnit {
    @Autowired
    private MibManager mibManager;

    @Autowired
    private MibFileDAO mibFileDAO;

    private Set<MibFile> scenario;

    @Before
    public void loadScenario() {
        scenario = new HashSet<>(mibFileDAO.findAll());
    }

    @Test(timeout = 600)
    public void testMibFilesInitialization() {
        Set<MibFile> list;
        assertEquals(5, scenario.size());

        mibManager.createMibFilesIndexes();

        list = new HashSet<>(mibFileDAO.findAll());
        assertEquals(11, list.size());

        int scenarioAsserted = 0;
        for (MibFile var : scenario) {
            for (MibFile item : list) {
                if (var.getFileName().equals(item.getFileName()) && (item.getRootOID() == null || var.getRootOID().equals(item.getRootOID()))) {
                    scenarioAsserted++;
                }
            }
        }
        assertTrue(scenarioAsserted == scenario.size());
    }
}
