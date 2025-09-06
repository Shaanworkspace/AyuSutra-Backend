package com.ayusutra.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_records")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Visit Details
    private LocalDateTime visitDate;

    @Column(length = 255)
    private String symptoms;

    @Lob
    private String diagnosis;

    @Lob
    private String prescribedTreatment;

    @Lob
    private String allergies;

    @Lob
    private String medicalHistoryNotes;

    @Lob
    private String medications;

    private String followUpRequired;


    private boolean needTherapy;


    @ManyToMany
    @JoinTable(
            name = "patient_need_therapy",
            joinColumns = @JoinColumn(name = "medical_record_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private List<TherapySpecialization> requiredTherapy = new ArrayList<>();

    @OneToOne
    private TherapyPlan therapyPlan;

    @ManyToOne
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;


    private boolean approvedByTherapist;
}