package com.had.teleconsulting.teleconsulting.Repository;

import com.had.teleconsulting.teleconsulting.Bean.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HealthRecordRepo extends JpaRepository<HealthRecord,Long> {
    @Query("SELECT new com.had.teleconsulting.teleconsulting.Bean.HealthRecord(hr.healthRecordName, hr.healthRecordUploadDate) AS healthRecordUploadDate FROM HealthRecord hr WHERE hr.patientDetails.patientID = :patientID")
    List<HealthRecord> findAllByPatientID(@Param("patientID") Long patientID);

//repo only used to get something from database not for post

}
