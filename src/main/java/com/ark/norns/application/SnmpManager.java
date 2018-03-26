package com.ark.norns.application;

import com.ark.norns.dataStructure.MibFile;
import com.ark.norns.dataStructure.MibFileOid;
import com.ark.norns.dataStructure.TreeNode;
import com.ark.norns.entity.Device;
import com.ark.norns.service.MibFileService;
import com.ark.norns.specification.MibFileSpecification;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SnmpManager {
    @Autowired
    private MibFileService mibFileService;

    @Autowired
    private MibManager mibManager;

    @Autowired
    public SnmpManager(MibFileService mibFileService, MibManager mibManager) {
        this.mibFileService = mibFileService;
        this.mibManager = mibManager;
    }

    public void translateVariableBindingIntoMibFileOid(List<VariableBinding> list) {
        List<MibFile> mibFiles = mibFileService.getMibFileDAO().getMibFileRepository().findAll(MibFileSpecification.translateVariableBindingIntoMibFileOid());
        Set<MibFile> cachedMibFile = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        Collections.sort(mibFiles, MibFile.ROOT_OID_DESC);
        TreeNode<Integer, String, MibFileOid> root = mibManager.createRootTreeNode();
        /*for (MibFile mibVar: root.getMibFileComposition()){
            root = mibManager.listMibFileObjectType(mibVar, root); //ta voltando nulo em algum momento
            cachedMibFile.add(mibVar);
        }*/

        long start = System.currentTimeMillis();
        OID_LOOP:
        for (VariableBinding var : list) {
            for (MibFile mibVar : mibFiles) {
                if (var.getOid().toString().startsWith(mibVar.getRootOID())) {
                    // Carrega os identificadores dos arquivos mib necessários na àrvore
                    if (!cachedMibFile.contains(mibVar)){
                        Set<MibFile> newCache = mibManager.loadMibIndexes(new HashSet<>(Arrays.asList(mibVar)), cachedMibFile, root, Properties.mibFilesPath);
                        if (newCache.size() > 0){
                            cachedMibFile.addAll(newCache);
                            if (cachedMibFile.contains(mibVar)) {
                                root = mibManager.populateIanaTree(mibVar.getModules(), mibVar, root);
                                if (mibVar.getRootOID() != null) {
                                    root = mibManager.listMibFileObjectType(mibVar, root);
                                }
                            }
                        }
                    }
                    if (cachedMibFile.contains(mibVar)){
                        queue.clear();
                        queue.addAll(Arrays.stream(var.getOid().toIntArray()).boxed().collect(Collectors.toList()));
                        MibFileOid oid = new MibFileOid(mibVar, var.getOid().toString(), var.getVariable());
                        root.addLeaf(queue, null, oid.getOid(), oid);
                    }
                    break;
                }
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("Tempo: " + (end-start));
    }

    public List<VariableBinding> doWalk(Device device) throws IOException {
        List<VariableBinding> snmpItemList = new ArrayList<>();
        CommunityTarget target = createTarget(device);
        TransportMapping transport = null;
        Snmp snmp = null;

        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();

            TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
            List<TreeEvent> events = treeUtils.getSubtree(target, new OID(Properties.snmpWalkOidStart));
            if (events == null || events.size() == 0) {
                System.out.println("Error: Unable to read table...");
                return snmpItemList;
            }

            for (TreeEvent event : events) {
                if (event == null) {
                    continue;
                }
                if (event.isError()) {
                    System.out.println("Error: table OID [" + Properties.snmpWalkOidStart + "] " + event.getErrorMessage());
                    continue;
                }

                VariableBinding[] varBindings = event.getVariableBindings();

                if (varBindings != null || varBindings.length != 0) {
                    snmpItemList.addAll(Arrays.asList(varBindings));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        snmp.close();

        return snmpItemList;
    }

    public CommunityTarget createTarget(Device device) {
        CommunityTarget target = new CommunityTarget();

        target.setCommunity(new OctetString(device.getDeviceCommunityProfile() != null ? device.getDeviceCommunityProfile().getDescription() : "public"));
        target.setAddress(GenericAddress.parse("udp:" + device.getIpv4() + "/" + device.getPort()));
        target.setVersion(Integer.parseInt(Properties.snmpWalkVersion.toString()));
        target.setRetries(Properties.snmpWalkRetries);
        target.setTimeout(Properties.snmpWalkTimeout);

        return target;
    }
}
