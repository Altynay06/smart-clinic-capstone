package com.project.back_end.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.util.*;
@Document(collection="prescriptions")
public class Prescription {
 @Id public String id;
 @NotNull public Long appointmentId;
 public Long patientId;
 public Long doctorId;
 @NotBlank public String medication;
 @NotBlank public String dosage;
 @NotBlank public String duration;
 public String notes;
}
