package com.had.teleconsulting.teleconsulting.Repository;

import com.had.teleconsulting.teleconsulting.Bean.PatientDetails;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;

import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PatientRepo extends JpaRepository<PatientDetails,Long> {

    public List<PatientDetails> findAllByPatientMobileNumber(String patientMobileNumber);

    @Query(nativeQuery = true,value = "SELECT DISTINCT doctor_specialisation FROM doctor_details")
    ArrayList<String> findAvailableSpecialisations();


}
