package com.ayusutra.Service;

import com.ayusutra.DTO.Response.MedicalRecordResponseDTO;
import com.ayusutra.Entity.Doctor;
import com.ayusutra.Entity.MedicalRecord;
import com.ayusutra.Entity.Patient;
import com.ayusutra.Repository.DoctorRepository;
import com.ayusutra.Repository.MedicalRecordRepository;
import com.ayusutra.Repository.PatientRepository;
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

    // 🔹 Converter: Entity -> DTO
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

                // 👇 Mapping new fields
                record.getCreatedDate(),
                record.getTherapyName(),
                record.getStartDate(),
                record.getEndDate(),
                record.getStatus(),
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

    /**
     *  Update medical record (doctor adds diagnosis, treatment etc.)
     */
    @Transactional
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord updatedRecord) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalRecord not found with id: " + id));

        // Only overwrite if value is not null
        if (updatedRecord.getMedicalHistoryNotes() != null && !updatedRecord.getMedicalHistoryNotes().isEmpty())
            existing.setMedicalHistoryNotes(updatedRecord.getMedicalHistoryNotes());

        if (updatedRecord.getMedications() != null && !updatedRecord.getMedications().isEmpty())
            existing.setMedications(updatedRecord.getMedications());

        if (updatedRecord.getFollowUpRequired() != null && !updatedRecord.getFollowUpRequired().isEmpty())
            existing.setFollowUpRequired(updatedRecord.getFollowUpRequired());

        // booleans default = false, so handle carefully:
        existing.setNeedTherapy(updatedRecord.isNeedTherapy());

        if (updatedRecord.getRequiredTherapy() != null && !updatedRecord.getRequiredTherapy().isEmpty())
            existing.setRequiredTherapy(updatedRecord.getRequiredTherapy());

        if (updatedRecord.getTherapyPlan() != null)
            existing.setTherapyPlan(updatedRecord.getTherapyPlan());

        if (updatedRecord.getStatus() != null && !updatedRecord.getStatus().isEmpty())
            existing.setStatus(updatedRecord.getStatus());

        if (updatedRecord.getNoOfDays() != null)
            existing.setNoOfDays(updatedRecord.getNoOfDays());

        return medicalRecordRepository.save(existing);
    }

    /**
     *  Delete a record
     */
    public void deleteMedicalRecord(Long recordId) {
        MedicalRecord record = getMedicalRecordById(recordId);
        medicalRecordRepository.delete(record);
    }
}