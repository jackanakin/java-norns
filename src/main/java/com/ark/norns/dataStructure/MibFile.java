package com.ark.norns.dataStructure;

import com.ark.norns.entity._Entity;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mib_file")
public class MibFile extends _Entity implements Comparable {
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "root_oid")
    private String rootOID;

    @Transient
    private Set<MibFile> dependencies = new HashSet<MibFile>();

    public MibFile() {
    }

    public static final Comparator<MibFile> ROOT_OID_DESC =
            new Comparator<MibFile>() {
                public int compare(MibFile e1, MibFile e2) {
                    return (e1.getRootOID().length() > e2.getRootOID().length() ? -1 :
                            (e1.getRootOID().length() == e2.getRootOID().length() ? 0 : 1));
                }
            };

    @Override
    public int compareTo(Object o) {
        MibFile o1 = (MibFile) o;
        if (this.rootOID.length() < o1.getRootOID().length()) {
            return -1;
        }
        if (this.rootOID.length() > o1.getRootOID().length()) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MibFile mibFile = (MibFile) o;

        return fileName.equals(mibFile.fileName);
    }

    @Override
    public int hashCode() {
        return fileName.hashCode();
    }

    public MibFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRootOID() {
        return rootOID;
    }

    public void setRootOID(String rootOID) {
        this.rootOID = rootOID;
    }

    public Set<MibFile> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<MibFile> dependencies) {
        this.dependencies = dependencies;
    }
}
