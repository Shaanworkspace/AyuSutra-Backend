package com.ayusutra.Repository;

import com.ayusutra.Entity.Doctor;
import com.ayusutra.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Find doctor by specialization
    List<Doctor> findBySpecialization(String specialization);

    Optional<Doctor> findByEmail(String email);

}