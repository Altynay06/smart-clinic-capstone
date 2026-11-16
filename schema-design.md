# Smart Clinic schema

MySQL stores relational data with generated BIGINT primary keys:

| Table | Fields | Constraints |
|---|---|---|
| admin | id, username, password | unique username; password stored as BCrypt hash |
| doctor | id, name, specialty, email, password, phone | unique email; validated name, specialty, phone |
| doctor_available_times | doctor_id, available_times | foreign key to doctor; hourly slot strings |
| patient | id, name, email, password, phone, address | unique email; validated required fields |
| appointment | id, doctor_id, patient_id, appointment_time, status | required foreign keys; status 0 scheduled, 1 completed, 2 cancelled |

Doctor 1:N Appointment; Patient 1:N Appointment. Booking locks the doctor row within a transaction and checks active overlapping appointments, preventing two requests from booking the same slot. Authentication determines patient identity; the request cannot choose another patient.

MongoDB collection prescriptions stores id, appointmentId, patientId, doctorId, medication, dosage, duration, notes. The backend derives patient/doctor IDs from an authorized appointment rather than trusting the submitted IDs. Reading a prescription requires ownership as the patient or assigned doctor.

SQL/procedures.sql contains daily, monthly, and yearly reports. The monthly/yearly procedures count distinct patients and exclude cancelled appointments. All demonstration records are synthetic.
