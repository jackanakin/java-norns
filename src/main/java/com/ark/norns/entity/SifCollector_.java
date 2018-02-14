package com.ark.norns.entity;

import com.ark.norns.enumerated.Status;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SifCollector.class)
public class SifCollector_ {
    public static volatile SingularAttribute<SifCollector, String> identifier;
    public static volatile SingularAttribute<SifCollector, String> description;
    public static volatile SingularAttribute<SifCollector, String> databaseUrl;
    public static volatile SingularAttribute<SifCollector, Status> status;
}
