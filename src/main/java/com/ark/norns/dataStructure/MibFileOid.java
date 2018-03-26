package com.ark.norns.dataStructure;

public class MibFileOid {
    private String identifier;
    private String oid;
    private MibFile mibFile;

    private MibParsingSyntax syntax;
    private MibParsingAccess access;
    private String description = "";
    private Object value;

    public MibFileOid(MibFile mibFile, String oid, Object value) {
        this.oid = oid;
        this.mibFile = mibFile;
        this.value = value;
    }

    public MibFileOid(String identifier, MibFile mibFile) {
        this.identifier = identifier;
        this.mibFile = mibFile;
    }

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

    public MibParsingSyntax getSyntax() {
        return syntax;
    }

    public void setSyntax(MibParsingSyntax syntax) {
        this.syntax = syntax;
    }

    public MibParsingAccess getAccess() {
        return access;
    }

    public void setAccess(MibParsingAccess access) {
        this.access = access;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void concatDescription(String s){
        description = description + " " + s;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return oid + ":" + identifier;
    }
}
