package com.ark.norns.entity;

import com.ark.norns.enumerated.Status;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(DeviceCommunityProfile.class)
public class DeviceCommunityProfile_ {
    public static volatile SingularAttribute<DeviceCommunityProfile, String> description;
    public static volatile SingularAttribute<DeviceCommunityProfile, Status> status;
}
