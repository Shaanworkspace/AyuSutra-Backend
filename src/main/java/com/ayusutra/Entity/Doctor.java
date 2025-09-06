package com.ayusutra.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, unique = true, length = 15)   // Mobile number
    private String phoneNumber;

    @Column(length = 100)   // E.g. "Cardiologist", "Dentist"
    private String specialization;

    @Column(nullable = false)
    private LocalDate joinedDate;

    @Column(length = 50)
    private String qualification;  // e.g., "MBBS, MD"

    @Column(length = 200)
    private String hospitalAffiliation; // Hospital/clinic name

    // Relationships
    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TherapyPlan> therapyPlans = new ArrayList<>();

}