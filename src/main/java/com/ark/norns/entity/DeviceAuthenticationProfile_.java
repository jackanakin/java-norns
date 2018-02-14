package com.ark.norns.entity;

import com.ark.norns.enumerated.Status;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(DeviceAuthenticationProfile.class)
public class DeviceAuthenticationProfile_ {
    public static volatile SingularAttribute<DeviceAuthenticationProfile, String> description;
    public static volatile SingularAttribute<DeviceAuthenticationProfile, String> username;
    public static volatile SingularAttribute<DeviceAuthenticationProfile, String> password;
    public static volatile SingularAttribute<DeviceAuthenticationProfile, Status> status;
}
