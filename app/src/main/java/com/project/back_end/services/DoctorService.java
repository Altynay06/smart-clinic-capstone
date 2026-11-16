package com.project.back_end.services;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;
@Service @Transactional
public class DoctorService {
 private final DoctorRepository doctors;private final BCryptPasswordEncoder passwords;
 public DoctorService(DoctorRepository doctors,BCryptPasswordEncoder passwords){this.doctors=doctors;this.passwords=passwords;}
 public List<Doctor> getDoctors(String name,String specialty,String time){return doctors.findByNameContainingIgnoreCaseAndSpecialtyContainingIgnoreCase(name,specialty).stream().filter(d->time.isBlank()||d.getAvailableTimes().stream().anyMatch(t->time.equalsIgnoreCase("AM")?Integer.parseInt(t.substring(0,2))<12:time.equalsIgnoreCase("PM")?Integer.parseInt(t.substring(0,2))>=12:t.startsWith(time))).toList();}
 public Doctor saveDoctor(Doctor doctor){if(doctors.findByEmail(doctor.getEmail()).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");
  doctor.setId(null);for(String slot:doctor.getAvailableTimes())if(!slot.matches("([01][0-9]|2[0-3]):00-([01][0-9]|2[0-3]):00"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Use hourly slots HH:00-HH:00");
  doctor.setPassword(passwords.encode(doctor.getPassword()));return doctors.save(doctor);}
}
