package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.PatientDetails;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Repository.AppointmentRepo;
import com.had.teleconsulting.teleconsulting.Repository.DoctorRepo;
import com.had.teleconsulting.teleconsulting.Repository.PatientRepo;
import com.had.teleconsulting.teleconsulting.Repository.UserRepo;
import com.had.teleconsulting.teleconsulting.Services.PatientService;
import org.apache.catalina.Store;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientImpl implements PatientService {

    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {

            PatientDetails patientDetails = new ModelMapper().map(patientDTO, PatientDetails.class);
            Optional<User> user = userRepo.findById(patientDTO.getUser().getUserID());
            patientDetails.setUser(user.get());
            PatientDetails savedPatient=this.patientRepo.save(patientDetails);
        return new ModelMapper().map(savedPatient,PatientDTO.class);
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patient, Integer patientID) {
        return null;
    }

    @Override
    public PatientDTO getPatientByID(Long patientID) throws PatientNotFoundException {
        Optional<PatientDetails> patientDetails=this.patientRepo.findById(patientID);


        if(!patientDetails.isPresent()){
            throw new PatientNotFoundException("No patient available with provided patientID");
        }

        return new ModelMapper().map(patientDetails.get(),PatientDTO.class);
    }


    @Override
    public Boolean getPatientByMobileNumber(String patientMobileNumber) {
        List<PatientDetails> patientByMobileNumber = this.patientRepo.findAllByPatientMobileNumber(patientMobileNumber);
        if(patientByMobileNumber.size()==0) return false;
        else return true;
    }


    @Override
    public ArrayList<String> getSpecialisation() {
        ArrayList<String> AvailableSpecialisations=this.patientRepo.findAvailableSpecialisations();
        return AvailableSpecialisations;
    }

    @Override
    public List<PatientDTO> getAllPatient() {
        List<PatientDetails> patients = this.patientRepo.findAll();
        List<PatientDTO> patientDTOs = patients.stream().map(patientDetails -> new ModelMapper().map(patientDetails,PatientDTO.class)).collect(Collectors.toList());
        return patientDTOs;
    }

    @Override
    public List<DoctorDTO> getAvailableDoctorsBySpecialisation(String category) throws DoctorNotFoundException {
        List<DoctorDetails> doctors=this.doctorRepo.findAllByDoctorSpecialisationAndDoctorAvailable(category,1);

        // if no doctor available with specified specialisation then this error will throw
        if(doctors.size()==0) {
            throw new DoctorNotFoundException("Doctor is not available with this Specialisation Please try after some time");
        }

        List<DoctorDTO> doctorDtos = doctors.stream().map(doctorDetails -> new ModelMapper().map(doctorDetails,DoctorDTO.class)).collect(Collectors.toList());

        return doctorDtos;
    }
    @Override
    public AppointmentDTO createAppointment(Map<String, Object> json) {
        long millis=System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        if(json!=null){
            Appointment createdAppointment = new Appointment();
            int ptid = (int) json.get("patientDetails");
            Long p = Long.valueOf(ptid);
            int did = (int) json.get("doctorID");
            Long d = Long.valueOf(did);
            Optional<PatientDetails> pt = patientRepo.findById(p);
            Optional<DoctorDetails> dt = doctorRepo.findById(d);
            createdAppointment.setPatientDetails(pt.get());
            createdAppointment.setDoctorDetails(dt.get());
            createdAppointment.setAppointmentOpdType((String) json.get("appointmentOpdType"));
            createdAppointment.setAppointmentDate(date);
            Appointment savedAppointment = this.appointmentRepo.save(createdAppointment);
            return new ModelMapper().map(savedAppointment,AppointmentDTO.class);
        }
        return null;

    }

}
