package com.ark.norns.dataStructure;

public class MibFileOid {
    private String identifier;
    private String oid;
    private MibFile mibFile;

    public MibFileOid(String oid, String identifier, MibFile mibFile) {
        this.identifier = identifier;
        this.mibFile = mibFile;
        this.oid = oid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public MibFile getMibFile() {
        return mibFile;
    }

    public void setMibFile(MibFile mibFile) {
        this.mibFile = mibFile;
    }

    @Override
    public String toString() {
        return oid + ":" + identifier;
    }
}
