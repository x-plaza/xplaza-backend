package com.backend.xplaza.service;

import com.backend.xplaza.model.Module;
import com.backend.xplaza.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepo;

    public void addModule(Module module) {
        moduleRepo.save(module);
    }

    public void updateModule(Module module) {
        moduleRepo.save(module);
    }

    public List<Module> listModules() {
        return moduleRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public String getModuleNameByID(Long id) {
        return moduleRepo.getName(id);
    }

    public void deleteModule(Long id) {
        moduleRepo.deleteById(id);
    }
}
