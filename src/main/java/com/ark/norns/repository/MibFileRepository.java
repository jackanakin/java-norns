package com.ark.norns.repository;

import com.ark.norns.dataStructure.MibFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MibFileRepository extends JpaRepository<MibFile, Long>, JpaSpecificationExecutor<MibFile> {

    @Query("SELECT mf.fileName FROM MibFile mf")
    public List<String> findAllNames();
}
