package com.had.teleconsulting.teleconsulting.Repository;

import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
    List<Appointment> findAllBydoctorDetailsOrderByAppointmentDateDesc(DoctorDetails doctorID);
}
