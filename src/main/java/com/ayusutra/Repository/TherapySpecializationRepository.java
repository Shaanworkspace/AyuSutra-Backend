package com.ayusutra.Repository;

import com.ayusutra.Entity.TherapyPlan;
import com.ayusutra.Entity.TherapySpecialization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TherapySpecializationRepository extends JpaRepository<TherapySpecialization, Long> {
}
