package com.project.back_end;
import com.project.back_end.models.*;import com.project.back_end.repo.*;import com.project.back_end.services.TokenService;
import org.junit.jupiter.api.*;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;import org.springframework.boot.test.mock.mockito.MockBean;import org.springframework.test.context.ActiveProfiles;import org.springframework.test.web.servlet.MockMvc;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;import static org.junit.jupiter.api.Assertions.*;
import java.time.*;import java.util.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class BackEndApplicationTests {
 @Autowired MockMvc mvc;@Autowired DoctorRepository doctors;@Autowired PatientRepository patients;@Autowired AppointmentRepository appointments;@Autowired TokenService tokens;@Autowired BCryptPasswordEncoder passwords;
 @MockBean PrescriptionRepository prescriptions;
 Doctor doctor;Patient patient;
 @BeforeEach void setup(){appointments.deleteAll();doctors.deleteAll();patients.deleteAll();doctor=new Doctor();doctor.setName("Demo Doctor");doctor.setSpecialty("Cardiology");doctor.setEmail("doctor@example.com");doctor.setPhone("2025550100");doctor.setPassword(passwords.encode("DemoPassword123!"));doctor.setAvailableTimes(List.of("09:00-10:00"));doctor=doctors.save(doctor);patient=new Patient();patient.setName("Demo Patient");patient.setEmail("patient@example.com");patient.setPassword(passwords.encode("DemoPassword123!"));patient.setPhone("2025550101");patient.setAddress("Synthetic address");patient=patients.save(patient);}
 @Test void catalogDoesNotLeakPassword()throws Exception{mvc.perform(get("/doctor")).andExpect(status().isOk()).andExpect(jsonPath("$.doctors[0].password").doesNotExist());}
 @Test void invalidLoginFails()throws Exception{mvc.perform(post("/patient/login").contentType("application/json").content("{\"email\":\"patient@example.com\",\"password\":\"wrong\"}")).andExpect(status().isUnauthorized());}
 @Test void validLoginReturnsToken()throws Exception{mvc.perform(post("/patient/login").contentType("application/json").content("{\"email\":\"patient@example.com\",\"password\":\"DemoPassword123!\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.token").isString());}
 @Test void patientCannotReadDoctorSchedule()throws Exception{mvc.perform(get("/appointments").header("Authorization","Bearer "+tokens.generateToken(patient.getId(),"patient"))).andExpect(status().isForbidden());}
 @Test void privateDataNeedsToken()throws Exception{mvc.perform(get("/patient/appointments")).andExpect(status().isUnauthorized());}
 @Test void bookingRejectsDuplicateSlot()throws Exception{String token="Bearer "+tokens.generateToken(patient.getId(),"patient");String data="{\"doctorId\":"+doctor.getId()+",\"appointmentTime\":\""+LocalDate.now().plusDays(1).atTime(9,0)+"\"}";mvc.perform(post("/appointments").header("Authorization",token).contentType("application/json").content(data)).andExpect(status().isOk());mvc.perform(post("/appointments").header("Authorization",token).contentType("application/json").content(data)).andExpect(status().isConflict());}
 @Test void invalidPatientIsRejected()throws Exception{mvc.perform(post("/patient").contentType("application/json").content("{\"name\":\"A\",\"email\":\"bad\",\"password\":\"x\",\"phone\":\"bad\",\"address\":\"\"}")).andExpect(status().isBadRequest());}
 @Test void portalIsRenderedByMvc()throws Exception{mvc.perform(get("/portal/admin")).andExpect(status().isOk()).andExpect(view().name("portal"));}
}
