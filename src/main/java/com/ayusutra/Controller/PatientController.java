package com.ayusutra.Controller;


import com.ayusutra.Entity.MedicalRecord;
import com.ayusutra.Entity.Patient;
import com.ayusutra.Service.PatientService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

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

    @PostMapping("/bulk")
    public List<Patient> createPatients(@RequestBody List<Patient> patients) {
        return patients.stream().map(patientService::registerPatient).toList();
    }

    // ✅ Get all patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    // ✅ Get patient by ID
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
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

    @PostMapping("medical-record/{patientId}")
    public ResponseEntity<String> createMedicalRecord(@RequestBody MedicalRecord medicalRecord,
                                                      @PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Patient not found with ID: " + patientId);
        }

        medicalRecord.setPatient(patient);
        patient.getMedicalRecords().add(medicalRecord);
        patientService.registerPatient(patient);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("✅ Medical record added successfully for Patient ID: " + patientId);
    }
}
