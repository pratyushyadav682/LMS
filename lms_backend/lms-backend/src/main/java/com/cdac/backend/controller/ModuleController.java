package com.cdac.backend.controller;

 
import com.cdac.backend.model.Module;
import com.cdac.backend.service.ModuleService;
 import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    private final ModuleService moduleService;

     
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    // Endpoint to get all modules
    @GetMapping
    public List<Module> getAllModules() {
        return moduleService.getAllModules();
    }
}

