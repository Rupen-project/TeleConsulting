package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Bean.HealthRecord;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.UnAuthorisedAccess;
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
@CrossOrigin(originPatterns = "*",allowedHeaders = "*", exposedHeaders = "*")
@RequestMapping("/api/patientDetails")
public class PatientController {

    // @Autowired will bring the object that already present in spring container and
    //    now on-words we do not have to make object of this with new keyword this is
    //    called property base dependency injection
    @Autowired
    private PatientService patientService;

    // @Request body will add will convert the coming request body to the required object
    @PostMapping("/registerPatient")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO,@RequestAttribute String role) throws UnAuthorisedAccess {

        if(role.equals("USR")){
            PatientDTO createPatientDTO=this.patientService.createPatient(patientDTO);
            return new ResponseEntity<>(createPatientDTO, HttpStatus.CREATED);
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }


    @GetMapping("/getPatientById/{id}")
    public ResponseEntity<PatientDTO> getPatientDetailsByID(@PathVariable("id") Long patientID,@RequestAttribute String role) throws PatientNotFoundException, UnAuthorisedAccess {
        if(role.equals("USR")){
            return ResponseEntity.ok(this.patientService.getPatientByID(patientID));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }

    @GetMapping("/getSpecialisation")
    public ResponseEntity<ArrayList<String>> getSpecialisation(@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("USR")){
            return ResponseEntity.ok(this.patientService.getSpecialisation());
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }
    @PostMapping("/AvailableDoctorsBySpecialisation")
    public ResponseEntity<List<DoctorDTO>> getAvailableDoctorsBySpecialisation(@RequestBody Map<String ,String> c,@RequestAttribute String role) throws DoctorNotFoundException, UnAuthorisedAccess {
        if(role.equals("USR")){
            return ResponseEntity.ok(this.patientService.getAvailableDoctorsBySpecialisation(c.get("category")));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }

    @PostMapping("/getAllPatientOfGivenUserId/{userId}")
    public ResponseEntity<List<PatientDTO>> getAllPatientOfGivenUserId(@PathVariable("userId")  Long userId,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("USR")){
            return ResponseEntity.ok(this.patientService.getAllPatientOfGivenUserId(userId));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }

    @PostMapping("/getAppointmentHistory/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentHistory(@PathVariable("patientId") Long patientId,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("USR")){
            return ResponseEntity.ok(this.patientService.getAppointmentHistory(patientId));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }

    @PostMapping("/healthRecord/{patientID}")
    public ResponseEntity<String> uploadHealthRecord(@PathVariable Long patientID, @RequestParam(value = "file") MultipartFile file,@RequestAttribute String role) throws IOException, UnAuthorisedAccess {
        if(role.equals("USR")){
            System.out.println("Inside Controller of uploadHR");
            String uploadRecord = patientService.uploadHealthRecords(file,patientID);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(uploadRecord);
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }
    @PostMapping("/createAppointment")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody Map<String, Object> json,@RequestAttribute String role) throws DoctorNotFoundException, UnAuthorisedAccess {
        if(role.equals("USR")){
            return ResponseEntity.ok(this.patientService.createAppointment(json));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }

    //this controller will delete entry from queue and update doctors queue size and
    //show correct queue size in real time


    @Autowired
    SimpMessagingTemplate template;
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody int appointmentID) throws UnAuthorisedAccess {

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



    @GetMapping("/prescription/{patientID}/{prescriptionDate}/{appointmentID}")
    public ResponseEntity<ByteArrayResource> getPrescription(@PathVariable String prescriptionDate, @PathVariable Long patientID,@PathVariable Long appointmentID,@RequestAttribute String role) throws IOException, UnAuthorisedAccess {
        if(role.equals("USR")){
            byte[] downloadPrescription = patientService.getPrescription(prescriptionDate, patientID,appointmentID);
            ByteArrayResource byteArrayResource = new ByteArrayResource(downloadPrescription);
            String prescriptionName = "Prescription-" + prescriptionDate +".pdf";
            return ResponseEntity.ok()
                    .contentLength(downloadPrescription.length)
                    .header("Content-type","application/octet-stream")
                    .header("Content-disposition","attachment; filename=\"" +prescriptionName+"\"")
                    .body(byteArrayResource);
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }

    @GetMapping("/prescription/{patientID}/{appointmentID}")
    public ResponseEntity<ByteArrayResource> downloadPrescription(@PathVariable Long patientID, @PathVariable Long appointmentID,@RequestAttribute String role) throws IOException, ParseException, UnAuthorisedAccess {
        if(role.equals("USR")){
            byte[] downloadPrescription = patientService.downloadPrescription(patientID,appointmentID);
            if (downloadPrescription == null) {
                System.out.println("downloadPrescription is null");

                return null;
            }
            ByteArrayResource byteArrayResource = new ByteArrayResource(downloadPrescription);
            String prescriptionName = "Prescription-" + patientID +".pdf";
            return ResponseEntity.ok()
                    .contentLength(downloadPrescription.length)
                    .header("Content-type","application/octet-stream")
                    .header("Content-disposition","attachment; filename=\"" +prescriptionName+"\"")
                    .body(byteArrayResource);
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }
    @PostMapping("/getFollowUps/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getFollowUps(@PathVariable("patientId") Long patientId,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("USR")){
            return ResponseEntity.ok(this.patientService.getFollowUps(patientId));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }
    @PostMapping("/makeFollowupFalse/{appointmentId}")
    public ResponseEntity<?> makeFollowupFalse(@PathVariable("appointmentId") Long appointmentId,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("USR")){
            this.patientService.makeFollowupFalse(appointmentId);
            return ResponseEntity.ok(HttpStatus.OK);
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }
    @PostMapping("/getAppointmentById/{appointmentId}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable("appointmentId") Long appointmentId,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("USR")){
            return ResponseEntity.ok(patientService.getAppointmentById(appointmentId));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }
    }

    @PostMapping("/initialData")
    public ResponseEntity<?> initialPatientData(@RequestBody List<PatientDTO> patientDTOS){

        List<PatientDTO> patientData = patientService.getInitialPatientData(patientDTOS);
        return ResponseEntity.ok(patientData);
    }

    @PostMapping("/getDoctorById/{doctorId}")
    public ResponseEntity<?> getDoctorById(@PathVariable("doctorId") Long doctorId,@RequestAttribute String role) throws DoctorNotFoundException, UnAuthorisedAccess {
        if(role.equals("USR")){
            return ResponseEntity.ok(patientService.getDoctorById(doctorId));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }
    }

}
