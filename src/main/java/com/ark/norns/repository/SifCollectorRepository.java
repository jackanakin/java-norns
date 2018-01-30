package com.ark.norns.repository;

import com.ark.norns.entity.SifCollector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface SifCollectorRepository extends JpaRepository<SifCollector, Long>, JpaSpecificationExecutor<SifCollector> {
}
