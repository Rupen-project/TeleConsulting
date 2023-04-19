package com.had.teleconsulting.teleconsulting.Repository;

import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
    List<Appointment> findAllBydoctorDetailsOrderByAppointmentDateDesc(DoctorDetails doctorID);
    List<Appointment> findAllByPatientDetails_PatientID(Long patientId);

    @Query("select a.doctorDetails.doctorFirstName FROM Appointment a WHERE a.appointmentID = :appointmentId")
    String findDoctorNameByAppointmentID(@Param("appointmentId") Long appointmentID);

    @Query("select a.patientDetails.patientFirstName || ' ' || a.patientDetails.patientLastName FROM Appointment a WHERE a.appointmentID = :appointmentId")
    String findPatientNameByAppointmentID(@Param("appointmentId") Long appointmentID);

    Appointment findAppointmentByAppointmentID(Long appointmentID);


    @Query("SELECT a FROM Appointment a WHERE a.patientDetails.patientID = :patientID ORDER BY a.appointmentDate DESC")
    List<Appointment> findAppointmentByPatientID(Long patientID);

}
