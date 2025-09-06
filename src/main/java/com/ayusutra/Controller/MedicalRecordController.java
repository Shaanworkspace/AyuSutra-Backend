package com.ayusutra.Controller;

import com.ayusutra.DTO.MedicalRecordResponse;
import com.ayusutra.Entity.MedicalRecord;
import com.ayusutra.Service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /**
     * Create a medical record (patient + doctor passed in)
     */
    @PostMapping("/{patientId}")
    public ResponseEntity<?> createMedicalRecord(@PathVariable Long patientId,
                                                 @RequestBody MedicalRecord record) {
        try {
            MedicalRecord saved = medicalRecordService.createMedicalRecord(patientId, record);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ " + e.getMessage());
        }
    }

    /**
     * Get all records
     */
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponse>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
    }

    /**
     * Get record by ID
     */
    @GetMapping("/{recordId}")
    public ResponseEntity<?> getRecordById(@PathVariable Long recordId) {
        try {
            return ResponseEntity.ok(medicalRecordService.getMedicalRecordById(recordId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }

    /**
     * Get all records for a patient
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecord>> getRecordsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByPatient(patientId));
    }

    /**
     * Get all records for a doctor
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecord>> getRecordsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByDoctor(doctorId));
    }

    /**
     * Update a medical record (doctor adds diagnosis, treatment, etc.)
     */
    @PutMapping("/{recordId}")
    public ResponseEntity<?> updateRecord(@PathVariable Long recordId,
                                          @RequestBody MedicalRecord updated) {
        try {
            MedicalRecord saved = medicalRecordService.updateMedicalRecord(recordId, updated);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }

    /**
     * Delete a medical record
     */
    @DeleteMapping("/{recordId}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long recordId) {
        try {
            medicalRecordService.deleteMedicalRecord(recordId);
            return ResponseEntity.ok("✅ Medical record deleted with ID: " + recordId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }
}