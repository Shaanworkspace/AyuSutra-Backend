package com.ayusutra.Service;

import com.ayusutra.Entity.Patient;
import com.ayusutra.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    // ✅ Register new patient
    public Patient registerPatient(Patient patient) {
        patient.setCreatedAt(LocalDateTime.now());
        patient.setUpdatedAt(LocalDateTime.now());
        // NOTE: in production, hash password before saving
        return patientRepository.save(patient);
    }

    // ✅ Get patient by ID
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    // ✅ Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
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
