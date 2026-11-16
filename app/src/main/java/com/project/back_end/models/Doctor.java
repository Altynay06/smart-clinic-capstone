package com.project.back_end.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.*;
import java.util.*;

@Entity
@Table(name="doctor")
public class Doctor {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @NotBlank @Size(min=3,max=100)
 private String name;
 @NotBlank @Size(min=3,max=50)
 private String specialty;
 @NotBlank @Email @Column(unique=true,nullable=false)
 private String email;
 @NotBlank @Size(min=8) @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
 private String password;
 @NotBlank @Pattern(regexp="^[0-9]{10}$")
 private String phone;
 @ElementCollection(fetch=FetchType.EAGER)
 private List<String> availableTimes = new ArrayList<>();
 public Long getId(){return id;}
 public void setId(Long value){this.id=value;}
 public String getName(){return name;}
 public void setName(String value){this.name=value;}
 public String getSpecialty(){return specialty;}
 public void setSpecialty(String value){this.specialty=value;}
 public String getEmail(){return email;}
 public void setEmail(String value){this.email=value;}
 public String getPassword(){return password;}
 public void setPassword(String value){this.password=value;}
 public String getPhone(){return phone;}
 public void setPhone(String value){this.phone=value;}
 public List<String> getAvailableTimes(){return availableTimes;}
 public void setAvailableTimes(List<String> value){this.availableTimes=value;}

}
