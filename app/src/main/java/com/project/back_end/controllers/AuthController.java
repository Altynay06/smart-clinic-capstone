package com.project.back_end.controllers;
import com.project.back_end.repo.*;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.TokenService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.*;
@RestController
public class AuthController {
 private final DoctorRepository doctors;private final PatientRepository patients;private final AdminRepository admins;private final TokenService tokens;private final BCryptPasswordEncoder passwords;
 public AuthController(DoctorRepository doctors,PatientRepository patients,AdminRepository admins,TokenService tokens,BCryptPasswordEncoder passwords){this.doctors=doctors;this.patients=patients;this.admins=admins;this.tokens=tokens;this.passwords=passwords;}
 @PostMapping("/{role:admin|doctor|patient}/login") public Map<String,Object> login(@PathVariable String role,@Valid @RequestBody Login input){
  Long id;String hash;String name;
  try{switch(role){case "admin" -> {var user=admins.findByUsername(input.username()).orElseThrow();id=user.getId();hash=user.getPassword();name=user.getUsername();}case "doctor" -> {var user=doctors.findByEmail(input.email()).orElseThrow();id=user.getId();hash=user.getPassword();name=user.getName();}default -> {var user=patients.findByEmail(input.email()).orElseThrow();id=user.getId();hash=user.getPassword();name=user.getName();}}}catch(NoSuchElementException error){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials");}
  if(!passwords.matches(input.password(),hash))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials");return Map.of("token",tokens.generateToken(id,role),"id",id,"role",role,"name",name);
 }
}
