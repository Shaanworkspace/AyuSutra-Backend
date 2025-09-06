package com.ayusutra.Repository;

import com.ayusutra.Entity.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TherapistRepository extends JpaRepository<Therapist, Long> {
    // Find therapist by expertise
    List<Therapist> findByExpertise(String expertise);
}