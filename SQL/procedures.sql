USE cms;
DELIMITER $$
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(IN report_date DATE)
BEGIN
 SELECT d.id AS doctor_id,d.name AS doctor_name,COUNT(a.id) AS appointment_count
 FROM doctor d JOIN appointment a ON d.id=a.doctor_id
 WHERE DATE(a.appointment_time)=report_date GROUP BY d.id,d.name ORDER BY d.id;
END$$
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(IN input_month INT,IN input_year INT)
BEGIN
 SELECT d.id AS doctor_id,d.name AS doctor_name,COUNT(DISTINCT a.patient_id) AS patient_count
 FROM doctor d JOIN appointment a ON d.id=a.doctor_id
 WHERE MONTH(a.appointment_time)=input_month AND YEAR(a.appointment_time)=input_year AND a.status<>2
 GROUP BY d.id,d.name ORDER BY patient_count DESC,d.id LIMIT 1;
END$$
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(IN input_year INT)
BEGIN
 SELECT d.id AS doctor_id,d.name AS doctor_name,COUNT(DISTINCT a.patient_id) AS patient_count
 FROM doctor d JOIN appointment a ON d.id=a.doctor_id
 WHERE YEAR(a.appointment_time)=input_year AND a.status<>2
 GROUP BY d.id,d.name ORDER BY patient_count DESC,d.id LIMIT 1;
END$$
DELIMITER ;
