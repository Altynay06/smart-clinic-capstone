# User stories and acceptance criteria

## Admin
As an administrator, I want to log in so that only authorized staff can manage doctors.
- Valid credentials return a signed role-bound token; invalid credentials return 401.
- Patient and doctor tokens cannot add doctors.

As an administrator, I want to add doctors with specialties and availability so patients can find appropriate appointments.
- Name, specialty, email, phone, password and slots are validated.
- Duplicate emails return a conflict; passwords are never returned in JSON.

## Doctor
As a doctor, I want to view my appointments so I can prepare for scheduled consultations.
- Only appointments assigned to the authenticated doctor are returned.
- A patient token cannot access the doctor schedule.

As a doctor, I want to record a prescription for an assigned appointment so care instructions remain associated with the right patient.
- Required medication, dosage and duration are validated.
- Another doctor's appointment is rejected; patient identity comes from the appointment.

## Patient
As a patient, I want to search doctors by name, specialty and time so I can find an appropriate consultation.
- Filters combine and search is case-insensitive.
- Empty results show a clear message.

As a patient, I want to book an available future slot so I can plan a consultation.
- Booking uses the authenticated patient identity.
- Unavailable or already-booked slots return 409.

As a patient, I want to view and cancel my scheduled appointments so I can manage my visits.
- Only my appointments are returned; another patient's cancellation is forbidden.
- Cancelling an already cancelled/completed appointment returns a conflict.
