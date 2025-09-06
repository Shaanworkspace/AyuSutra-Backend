package com.ayusutra.Service;


import com.ayusutra.DTO.Response.DoctorResponseDTO;
import com.ayusutra.DTO.Response.MedicalRecordResponseDTO;
import com.ayusutra.Entity.Doctor;
import com.ayusutra.Entity.MedicalRecord;
import com.ayusutra.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private MedicalRecordService medicalRecordService;


    public Doctor addDoctor(Doctor doctor) {
        // Check if email already exists
        if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Patient with this email already exists");
        }
        // Let timestamps be handled by entity @PrePersist
        return doctorRepository.save(doctor);
    }

    public DoctorResponseDTO getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(this::mapDoctorToDto)
                .orElse(null);
    }

    // Get doctors by specialization
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public List<DoctorResponseDTO> getAllDoctors() {
        // 1. Doctor entity nikaalo DB se
        // 2. Har Doctor ko mapDoctorToDto() se DTO banake collect karo
        return doctorRepository.findAll()
                .stream()
                .map(this::mapDoctorToDto)    // entity -> DTO conversion
                .collect(Collectors.toList());
    }
    // Convert Doctor entity -> DoctorResponseDTO
    public DoctorResponseDTO mapDoctorToDto(Doctor doctor) {

        // Agar doctor ke medicalRecords hain to unhe DTO me convert karo
        List<MedicalRecordResponseDTO> recordResponses = doctor.getMedicalRecords() != null
                ? doctor.getMedicalRecords().stream()
                .map(this::mapRecordToDto)  // convert every MedicalRecord to DTO
                .collect(Collectors.toList())
                : List.of(); // agar null hai to empty list

        // Ab doctor ka DTO banaao
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getEmail(),
                doctor.getPhoneNumber(),
                doctor.getSpecialization(),
                doctor.getQualification(),
                doctor.getHospitalAffiliation(),
                recordResponses
        );
    }
    // Convert MedicalRecord entity -> MedicalRecordResponse DTO
    private MedicalRecordResponseDTO mapRecordToDto(MedicalRecord record) {
        return medicalRecordService.medicalRecordToDto(record);
    }

    public Doctor login(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("❌ Doctor not found with email: " + email));

        if (!doctor.getPassword().equals(password)) {
            throw new IllegalArgumentException("❌ Invalid password");
        }
        return doctor;
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> createDoctors(List<Doctor> doctors) {
        return doctorRepository.saveAll(doctors);
    }


    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
