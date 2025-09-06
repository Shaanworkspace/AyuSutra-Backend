package com.ayusutra.Controller;


import com.ayusutra.DTO.Request.LoginRequestDTO;
import com.ayusutra.DTO.Response.PatientResponseDTO;
import com.ayusutra.Entity.MedicalRecord;
import com.ayusutra.Entity.Patient;
import com.ayusutra.Service.MedicalRecordService;
import com.ayusutra.Service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            Patient savedPatient = patientService.registerPatient(patient);
            // Return 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    // ✅ Get all patients
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // ✅ Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }


    // ✅ Delete patient
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    // ✅ Get patient by email
    @GetMapping("/email/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    // ✅ Get patient by phone
    @GetMapping("/phone/{phone}")
    public Patient getPatientByPhone(@PathVariable String phone) {
        return patientService.getPatientByPhone(phone);
    }

    // ✅ Search patients by name (first or last)
    @GetMapping("/search")
    public List<Patient> searchPatientsByName(@RequestParam String name) {
        return patientService.searchPatientsByName(name);
    }

    // ✅ Get patients by blood group
    @GetMapping("/blood-group/{bloodGroup}")
    public List<Patient> getPatientsByBloodGroup(@PathVariable String bloodGroup) {
        return patientService.getPatientsByBloodGroup(bloodGroup);
    }

    @PostMapping("/doctor-visit/{patientId}")
    public ResponseEntity<?> createMedicalRecord(@RequestBody MedicalRecord medicalRecord,
                                                 @PathVariable Long patientId) {
        try {
            medicalRecordService.createMedicalRecord(patientId, medicalRecord);
            PatientResponseDTO patientDto = patientService.getPatientById(patientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(patientDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            Patient patient = patientService.login(request.getEmail(), request.getPassword());
            PatientResponseDTO patientResponseDTO = patientService.patientToDto(patient);
            return ResponseEntity.ok(patientResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
