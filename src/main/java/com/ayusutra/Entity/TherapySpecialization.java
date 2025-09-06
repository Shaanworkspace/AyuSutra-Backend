package com.ayusutra.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "therapy_specializations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapySpecialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;   // e.g. "Abhyanga", "Vamana", "Shirodhara"

    private String description; // Optional: details about therapy

    @Builder.Default
    @ManyToMany(mappedBy = "specializations")
    private List<Therapist> therapists = new ArrayList<>();
}
