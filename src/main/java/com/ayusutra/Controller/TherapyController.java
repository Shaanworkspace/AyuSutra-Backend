package com.ayusutra.Controller;

import com.ayusutra.Entity.TherapyPlan;
import com.ayusutra.Service.TherapyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapies")
public class TherapyController {

    @Autowired
    private TherapyPlanService therapyService;

    @PostMapping
    public TherapyPlan createTherapy(@RequestBody TherapyPlan therapy) {
        return therapyService.assignTherapy(therapy);
    }

    @GetMapping
    public List<TherapyPlan> getAllTherapies() {
        return therapyService.getAllTherapies();
    }


}
