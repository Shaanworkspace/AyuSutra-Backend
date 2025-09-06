package com.ayusutra.Service;

import com.ayusutra.DTO.MedicalRecordResponse;
import com.ayusutra.Entity.Doctor;
import com.ayusutra.Entity.MedicalRecord;
import com.ayusutra.Entity.Patient;
import com.ayusutra.Repository.DoctorRepository;
import com.ayusutra.Repository.MedicalRecordRepository;
import com.ayusutra.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<MedicalRecordResponse> getAllMedicalRecords() {
        List<MedicalRecord> records = medicalRecordRepository.findAll();
        // Yeh sirf collect karega
        return records.stream()
                .map(this::medicalRecordToDto)   // 👈 converter method call
                .collect(Collectors.toList());
    }

    // 🔹 Converter: Entity -> DTO
    public MedicalRecordResponse medicalRecordToDto(MedicalRecord record) {
        return new MedicalRecordResponse(
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
    public MedicalRecord updateMedicalRecord(Long recordId, MedicalRecord updated) {
        MedicalRecord existing = getMedicalRecordById(recordId);

        existing.setDiagnosis(updated.getDiagnosis());
        existing.setPrescribedTreatment(updated.getPrescribedTreatment());
        existing.setMedications(updated.getMedications());
        existing.setAllergies(updated.getAllergies());
        existing.setMedicalHistoryNotes(updated.getMedicalHistoryNotes());
        existing.setFollowUpRequired(updated.getFollowUpRequired());
        existing.setNeedTherapy(updated.isNeedTherapy());
        existing.setTherapist(updated.getTherapist());
        existing.setApprovedByTherapist(updated.isApprovedByTherapist());
        existing.setTherapyPlan(updated.getTherapyPlan());

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