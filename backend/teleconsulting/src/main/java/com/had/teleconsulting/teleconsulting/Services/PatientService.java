package com.had.teleconsulting.teleconsulting.Services;

import com.had.teleconsulting.teleconsulting.Bean.HealthRecord;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PatientService {

    PatientDTO createPatient(PatientDTO patient);
    PatientDTO getPatientByID(Long patientID) throws PatientNotFoundException;
    ArrayList<String> getSpecialisation();

    AppointmentDTO createAppointment(Map<String, Object> json) throws DoctorNotFoundException;

    List<DoctorDTO> getAvailableDoctorsBySpecialisation(String category) throws DoctorNotFoundException;



    AppointmentDTO onCallDisconnect(AppointmentDTO appointmentDTO);

    List<PatientDTO> getAllPatientOfGivenUserId(Long userId);

    List<AppointmentDTO> getAppointmentHistory(Long patientId);

    List<HealthRecord> getHealthRecordsByPatientId(Long patientID) throws PatientNotFoundException;

    String uploadHealthRecords(MultipartFile record, Long patientID) throws IOException;

    byte[] getHealthRecord(String patientHealthRecordName, Long patientID) throws IOException;

    //String deleteHealthRecord(String healthRecordName);


    byte[] getPrescription(String prescriptionDate, Long patientID, Long appointmentID) throws IOException;

    byte[] downloadPrescription(Long patientID, Long appointmentID) throws ParseException, IOException;

    List<AppointmentDTO> getFollowUps(Long patientId);

    void makeFollowupFalse(Long appointmentId);

    AppointmentDTO getAppointmentById(Long appointmentId);

    List<PatientDTO> getInitialPatientData(List<PatientDTO> patientDTOS);
}