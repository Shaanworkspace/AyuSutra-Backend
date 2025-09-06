package com.ayusutra.Controller;

import com.ayusutra.DTO.Request.LoginRequestDTO;
import com.ayusutra.DTO.Response.DoctorResponseDTO;
import com.ayusutra.Entity.Doctor;
import com.ayusutra.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        try {
            Doctor doctor1 = doctorService.createDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(doctor1);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            Doctor doctor = doctorService.login(request.getEmail(), request.getPassword());
            DoctorResponseDTO doctor1 = doctorService.mapDoctorToDto(doctor);
            return ResponseEntity.ok(doctor1);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        DoctorResponseDTO doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Doctor not found!");
        }
        return ResponseEntity.ok(doctor);
    }



    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}

