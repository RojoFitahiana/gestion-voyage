package com.taxibrousse.gestionvoyage.controller;

import com.taxibrousse.gestionvoyage.dto.AvisCreationDTO;
import com.taxibrousse.gestionvoyage.model.Avis;
import com.taxibrousse.gestionvoyage.service.AvisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avis")
public class AvisController {

    @Autowired
    private AvisService avisService;

    @GetMapping
    public List<Avis> getAllAvis() {
        return avisService.getAllAvis();
    }

    @PostMapping
    public Avis createAvis(@RequestBody AvisCreationDTO dto) {
        return avisService.createAvis(dto);
    }

    @GetMapping("/{id}")
    public Avis getAvisById(@PathVariable Long id) {
        return avisService.getAvisById(id);
    }

    @PutMapping("/{id}")
    public Avis updateAvis(@PathVariable Long id, @RequestBody Avis avis) {
        return avisService.updateAvis(id, avis);
    }

    @DeleteMapping("/{id}")
    public void deleteAvis(@PathVariable Long id) {
        avisService.deleteAvis(id);
    }
}