package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.had.teleconsulting.teleconsulting.Bean.Queue;
import com.had.teleconsulting.teleconsulting.Bean.*;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Repository.*;
import com.had.teleconsulting.teleconsulting.Services.PatientService;
import com.had.teleconsulting.teleconsulting.Services.Util.EncryptDecrypt;
import com.had.teleconsulting.teleconsulting.Services.Util.giveEncryptDecrypt;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatientImpl implements PatientService {

    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private HealthRecordRepo healthRecordRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PrescriptionRepo prescriptionRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;
    private PatientDTO reusePatientDTO;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private QueueRepo queuerepo;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {

        PatientDetails patientDetails = new ModelMapper().map(patientDTO, PatientDetails.class);
        try {
            User user = userRepo.findById(patientDTO.getUser().getUserID()).get();

            patientDetails.setUser(user);

            giveEncryptDecrypt.encryptPatient(patientDetails);
            this.patientRepo.save(patientDetails);

//            giveEncryptDecrypt.decryptUser(user);
            giveEncryptDecrypt.decryptPatient(patientDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ModelMapper().map(patientDetails,PatientDTO.class);
    }

    @Override
    public PatientDTO getPatientByID(Long patientID) throws PatientNotFoundException {
        Optional<PatientDetails> patientDetails=this.patientRepo.findById(patientID);


        if(!patientDetails.isPresent()){
            throw new PatientNotFoundException("No patient available with provided patientID");
        }

        PatientDetails patientDetails1;
        try {
            patientDetails1=patientDetails.get();

            giveEncryptDecrypt.decryptPatient(patientDetails1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ModelMapper().map(patientDetails1,PatientDTO.class);
    }



    @Override
    public ArrayList<String> getSpecialisation() {
        ArrayList<String> AvailableSpecialisations=this.patientRepo.findAvailableSpecialisations();
        for (int i = 0; i < AvailableSpecialisations.size(); i++){
            String tmp = AvailableSpecialisations.get(i);
            AvailableSpecialisations.remove(i);
            try {
                tmp=EncryptDecrypt.decrypt(tmp,giveEncryptDecrypt.SECRET_KEY);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            AvailableSpecialisations.add(tmp);
        }
        return AvailableSpecialisations;
    }


    @Override
    public String uploadHealthRecords(MultipartFile record, Long patientID) throws IOException {
        System.out.println("Inside Implementation of uploadHR");
        //Long patientID = reusePatientDTO.getPatientID();
        File fileObject = convertMultiPartFileToFile(record);
        //hardcoding the patientID for testing as it will depend on the api call of getPatientByID
        String folderName = "HealthRecord/" + patientID;
        String fileName = record.getOriginalFilename();
        String keyName = folderName + "/" + fileName;
        amazonS3.putObject(new PutObjectRequest(bucketName,keyName,fileObject));
        fileObject.delete();
        //call reusePatientDTO to get the patient ID but currently using dummy data for testing
        HealthRecord healthRecord = new HealthRecord();
//        healthRecord.setHealthRecordID((long)count);
//        count++;
        healthRecord.setHealthRecordName(fileName);
        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));
        PatientDetails patientDetails = new PatientDetails();
        patientDetails.setPatientID(patientID);
        healthRecord.setHealthRecordUploadDate(localDateString);
        healthRecord.setPatientDetails(patientDetails);
        healthRecordRepo.save(healthRecord);
        System.out.println("Returning "+"s3://"+keyName);
        return "File Uploaded successfully: "+keyName;
    }

    @Override
    @Transactional
    public byte[] getHealthRecord(String patientHealthRecordName, Long patientID) throws IOException {
        String folderName = patientID.toString(); // convert patientID to a string for folder name
        String objectKey = "HealthRecord/" + folderName + "/" + patientHealthRecordName;
        System.out.println(objectKey);
        S3Object s3Object = amazonS3.getObject(bucketName,objectKey);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
        return content;
    }


    @Override
    public byte[] getPrescription(String prescriptionDate, Long patientID) throws IOException {
        String folderName = patientID.toString(); // convert patientID to a string for folder name
        String prescriptionName = "Prescription-" + prescriptionDate +".pdf";
        String objectKey = "Prescription/" + folderName + "/" + prescriptionName;
        S3Object s3Object = amazonS3.getObject(bucketName,objectKey);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
        return content;
    }

    @Override
    public List<HealthRecord> getHealthRecordsByPatientId(Long patientID)  {
        List<HealthRecord> healthRecordList = this.healthRecordRepo.findAllByPatientID(patientID);
        for (HealthRecord healthRecord : healthRecordList) {
            System.out.println(healthRecord);
        }
        System.out.println("Sending the response back");
        return healthRecordList;
    }

    @Override
    public byte[] downloadPrescription(Long patientID) throws ParseException, IOException {
        System.out.println("Inside Implementation");
        List<Appointment> appointment = this.appointmentRepo.findAppointmentByPatientID(patientID);
        Appointment latestAppointment = appointment.get(0);
        Prescription latestPrescription = latestAppointment.getPrescription();
        String originalDate = latestPrescription.getPrescriptionUploadDate();
        DateFormat originalFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        Date date = originalFormat.parse(originalDate);
        String formattedDate = targetFormat.format(date);
        String folderName = patientID.toString(); // convert patientID to a string for folder name
        String prescriptionName = "Prescription-" + formattedDate +".pdf";
        System.out.println(prescriptionName);
        String objectKey = "Prescription/" + folderName + "/" + prescriptionName;
        S3Object s3Object = amazonS3.getObject(bucketName,objectKey);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
        System.out.println("Sending the response back");
        return content;
    }

    //As of now not required API
//    @Override
//    @Transactional
//    public String deleteHealthRecord(String healthRecordName) {
//        amazonS3.deleteObject(bucketName,healthRecordName);
//        return healthRecordName + " has been removed successfully!";
//    }

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
        List<DoctorDTO> doctorDtos;

        try {
            String encryptedCategory = EncryptDecrypt.encrypt(category,giveEncryptDecrypt.SECRET_KEY);
            List<DoctorDetails> doctors=this.doctorRepo.findAllByDoctorSpecialisationAndDoctorAvailable(encryptedCategory,1);

            // if no doctor available with specified specialisation then this error will throw
            if(doctors.size()==0) {
                throw new DoctorNotFoundException("Doctor is not available with this Specialisation Please try after some time");
            }

            List<DoctorDetails> doctors1= doctors.stream().map(doctorDetails -> {
                try {
                    giveEncryptDecrypt.decryptDoctor(doctorDetails);
                    return doctorDetails;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());

            doctorDtos = doctors1.stream().map(doctorDetails -> new ModelMapper().map(doctorDetails,DoctorDTO.class)).collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("exception = " + e);
            throw new RuntimeException(e);
        }

        return doctorDtos;
    }
    @Override
    public AppointmentDTO createAppointment(Map<String, Object> json) throws DoctorNotFoundException {
//        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
//        System.out.println(date);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));
        if(json!=null){
            Appointment createdAppointment = new Appointment();
            Long p = Long.valueOf((int) json.get("patientDetails"));
            Long d = Long.valueOf((int) json.get("doctorID"));
            Optional<PatientDetails> pt = patientRepo.findById(p);
            Optional<DoctorDetails> dt = doctorRepo.findById(d);
            int queueSize = dt.get().getDoctorQueueSize();
            if(queueSize==0 || dt.get().getDoctorAvailable()==0){
                throw new DoctorNotFoundException("Doctor is either not available or currently at maximum capacity. ");
            }
            //updating doctors queue size
            dt.get().setDoctorQueueSize(queueSize-1);
            DoctorDetails updatedDoctorQueue = this.doctorRepo.save(dt.get());
            //Feeding the newly created appointment to queue
            Queue queue = new Queue();
            queue.setDoctorDetails(updatedDoctorQueue);
            Queue savedQueue =  this.queuerepo.save(queue);
            //Finally creating appointment
            createdAppointment.setPatientDetails(pt.get());
            createdAppointment.setDoctorDetails(dt.get());
            createdAppointment.setAppointmentOpdType((String) json.get("appointmentOpdType"));
            createdAppointment.setAppointmentDate(date);
            createdAppointment.setQueue(savedQueue);
            Appointment savedAppointment = this.appointmentRepo.save(createdAppointment);
            try {
                giveEncryptDecrypt.decryptDoctor(savedAppointment.getDoctorDetails());
                giveEncryptDecrypt.decryptPatient(savedAppointment.getPatientDetails());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new ModelMapper().map(savedAppointment,AppointmentDTO.class);
        }
        return null;

    }


    @Override
    public AppointmentDTO onCallDisconnect(AppointmentDTO appointmentDTO){
        DoctorDetails doctor = appointmentDTO.getDoctorDetails();
        Queue queueEntry = appointmentDTO.getQueue();
        appointmentDTO.setQueue(null);
        this.appointmentRepo.save(new ModelMapper().map(appointmentDTO,Appointment.class));
        int q = doctor.getDoctorQueueSize();
        doctor.setDoctorQueueSize(q+1);
        DoctorDetails updatedQueueSizeDoctor = this.doctorRepo.save(doctor);
        System.out.println(updatedQueueSizeDoctor.getDoctorQueueSize());
        this.queuerepo.delete(queueEntry);
        final String uri = "http://localhost:8083/api/patientDetails/send";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(uri,updatedQueueSizeDoctor.getDoctorQueueSize(), Integer.class);
        System.out.println("posting");
        return null;
    }
    @Override
    public List<PatientDTO> getAllPatientOfGivenUserId(Long userId){
        List<PatientDetails> patients = this.patientRepo.findAllByUser_UserID(userId);
        List<PatientDTO> patientDTOs = patients.stream().map(patientDetails -> new ModelMapper().map(patientDetails,PatientDTO.class)).collect(Collectors.toList());
        return patientDTOs;
    }

    @Override
    public List<AppointmentDTO> getAppointmentHistory(Long patientId){
        List<Appointment> appointmentHistory = this.appointmentRepo.findAllByPatientDetails_PatientID(patientId);
        List<AppointmentDTO> appointmentDTOList = appointmentHistory.stream().map(appts -> new ModelMapper().map(appts,AppointmentDTO.class)).collect(Collectors.toList());
        return appointmentDTOList;
    }

}
