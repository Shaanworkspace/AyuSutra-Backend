package com.ayusutra.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Profile
    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(length = 10)
    private String gender; // Male, Female, Other

    private LocalDate dateOfBirth;

    @Column(length = 5)
    private String bloodGroup; // A+, O-, etc.

    // Contact Information
    @Column(unique = true, nullable = false, length = 120)
    private String email;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    private String emergencyContact;

    // Authentication (hashed in real application)
    @Column(nullable = false)
    private String password;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}