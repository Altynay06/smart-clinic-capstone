package com.project.back_end.config;
import com.project.back_end.models.*;
import com.project.back_end.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.*;import java.util.*;
@Configuration
public class DemoData {
 @Bean CommandLineRunner seed(DoctorRepository doctors,PatientRepository patients,AdminRepository admins,AppointmentRepository appointments,BCryptPasswordEncoder passwords,@Value("${app.seed:false}") boolean enabled,@Value("${app.demo-password:}") String password){return args->{
  if(!enabled)return;if(password.length()<12)throw new IllegalStateException("DEMO_PASSWORD must have at least 12 characters");
  if(admins.count()==0){Admin admin=new Admin();admin.setUsername("admin");admin.setPassword(passwords.encode(password));admins.save(admin);}
  if(doctors.count()==0){String[] names={"Dr. Maya Chen","Dr. Alex Rivera","Dr. Sam Patel"};String[] specialties={"Cardiology","Dermatology","General Medicine"};for(int i=0;i<3;i++){Doctor doctor=new Doctor();doctor.setName(names[i]);doctor.setSpecialty(specialties[i]);doctor.setEmail("doctor"+(i+1)+"@clinic.example");doctor.setPassword(passwords.encode(password));doctor.setPhone("202555010"+i);doctor.setAvailableTimes(List.of("09:00-10:00","10:00-11:00","14:00-15:00"));doctors.save(doctor);}}
  if(patients.count()==0)for(int i=1;i<=5;i++){Patient patient=new Patient();patient.setName("Demo Patient "+i);patient.setEmail("patient"+i+"@clinic.example");patient.setPassword(passwords.encode(password));patient.setPhone("202555011"+i);patient.setAddress("Synthetic address "+i);patients.save(patient);}
  if(appointments.count()==0){var doctor=doctors.findAll().get(0);var people=patients.findAll();for(int i=0;i<3;i++){Appointment appointment=new Appointment();appointment.setDoctor(doctor);appointment.setPatient(people.get(i));appointment.setAppointmentTime(LocalDate.now().plusDays(1).atTime(i<2?9+i:14,0));appointment.setStatus(0);appointments.save(appointment);}}
 };}
}
