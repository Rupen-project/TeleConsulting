package com.had.teleconsulting.teleconsulting.Repository;

import com.had.teleconsulting.teleconsulting.Bean.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepo extends JpaRepository<Prescription,Long> {
}
