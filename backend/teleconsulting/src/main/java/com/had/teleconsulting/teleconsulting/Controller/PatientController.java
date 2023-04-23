package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Bean.HealthRecord;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Services.PatientService;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(originPatterns = "*",allowedHeaders = "*")
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


    @GetMapping("/getPatientById/{id}")
    public ResponseEntity<PatientDTO> getPatientDetailsByID(@PathVariable("id") Long patientID) throws PatientNotFoundException {
        return ResponseEntity.ok(this.patientService.getPatientByID(patientID));
    }

    @GetMapping("/getSpecialisation")
    public ResponseEntity<ArrayList<String>> getSpecialisation() {
        return ResponseEntity.ok(this.patientService.getSpecialisation());
    }
    @PostMapping("/AvailableDoctorsBySpecialisation")
    public ResponseEntity<List<DoctorDTO>> getAvailableDoctorsBySpecialisation(@RequestBody Map<String ,String> c) throws DoctorNotFoundException {

        return ResponseEntity.ok(this.patientService.getAvailableDoctorsBySpecialisation(c.get("category")));
    }

    @PostMapping("/getAllPatientOfGivenUserId/{userId}")
    public ResponseEntity<List<PatientDTO>> getAllPatientOfGivenUserId(@PathVariable("userId")  Long userId){
        return ResponseEntity.ok(this.patientService.getAllPatientOfGivenUserId(userId));
    }

    @PostMapping("/getAppointmentHistory/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentHistory(@PathVariable("patientId") Long patientId){
        return ResponseEntity.ok(this.patientService.getAppointmentHistory(patientId));
    }

    @PostMapping("/healthRecord/{patientID}")
    public ResponseEntity<String> uploadHealthRecord(@PathVariable Long patientID, @RequestParam(value = "file") MultipartFile file) throws IOException {
        System.out.println("Inside Controller of uploadHR");
        String uploadRecord = patientService.uploadHealthRecords(file,patientID);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadRecord);
    }
    @PostMapping("/createAppointment")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody Map<String, Object> json) throws DoctorNotFoundException {
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

    @GetMapping("/healthrecord/{patientID}/{patientHealthRecordName}")
    public ResponseEntity<ByteArrayResource> getHealthRecord(@PathVariable String patientHealthRecordName, @PathVariable Long patientID) throws IOException {
        System.out.println("Inside healthrecord abhi");
        byte[] downloadRecord = patientService.getHealthRecord(patientHealthRecordName, patientID);
        ByteArrayResource byteArrayResource = new ByteArrayResource(downloadRecord);
        System.out.println("Sending back: "+patientHealthRecordName);
        return ResponseEntity.ok()
                .contentLength(downloadRecord.length)
                .header("Content-type","application/pdf")
                .header("Content-disposition","inline; filename=\"" +patientHealthRecordName+"\"")
                .body(byteArrayResource);
    }

    @GetMapping("/prescription/{patientID}/{prescriptionDate}")
    public ResponseEntity<ByteArrayResource> getPrescription(@PathVariable String prescriptionDate, @PathVariable Long patientID) throws IOException {
        byte[] downloadPrescription = patientService.getPrescription(prescriptionDate, patientID);
        ByteArrayResource byteArrayResource = new ByteArrayResource(downloadPrescription);
        String prescriptionName = "Prescription-" + prescriptionDate +".pdf";
        return ResponseEntity.ok()
                .contentLength(downloadPrescription.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\"" +prescriptionName+"\"")
                .body(byteArrayResource);
    }

//    @DeleteMapping("/deleting/{patientHealthRecordName}")
//    public ResponseEntity<String> deleteHealthRecord(@PathVariable String patientHealthRecordName){
//        System.out.println("Delete Controller");
//        return new ResponseEntity<>(patientService.deleteHealthRecord(patientHealthRecordName),HttpStatus.OK);
//    }

    @GetMapping("/getHealthRecordsByPatientId/{patientID}")
    public ResponseEntity<List<HealthRecord>> getHealthRecordsByPatientId(@PathVariable("patientID") Long patientID) throws PatientNotFoundException {
        return ResponseEntity.ok(this.patientService.getHealthRecordsByPatientId(patientID));
    }

    @GetMapping("/prescription/{patientID}")
    public ResponseEntity<ByteArrayResource> downloadPrescription(@PathVariable Long patientID) throws IOException, ParseException {
        byte[] downloadPrescription = patientService.downloadPrescription(patientID);
        ByteArrayResource byteArrayResource = new ByteArrayResource(downloadPrescription);
        String prescriptionName = "Prescription-" + patientID +".pdf";
        return ResponseEntity.ok()
                .contentLength(downloadPrescription.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\"" +prescriptionName+"\"")
                .body(byteArrayResource);
    }
}
