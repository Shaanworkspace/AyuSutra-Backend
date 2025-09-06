package com.ayusutra.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "doctor")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)  // Required & max size
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)  // Avoid duplicate emails
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 15)   // Mobile number
    private String phoneNumber;

    @Column(length = 100)   // E.g. "Cardiologist", "Dentist"
    private String specialization;


    @Column(length = 50)
    private String qualification;  // e.g., "MBBS, MD"

    @Column(length = 200)
    private String hospitalAffiliation; // Hospital/clinic name

    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    // Relationships
    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<TherapyPlan> therapyPlans = new ArrayList<>();
}