package com.ayusutra.Controller;


import com.ayusutra.DTO.Request.LoginRequestDTO;
import com.ayusutra.DTO.Response.TherapistResponseDTO;
import com.ayusutra.Entity.Therapist;
import com.ayusutra.Service.TherapistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapists")
@RequiredArgsConstructor
public class TherapistController {

    private final TherapistService therapistService;

    @PostMapping
    public Therapist createTherapist(@RequestBody Therapist therapist) {
        return therapistService.addTherapist(therapist);
    }

    @PostMapping("/bulk")
    public List<Therapist> createTherapists(@RequestBody List<Therapist> therapists) {
        return therapistService.createTherapists(therapists);
    }

    @GetMapping
    public List<TherapistResponseDTO> getAllTherapists() {
        return therapistService.getAllTherapists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTherapistById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(therapistService.getTherapistById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/expertise/{exp}")
    public List<TherapistResponseDTO> getTherapistsByExpertise(@PathVariable String exp) {
        return therapistService.getTherapistsByExpertise(exp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            Therapist therapist = therapistService.login(request.getEmail(), request.getPassword());
            TherapistResponseDTO therapistResponseDTO = therapistService.therapistToDTO(therapist);
            return ResponseEntity.ok(therapistResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTherapist(@PathVariable Long id) {
        try {
            therapistService.deleteTherapist(id);
            return ResponseEntity.ok("✅ Therapist deleted with id"+id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}