package com.ark.norns.repository;

import com.ark.norns.dataStructure.MibFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MibFileRepository extends JpaRepository<MibFile, Long>, JpaSpecificationExecutor<MibFile> {
}
