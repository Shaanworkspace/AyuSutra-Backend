package com.ayusutra.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Therapist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String expertise;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapyPlan> therapyPlans = new ArrayList<>();

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<ScheduleSlot> scheduleSlots = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "therapist_have_specialization",
            joinColumns = @JoinColumn(name = "therapist_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private List<TherapySpecialization> specializations = new ArrayList<>();

    @OneToMany
    private List<MedicalRecord> medicalRecords;
}