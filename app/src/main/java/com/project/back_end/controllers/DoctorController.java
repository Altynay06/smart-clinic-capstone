package com.project.back_end.controllers;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/doctor")
public class DoctorController {
 private final DoctorService doctors;private final TokenService tokens;
 public DoctorController(DoctorService doctors,TokenService tokens){this.doctors=doctors;this.tokens=tokens;}
 @GetMapping public Map<String,Object> getDoctors(@RequestParam(defaultValue="") String name,@RequestParam(defaultValue="") String specialty,@RequestParam(defaultValue="") String time){return Map.of("doctors",doctors.getDoctors(name,specialty,time));}
 @PostMapping public Doctor saveDoctor(@RequestHeader(value="Authorization",required=false) String token,@Valid @RequestBody Doctor doctor){tokens.validateToken(token,"admin");return doctors.saveDoctor(doctor);}
}
