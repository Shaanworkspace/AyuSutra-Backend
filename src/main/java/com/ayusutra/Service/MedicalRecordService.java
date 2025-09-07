package com.ayusutra.Service;

import com.ayusutra.DTO.Response.MedicalRecordResponseDTO;
import com.ayusutra.Entity.Doctor;
import com.ayusutra.Entity.MedicalRecord;
import com.ayusutra.Entity.Patient;
import com.ayusutra.Entity.Therapist;
import com.ayusutra.Repository.DoctorRepository;
import com.ayusutra.Repository.MedicalRecordRepository;
import com.ayusutra.Repository.PatientRepository;
import com.ayusutra.Repository.TherapistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TherapistRepository therapistRepository;
    Logger logger = Logger.getLogger(MedicalRecordService.class.getName());
    /**
     * Creates a new Medical Record for a patient with a linked doctor.
     */
    public MedicalRecord createMedicalRecord(Long patientId, MedicalRecord record) {
        logger.info("createMedicalRecord reached ");
        // Fetch Patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));
        logger.info("Got Patient "+patient.getFirstName()+" "+patient.getLastName());
        //  Check if doctor was passed in JSON
        if (record.getDoctor() == null || record.getDoctor().getId() == null) {
            throw new IllegalArgumentException("Doctor ID is required");
        }

        //  Fetch Doctor
        Doctor doctor = doctorRepository.findById(record.getDoctor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + record.getDoctor().getId()));
        logger.info("Got Doctor "+doctor.getFirstName()+" "+doctor.getLastName());
        //  Attach properly
        record.setPatient(patient);
        record.setDoctor(doctor);
        logger.info("Set the patient and doctor");
        return medicalRecordRepository.save(record);
    }

    public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
        List<MedicalRecord> records = medicalRecordRepository.findAll();
        // Yeh sirf collect karega
        return records.stream()
                .map(this::medicalRecordToDto)   // 👈 converter method call
                .collect(Collectors.toList());
    }

    // Converter: Entity -> DTO
    public MedicalRecordResponseDTO medicalRecordToDto(MedicalRecord record) {
        return new MedicalRecordResponseDTO(
                record.getId(),
                record.getVisitDate(),
                record.getSymptoms(),
                record.getDiagnosis(),
                record.getPrescribedTreatment(),
                record.getPatient() != null ? record.getPatient().getId() : null,
                record.getPatient() != null
                        ? record.getPatient().getFirstName() + " " + record.getPatient().getLastName()
                        : null,
                record.getDoctor() != null ? record.getDoctor().getId() : null,
                record.getDoctor() != null
                        ? record.getDoctor().getFirstName() + " " + record.getDoctor().getLastName()
                        : null,
                record.getTherapist() != null ? record.getTherapist().getId() : null,
                record.getTherapist() != null
                        ? record.getTherapist().getFirstName() + " " + record.getTherapist().getLastName()
                        : null,


                record.getCreatedDate(),
                record.getTherapyName(),
                record.getStartDate(),
                record.getEndDate(),
                record.getStatus() != null ? record.getStatus().name() : null,
                record.getNoOfDays(),
                record.getDoctorNotes(),
                record.getRating()
        );
    }

    /**
     *  Get a record by ID
     */
    public MedicalRecord getMedicalRecordById(Long recordId) {
        return medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Medical Record not found with ID: " + recordId));
    }

    /**
     *  Get all records for a patient
     */
    public List<MedicalRecord> getMedicalRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    /**
     *  Get all records for a doctor
     */
    public List<MedicalRecord> getMedicalRecordsByDoctor(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId);
    }

    @Transactional
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord updatedRecord) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalRecord not found with id: " + id));

        // =========================================================================
        // Business Rule: DO NOT ALLOW CHANGING PATIENT OR DOCTOR VIA THIS UPDATE
        // =========================================================================
        // This means the incoming 'updatedRecord' JSON can have patient/doctor fields,
        // but our logic will ignore them to prevent accidental or unauthorized changes.
        // The doctor who initially created the record or is currently linked to it
        // should not be changeable here. Patient is also fixed.
        // =========================================================================

        // Only overwrite if value is not null and not empty for Strings
        if (updatedRecord.getMedicalHistoryNotes() != null && !updatedRecord.getMedicalHistoryNotes().isEmpty())
            existing.setMedicalHistoryNotes(updatedRecord.getMedicalHistoryNotes());

        if (updatedRecord.getMedications() != null && !updatedRecord.getMedications().isEmpty())
            existing.setMedications(updatedRecord.getMedications());

        if (updatedRecord.getFollowUpRequired() != null && !updatedRecord.getFollowUpRequired().isEmpty())
            existing.setFollowUpRequired(updatedRecord.getFollowUpRequired());

        // Booleans update directly (false is a valid update)
        existing.setNeedTherapy(updatedRecord.isNeedTherapy()); // This always updates if provided

        // Lists (ManyToMany)
        // If an empty list is sent, it will clear the requiredTherapy.
        // If you want to prevent clearing on empty list, use:
        // if (updatedRecord.getRequiredTherapy() != null && !updatedRecord.getRequiredTherapy().isEmpty())
        if (updatedRecord.getRequiredTherapy() != null) // Allows clearing the list if [] is sent
            existing.setRequiredTherapy(updatedRecord.getRequiredTherapy());
        // Note: For ManyToMany, complex updates (add/remove specific items) would need custom logic
        // This simply replaces the whole list.

        // OneToOne / ManyToOne object fields
        if (updatedRecord.getTherapyPlan() != null) // Allows setting/clearing TherapyPlan
            existing.setTherapyPlan(updatedRecord.getTherapyPlan());

        if (updatedRecord.getTherapist() != null) // Allows setting/clearing Therapist
            existing.setTherapist(updatedRecord.getTherapist());

        // New fields
        // Assuming diagnosis, prescribedTreatment are also doctor-updatable
        if (updatedRecord.getDiagnosis() != null && !updatedRecord.getDiagnosis().isEmpty())
            existing.setDiagnosis(updatedRecord.getDiagnosis());
        if (updatedRecord.getPrescribedTreatment() != null && !updatedRecord.getPrescribedTreatment().isEmpty())
            existing.setPrescribedTreatment(updatedRecord.getPrescribedTreatment());
        if (updatedRecord.getDoctorNotes() != null && !updatedRecord.getDoctorNotes().isEmpty())
            existing.setDoctorNotes(updatedRecord.getDoctorNotes());
        if (updatedRecord.getRating() != null) // Rating can be null or a double
            existing.setRating(updatedRecord.getRating());

        // Status and NoOfDays
        if (updatedRecord.getStatus() != null)
            existing.setStatus(updatedRecord.getStatus());
        if (updatedRecord.getNoOfDays() != null)
            existing.setNoOfDays(updatedRecord.getNoOfDays());


        // --- Important: Auto-update `updatedAt` field if it exists in MedicalRecord ---
        // If your MedicalRecord entity has an `updatedAt` field with @PreUpdate, it will be handled automatically.
        // Otherwise, you might set it here:
        // existing.setUpdatedAt(LocalDateTime.now());

        return medicalRecordRepository.save(existing);
    }
    @Transactional
    public MedicalRecord assignTherapist(Long recordId, Long therapistId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Medical Record not found with id " + recordId));

        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Therapist not found with id " + therapistId));

        record.setTherapist(therapist);

        return medicalRecordRepository.save(record);
    }

    /**
     *  Delete a record
     */
    public void deleteMedicalRecord(Long recordId) {
        MedicalRecord record = getMedicalRecordById(recordId);
        medicalRecordRepository.delete(record);
    }
}