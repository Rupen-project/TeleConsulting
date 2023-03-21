package com.had.teleconsulting.teleconsulting.Services;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundExeption;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;

import java.util.ArrayList;
import java.util.List;

public interface PatientService {

    PatientDTO createPatient(PatientDTO patient);
    Boolean getPatientByMobileNumber(String patientMobileNumber);
    PatientDTO updatePatient(PatientDTO patient, Integer patientID);
    PatientDTO getPatientByID(Long patientID) throws PatientNotFoundExeption;
    List<PatientDTO> getAllPatient();
    void deletePatient(Integer patientID);

    ArrayList<String> getAvailableSpecialisationsfromAvailableDoctors();

    List<DoctorDTO> getAvailableDoctorsBySpecialisation(String category) throws DoctorNotFoundException;
}