package com.ark.norns.application;

import com.ark.norns.TestUnit;
import com.ark.norns.dao.MibFileDAO;
import com.ark.norns.dataStructure.MibFile;
import com.ark.norns.dataStructure.MibFileOid;
import com.ark.norns.dataStructure.TreeNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class MibManagerTest extends TestUnit {

    @Autowired
    private MibManager mibManager;

    @Autowired
    private MibFileDAO mibFileDAO;

    private Set<MibFile> scenario;

    @Before
    public void loadScenario() {
        scenario = new HashSet<>(mibFileDAO.findAll());
    }

    @Test(timeout = 1300)
    public void createRootTreeNode() {
        TreeNode<Integer, String, MibFileOid> root = mibManager.createRootTreeNode();
        List<String> rootOids = new ArrayList<>();
        for (TreeNode<Integer, String, MibFileOid> node : root.getChildren()) {
            if (node.getPath() != null) {
                rootOids.add(node.getPath());
            }
        }
        URL filePath = this.getClass().getClassLoader().getResource("root.json");

        Stream<String> stream = rootOids.stream();

        try {
            try (PrintWriter pw = new PrintWriter(filePath.toString(), "UTF-8")) {
                stream.map(s -> s).forEachOrdered(pw::println);
            }
        }catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    @Ignore
    public void loadMibIndexes() {
        //createRootTreeNode
        //mibManager.loadMibIndexes();

    }
}
