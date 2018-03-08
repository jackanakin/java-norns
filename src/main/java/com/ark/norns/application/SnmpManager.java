package com.ark.norns.application;

import com.ark.norns.dataStructure.MibFile;
import com.ark.norns.dataStructure.MibFileOid;
import com.ark.norns.dataStructure.TreeNode;
import com.ark.norns.entity.Device;
import com.ark.norns.service.MibFileService;
import com.ark.norns.specification.MibFileSpecification;
import javafx.util.Pair;
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

@Component
public class SnmpManager {
    @Autowired
    private MibFileService mibFileService;

    public void translateVariableBindingIntoMibFileOid(List<VariableBinding> list) {
        List<MibFile> mibFiles = mibFileService.getMibFileDAO().getMibFileRepository().findAll(MibFileSpecification.translateVariableBindingIntoMibFileOid());
        Collections.sort(mibFiles, MibFile.ROOT_OID_DESC);
        List<Pair<MibFile, Boolean>> mibFileCache = new ArrayList<>();

        TreeNode<Integer, String, MibFileOid> root = MibManager.initializeYggdrasil(new TreeNode(1, "1", new MibFileOid("1", "iso", MibManager.yggdrasil), 0));
        for (VariableBinding var : list) {
            for (MibFile mibVar : mibFiles) {
                if (var.getOid().toString().startsWith(mibVar.getRootOID())) {
                    if (!mibFileCache.contains(mibVar)){

                    }
                    System.out.println(var.getOid().toString() + ": " + mibVar.getRootOID());
                    break;
                    /// carregar mib file objects em root tree
                    /// pesquisar na root tree o oid atual
                }
            }
        }
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

    /////////////////////
    ////////////
    /////////
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
