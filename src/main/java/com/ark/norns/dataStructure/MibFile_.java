package com.ark.norns.dataStructure;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(MibFile.class)
public class MibFile_ {
    public static volatile SingularAttribute<MibFile, String> fileName;
    public static volatile SingularAttribute<MibFile, String> rootOID;
    public static volatile SetAttribute<MibFile, MibFile> dependencies;
}