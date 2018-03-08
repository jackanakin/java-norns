package com.ark.norns.service;

import com.ark.norns.application.Properties;
import com.ark.norns.entity.Device;
import org.snmp4j.CommunityTarget;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.springframework.stereotype.Service;

@Service
public class SnmpService {

    public CommunityTarget createTarget(Device device) {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(device.getDeviceCommunityProfile().getDescription()));
        target.setAddress(GenericAddress.parse("udp:" + device.getIpv4() + "/" + device.getPort()));
        target.setVersion(device.getSnmpv().getVersion());
        target.setRetries(Properties.snmpWalkRetries);
        target.setTimeout(Properties.snmpWalkTimeout);
        return target;
    }
}
