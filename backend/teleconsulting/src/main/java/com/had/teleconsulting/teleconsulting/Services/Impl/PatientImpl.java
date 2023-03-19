package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.PatientDetails;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundExeption;
import com.had.teleconsulting.teleconsulting.Exception.ResourceNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Repository.DoctorRepo;
import com.had.teleconsulting.teleconsulting.Repository.PatientRepo;
import com.had.teleconsulting.teleconsulting.Services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientImpl implements PatientService {

    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorRepo doctorRepo;

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
    public PatientDTO getPatientByID(Integer patientID) throws PatientNotFoundExeption {
        Optional<PatientDetails> patientDetails=this.patientRepo.findById(patientID);

        if(!patientDetails.isPresent()){
                throw new PatientNotFoundExeption("No patient available with provided patientID");
        }

        return this.patientToDto(patientDetails.get());
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

    @Override
    public ArrayList<String> getAvailableSpecialisationsfromAvailableDoctors() {
        ArrayList<String> AvailableSpecialisationsfromAvailableDoctors=this.patientRepo.findAvailableSpecialisationsfromAvailableDoctors();
        return AvailableSpecialisationsfromAvailableDoctors;
    }

    @Override
    public List<PatientDTO> getAllPatient() {
        List<PatientDetails> patients = this.patientRepo.findAll();
        List<PatientDTO> patientDTOs = patients.stream().map(patientDetails -> this.patientToDto(patientDetails)).collect(Collectors.toList());
        return patientDTOs;
    }

    @Override
    public List<DoctorDTO> getAvailableDoctorsBySpecialisation(String category) throws DoctorNotFoundException {
        List<DoctorDetails> doctors=this.doctorRepo.findAllByDoctorSpecialisationAndDoctorAvailable(category,1);

        // if no doctor available with specified specialisation then this error will throw
        if(doctors.size()==0) {
            throw new DoctorNotFoundException("Doctor is not available with this Specialisation Please try after some time");
        }

        List<DoctorDTO> doctorDtos = doctors.stream().map(doctorDetails -> this.doctorToDto(doctorDetails)).collect(Collectors.toList());

        return doctorDtos;
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

    public DoctorDTO doctorToDto(DoctorDetails doctorDetails){
        DoctorDTO doctorDTO=new DoctorDTO();
        doctorDTO.setDoctorID(doctorDetails.getDoctorID());
        doctorDTO.setDoctorSpecialisation(doctorDetails.getDoctorSpecialisation());
        doctorDTO.setDoctorPassword(doctorDetails.getDoctorPassword());
        doctorDTO.setDoctorFirstName(doctorDetails.getDoctorFirstName());
        doctorDTO.setDoctorLastName(doctorDetails.getDoctorLastName());
        doctorDTO.setDoctorAvailable(doctorDetails.getDoctorAvailable());
        doctorDTO.setDoctorEmail(doctorDetails.getDoctorEmail());
        doctorDTO.setDoctorMobileNumber(doctorDetails.getDoctorMobileNumber());
        doctorDTO.setDoctorQueueSize(doctorDetails.getDoctorQueueSize());
        return doctorDTO;

    }

    public DoctorDetails DtoTodoctor(DoctorDTO doctorDTO){
        DoctorDetails doctorDetails=new DoctorDetails();
        doctorDetails.setDoctorID(doctorDTO.getDoctorID());
        doctorDetails.setDoctorAvailable(doctorDTO.getDoctorAvailable());
        doctorDetails.setDoctorEmail(doctorDTO.getDoctorEmail());
        doctorDetails.setDoctorPassword(doctorDTO.getDoctorPassword());
        doctorDetails.setDoctorSpecialisation(doctorDTO.getDoctorSpecialisation());
        doctorDetails.setDoctorLastName(doctorDTO.getDoctorLastName());
        doctorDetails.setDoctorFirstName(doctorDTO.getDoctorFirstName());
        doctorDetails.setDoctorMobileNumber(doctorDTO.getDoctorMobileNumber());
        doctorDetails.setDoctorQueueSize(doctorDTO.getDoctorQueueSize());

        return doctorDetails;

    }
}
