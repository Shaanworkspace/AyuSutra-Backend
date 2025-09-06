package com.ayusutra.Service;

import com.ayusutra.DTO.Response.MedicalRecordResponseDTO;
import com.ayusutra.DTO.Response.PatientResponseDTO;
import com.ayusutra.Entity.Patient;
import com.ayusutra.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final MedicalRecordService medicalRecordService;

    public Patient login(String email, String password) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("❌ Patient not found with email: " + email));

        if (!patient.getPassword().equals(password)) {
            throw new IllegalArgumentException("❌ Invalid password");
        }
        return patient;
    }

    public Patient registerPatient(Patient patient) {
        // Check if email already exists
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Patient with this email already exists");
        }

        // Let timestamps be handled by entity @PrePersist
        return patientRepository.save(patient);
    }


    public PatientResponseDTO patientToDto(Patient patient) {
        // Convert records into DTOs
        List<MedicalRecordResponseDTO> recordResponses = patient.getMedicalRecords() != null
                ? patient.getMedicalRecords().stream()
                .map(medicalRecordService::medicalRecordToDto) // reuse existing record mapper
                .collect(Collectors.toList())
                : List.of();

        return new PatientResponseDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getAge(),
                patient.getGender(),
                patient.getDateOfBirth(),
                patient.getBloodGroup(),
                patient.getEmail(),
                patient.getPhoneNumber(),
                patient.getAddress(),
                patient.getEmergencyContact(),
                patient.getCreatedAt(),
                patient.getUpdatedAt(),
                recordResponses
        );
    }

    // Get patient by ID
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));

        return patientToDto(patient);
    }

    //  Get all patients with mapped DTOs
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::patientToDto) // reuse the converter here as well
                .collect(Collectors.toList());
    }


    // ✅ Delete patient
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    // ✅ Get patient by email (for login / lookup)
    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email).orElse(null);
    }

    // ✅ Get patient by phone number
    public Patient getPatientByPhone(String phoneNumber) {
        return patientRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    // ✅ Search patients by first or last name (case-insensitive)
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    // ✅ Filter patients by blood group
    public List<Patient> getPatientsByBloodGroup(String bloodGroup) {
        return patientRepository.findByBloodGroup(bloodGroup);
    }

}
