package com.had.teleconsulting.teleconsulting.Repository;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<DoctorDetails,Integer> {
//    @Query(nativeQuery = true,value = "SELECT * FROM doctor_details WHERE doctor_available = 1 and doctor_specialisation = :category ")
    public List<DoctorDetails> findAllByDoctorSpecialisationAndDoctorAvailable(String category,int available);
}
