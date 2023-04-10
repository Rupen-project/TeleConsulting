package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Services.PatientService;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/patientDetails")
public class PatientController {

    // @Autowired will bring the object that already present in spring container and
    //    now on-words we do not have to make object of this with new keyword this is
    //    called property base dependency injection
    @Autowired
    private PatientService patientService;

    // @Request body will add will convert the coming request body to the required object
    @PostMapping("/registerPatient")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO){

        PatientDTO createPatientDTO=this.patientService.createPatient(patientDTO);
        return new ResponseEntity<>(createPatientDTO, HttpStatus.CREATED);
    }


    @GetMapping("/verifyMobileNumber/{patientMobileNumber}")
    public ResponseEntity<Boolean> getPatientByMobileNumber(@PathVariable String patientMobileNumber){
        return ResponseEntity.ok(this.patientService.getPatientByMobileNumber(patientMobileNumber));
    }

    @GetMapping("/allPatient")
    public ResponseEntity<List<PatientDTO>> getAllPatient(){
        return ResponseEntity.ok(this.patientService.getAllPatient());
    }

    @GetMapping("/getPatientById/{id}")
    public ResponseEntity<PatientDTO> getPatientDetailsByID(@PathVariable("id") Long patientID) throws PatientNotFoundException {
        return ResponseEntity.ok(this.patientService.getPatientByID(patientID));
    }

    @GetMapping("/getSpecialisation")
    public ResponseEntity<ArrayList<String>> getSpecialisation() {
        return ResponseEntity.ok(this.patientService.getSpecialisation());
    }

    @PostMapping("/AvailableDoctorsBySpecialisation")
    public ResponseEntity<List<DoctorDTO>> getAvailableDoctorsBySpecialisation(@RequestBody String category) throws DoctorNotFoundException {
        return ResponseEntity.ok(this.patientService.getAvailableDoctorsBySpecialisation(category));
    }

    @PostMapping("/createAppointment")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody Map<String, Object> json)
    {
        return ResponseEntity.ok(this.patientService.createAppointment(json));
    }

    @PostMapping("/healthRecord")
    public ResponseEntity<String> uploadHealthRecord(@RequestParam(value = "file") MultipartFile file) throws IOException {
        System.out.println("Inside Controller of uploadHR");
        String uploadRecord = patientService.uploadHealthRecords(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadRecord);
    }

    @GetMapping("/prescription/{patientHealthRecordName}")
    public ResponseEntity<ByteArrayResource> getPrescription(@PathVariable String patientHealthRecordName) throws IOException {
        byte[] downloadRecord = patientService.getPrescription(patientHealthRecordName);
        ByteArrayResource byteArrayResource = new ByteArrayResource(downloadRecord);
        return ResponseEntity.ok()
                .contentLength(downloadRecord.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\"" +patientHealthRecordName+"\"")
                .body(byteArrayResource);
    }

    @DeleteMapping("/deleting/{patientHealthRecordName}")
    public ResponseEntity<String> deleteHealthRecord(@PathVariable String patientHealthRecordName){
        System.out.println("Delete Controller");
        return new ResponseEntity<>(patientService.deleteHealthRecord(patientHealthRecordName),HttpStatus.OK);
    }
}
