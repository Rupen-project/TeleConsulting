package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.PatientDetails;
import com.had.teleconsulting.teleconsulting.Exception.ResourceNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Repository.PatientRepo;
import com.had.teleconsulting.teleconsulting.Services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientImpl implements PatientService {

    @Autowired
    private PatientRepo patientRepo;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {

            PatientDetails patientDetails=this.dtoToPatient(patientDTO);

            PatientDetails savedPatient=this.patientRepo.save(patientDetails);
        return this.patientToDto(savedPatient);
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patient, Integer patientID) {
        return null;
    }

    @Override
    public PatientDTO getPatientByID(Integer patientID) {
        PatientDetails patientDetails=this.patientRepo.findById(patientID)
                .orElseThrow(()->new ResourceNotFoundException("Patient","ID",patientID));
        return this.patientToDto(patientDetails);
    }

    @Override
    public List<PatientDTO> getAllPatient() {
        List<PatientDetails> patients = this.patientRepo.findAll();
        List<PatientDTO> patientDTOs = patients.stream().map(patientDetails -> this.patientToDto(patientDetails)).collect(Collectors.toList());
        return patientDTOs;
    }
    @Override
    public Boolean getPatientByMobileNumber(String patientMobileNumber) {
        List<PatientDetails> patientByMobileNumber = this.patientRepo.findAllByPatientMobileNumber(patientMobileNumber);
        if(patientByMobileNumber.size()==0) return false;
        else return true;
    }


    @Override
    public void deletePatient(Integer patientID) {

    }

    public PatientDetails dtoToPatient(PatientDTO patientDto){
        PatientDetails patientDetails=new PatientDetails();
        patientDetails.setPatientPassword(patientDto.getPatientPassword());
        patientDetails.setPatientID(patientDto.getPatientID());
        patientDetails.setPatientDOB(patientDto.getPatientDOB());
        patientDetails.setPatientEmail(patientDto.getPatientEmail());
        patientDetails.setPatientGender(patientDto.getPatientGender());
        patientDetails.setPatientFirstName(patientDto.getPatientFirstName());
        patientDetails.setPatientLastName(patientDto.getPatientLastName());
        patientDetails.setPatientMobileNumber(patientDto.getPatientMobileNumber());

        return patientDetails;
    }

    public PatientDTO patientToDto(PatientDetails patientDetails){
        PatientDTO patientDTO=new PatientDTO();
        patientDTO.setPatientID(patientDetails.getPatientID());
        patientDTO.setPatientPassword(patientDetails.getPatientPassword());
        patientDTO.setPatientDOB(patientDetails.getPatientDOB());
        patientDTO.setPatientEmail(patientDetails.getPatientEmail());
        patientDTO.setPatientGender(patientDetails.getPatientGender());
        patientDTO.setPatientFirstName(patientDetails.getPatientFirstName());
        patientDTO.setPatientLastName(patientDetails.getPatientLastName());
        patientDTO.setPatientMobileNumber(patientDetails.getPatientMobileNumber());
        return patientDTO;
    }
}
