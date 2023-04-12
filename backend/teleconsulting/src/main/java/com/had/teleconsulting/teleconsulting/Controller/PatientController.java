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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @PostMapping("/getAllPatientOfGivenUserId/{userId}")
    public ResponseEntity<List<PatientDTO>> getAllPatientOfGivenUserId(@PathVariable("userId")  Long userId){
        return ResponseEntity.ok(this.patientService.getAllPatientOfGivenUserId(userId));
    }

    @PostMapping("/getAppointmentHistory/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentHistory(@PathVariable("patientId") Long patientId){
        return ResponseEntity.ok(this.patientService.getAppointmentHistory(patientId));
    }

    @PostMapping("/healthRecord")
    public ResponseEntity<String> uploadHealthRecord(@RequestParam(value = "file") MultipartFile file) throws IOException {
        System.out.println("Inside Controller of uploadHR");
        String uploadRecord = patientService.uploadHealthRecords(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadRecord);
    }
    @PostMapping("/createAppointment")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody Map<String, Object> json)
    {
        return ResponseEntity.ok(this.patientService.createAppointment(json));
    }

    //this controller will delete entry from queue and update doctors queue size and
    //show correct queue size in real time
    @PostMapping("/onCallDisconnect")
    public ResponseEntity<AppointmentDTO> onCallDisconnect(@RequestBody AppointmentDTO appointmentDTO){
        return ResponseEntity.ok(this.patientService.onCallDisconnect(appointmentDTO));
    }

    @Autowired
    SimpMessagingTemplate template;
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody int appointmentID) {
        template.convertAndSend("/topic/message", appointmentID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload int appointmentID) {
        // receive message from client
    }


    @SendTo("/topic/message")
    public int broadcastMessage(@Payload int appointmentID) {
        return appointmentID;
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
