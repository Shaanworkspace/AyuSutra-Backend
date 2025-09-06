package com.ayusutra.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Visit Details
    private LocalDateTime visitDate;   // kab doctor ko dikhaaya

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

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "patient_need_therapy",
            joinColumns = @JoinColumn(name = "medical_record_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private List<TherapySpecialization> requiredTherapy = new ArrayList<>();

    @OneToOne
    @JoinColumn(nullable = true)
    private TherapyPlan therapyPlan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "therapist_id", nullable = true)
    private Therapist therapist;

    private boolean approvedByTherapist;

    // Record creation date (auto set)
    private LocalDate createdDate;

    // Therapy name (like Panchakarma 5 types)
    @Column(length = 100)
    private String therapyName;   // e.g. "Vamana", "Virechana", "Basti", "Nasya", "Raktamokshana"

    private LocalDate startDate;   // therapy start
    private LocalDate endDate;     // therapy end

    private String status;         // Active, Completed, Pending
    private Integer noOfDays;      // duration

    @Lob
    private String doctorNotes;    // Doctor instructions

    private Double rating;         // Patient feedback rating (0–5)

    //Automatically set createdDate & visitDate when record is first created
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
        if (this.visitDate == null) {
            this.visitDate = LocalDateTime.now();
        }
    }
}