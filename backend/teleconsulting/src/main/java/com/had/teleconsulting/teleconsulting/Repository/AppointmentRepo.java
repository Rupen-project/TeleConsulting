package com.had.teleconsulting.teleconsulting.Repository;

import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
}
