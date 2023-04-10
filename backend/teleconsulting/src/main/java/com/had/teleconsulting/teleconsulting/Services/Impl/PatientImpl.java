package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.HealthRecord;
import com.had.teleconsulting.teleconsulting.Bean.PatientDetails;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Repository.AppointmentRepo;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PatientImpl implements PatientService {

    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;
    private PatientDTO reusePatientDTO;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

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
        List<PatientDTO> patientDTOs = patients.stream().map(patientDetails -> new ModelMapper().map(patientDetails, PatientDTO.class)).collect(Collectors.toList());
        return patientDTOs;
    }


    @Override
    public String uploadHealthRecords(MultipartFile record) throws IOException {
        System.out.println("Inside Implementation of uploadHR");
        File fileObject = convertMultiPartFileToFile(record);
        String fileName = record.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName,fileName,fileObject));
        fileObject.delete();
        HealthRecord healthRecord = new HealthRecord();
        //call reusePatientDTO to get the patient ID but currently using dummy data for testing
        healthRecord.setHealthRecordID((long)5);
        healthRecord.setHealthRecordURL("s3://"+bucketName+"/"+fileName);
        System.out.println("Returning "+"s3://"+bucketName+"/"+fileName);
        return "File Uploaded successfully: "+fileName;
    }

    @Override
    @Transactional
    public byte[] getPrescription(String patientHealthRecordName) throws IOException {
        S3Object s3Object = amazonS3.getObject(bucketName,patientHealthRecordName);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
        return content;
    }

    @Override
    @Transactional
    public String deleteHealthRecord(String healthRecordName) {
        amazonS3.deleteObject(bucketName,healthRecordName);
        return healthRecordName + " has been removed successfully!";
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch(IOException e) {
            log.error("Error converting multipart file to file",e);
        }
        return convertedFile;
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
