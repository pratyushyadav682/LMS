package com.cdac.backend.service;

 
import com.cdac.backend.model.Module;
import com.cdac.backend.repository.ModuleRepository;
 import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

     
    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    // Method to fetch all modules
    public List<Module> getAllModules() {
        return moduleRepository.findAll(); // Fetching all modules from the database
    }
    
}
