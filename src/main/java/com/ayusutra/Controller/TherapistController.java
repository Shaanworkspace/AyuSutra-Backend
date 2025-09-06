package com.ayusutra.Controller;


import com.ayusutra.Entity.Therapist;
import com.ayusutra.Service.TherapistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapists")
public class TherapistController {

    @Autowired
    private TherapistService therapistService;

    @PostMapping
    public Therapist createTherapist(@RequestBody Therapist therapist) {
        return therapistService.addTherapist(therapist);
    }

    @PostMapping("/bulk")
    public List<Therapist> createTherapists(@RequestBody List<Therapist> therapists) {
        return therapistService.createTherapists(therapists);
    }

    @GetMapping
    public List<Therapist> getAllTherapists() {
        return therapistService.getAllTherapists();
    }

    @GetMapping("/{id}")
    public Therapist getTherapistById(@PathVariable Long id) {
        return therapistService.getTherapistById(id);
    }



    @DeleteMapping("/{id}")
    public void deleteTherapist(@PathVariable Long id) {
        therapistService.deleteTherapist(id);
    }
}

