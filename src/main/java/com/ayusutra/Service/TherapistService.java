package com.ayusutra.Service;



import com.ayusutra.Entity.Therapist;
import com.ayusutra.Repository.TherapistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TherapistService {

    @Autowired
    private TherapistRepository therapistRepository;

    // Add new therapist
    public Therapist addTherapist(Therapist therapist) {
        return therapistRepository.save(therapist);
    }

    // Get therapist by ID
    public Therapist getTherapistById(Long id) {
        return therapistRepository.findById(id).orElse(null);
    }

    // Get therapists by expertise
    public List<Therapist> getTherapistsByExpertise(String expertise) {
        return therapistRepository.findByExpertise(expertise);
    }

    // Get all therapists
    public List<Therapist> getAllTherapists() {
        return therapistRepository.findAll();
    }

    public List<Therapist> createTherapists(List<Therapist> therapists) {
        return therapistRepository.saveAll(therapists);
    }



    public void deleteTherapist(Long id) {
    }
}
