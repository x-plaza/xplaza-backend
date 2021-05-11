package com.backend.xplaza.repository;

import com.backend.xplaza.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    @Query(value = "select module_name from modules where module_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from modules where module_id = ?1", nativeQuery = true)
    Module findItemById(long id);
}
