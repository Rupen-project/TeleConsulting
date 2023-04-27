package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Bean.*;
import com.had.teleconsulting.teleconsulting.Config.JwtService;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.UnAuthorisedAccess;
import com.had.teleconsulting.teleconsulting.Payloads.*;
import com.had.teleconsulting.teleconsulting.Services.DoctorService;
import com.had.teleconsulting.teleconsulting.Services.PatientService;
import com.had.teleconsulting.teleconsulting.Services.Util.EncryptDecrypt;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(originPatterns = "*", exposedHeaders = "*",allowedHeaders = "*")
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/getDoctorsAppointments/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getDoctorsAppointments(@PathVariable("doctorId") Long doctorID,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("DOC")){
            List<AppointmentDTO> appointments = this.doctorService.getDoctorsAppointments(doctorID);
        return ResponseEntity.ok(appointments);
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }
    }

    @PostMapping ("/doctorLogin")
    public ResponseEntity<DoctorDTO> loginDoctor(@RequestBody LoginModel loginModel, HttpServletResponse response) throws DoctorNotFoundException {
        DoctorDTO doctorDTO=this.doctorService.loginDoctor(loginModel);
        String authToken = null;
        DoctorDetails doctorDetails = new DoctorDetails();
        try {
            doctorDetails.setDoctorEmail("DOC#"+ EncryptDecrypt.encrypt(loginModel.getEmail(),"8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        authToken = jwtService.generateToken(doctorDetails);
        response.setHeader("token", authToken);
        return new ResponseEntity<>(doctorDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping("/registerDoctor")
    public ResponseEntity<DoctorDTO> registerDoctor(@RequestBody DoctorDTO doctorDTO){

        DoctorDTO createDoctorDTO=this.doctorService.registerDoctor(doctorDTO);
        return new ResponseEntity<>(createDoctorDTO, HttpStatus.CREATED);
    }

    @PostMapping("/generatePrescription")
    public ResponseEntity<PrescriptionDTO> createPrescription(@RequestBody Map<String, Object> prescDetails,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("DOC")){
            return ResponseEntity.ok(this.doctorService.createPrescription(prescDetails));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }
    }

    @GetMapping("/edit")
    public ResponseEntity<String> edit(@RequestAttribute String role) {
//       ("token", authToken);

        return new ResponseEntity<>(role, HttpStatus.ACCEPTED);
    }

    @PostMapping("/uploadPrescription/{appointmentID}/{patientID}")
    public ResponseEntity<String> generatePdfReport(@RequestBody PrescriptionAppointmentRequestDTO request,@PathVariable Long patientID, @PathVariable Long appointmentID,@RequestAttribute String role) throws IOException, UnAuthorisedAccess {
        if(role.equals("DOC")){
            System.out.println(request.toString());
            Prescription prescription = request.getPrescription();
            Appointment appointment = request.getAppointment();
            System.out.println(prescription);
            System.out.println(appointment);
            String uploadPrescription = this.doctorService.uploadPrescription(prescription,patientID,appointmentID,appointment);
            System.out.println("Outside Controller");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(uploadPrescription);
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }
    @PostMapping("/onLogout")
    public ResponseEntity<?> logoutDoctor(@RequestBody Map<String,Long> d,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("DOC")){
            this.doctorService.logoutDoctor(d.get("doctorId"));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }

    @GetMapping("/getPatientById/{id}")
    public ResponseEntity<PatientDTO> getPatientDetailsByID(@PathVariable("id") Long patientID,@RequestAttribute String role) throws PatientNotFoundException, UnAuthorisedAccess {
        if(role.equals("DOC")){
            return ResponseEntity.ok(this.doctorService.getPatientByID(patientID));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }

    @PostMapping("/TodaysAppointments")
    public ResponseEntity<?> getTodaysAppointments(@RequestBody Map<String,Long> m,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("DOC")){
            return ResponseEntity.ok(doctorService.getTodaysAppointments(m.get("doctorId")));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }
    }

    @PostMapping("/onCallDisconnect")
    public ResponseEntity<AppointmentDTO> onCallDisconnect(@RequestBody AppointmentDTO appointmentDTO,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("DOC")){
            return ResponseEntity.ok(patientService.onCallDisconnect(appointmentDTO));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }


    }

    @GetMapping("/healthrecord/{patientID}/{patientHealthRecordName}")
    public ResponseEntity<ByteArrayResource> getHealthRecord(@PathVariable String patientHealthRecordName, @PathVariable Long patientID,@RequestAttribute String role) throws IOException, UnAuthorisedAccess {
        if(role.equals("DOC")){
            System.out.println("Inside healthrecord abhi");
            byte[] downloadRecord = patientService.getHealthRecord(patientHealthRecordName, patientID);
            ByteArrayResource byteArrayResource = new ByteArrayResource(downloadRecord);
            System.out.println("Sending back: "+patientHealthRecordName);
            return ResponseEntity.ok()
                    .contentLength(downloadRecord.length)
                    .header("Content-type","application/octet-stream")
                    .header("Content-disposition","inline; filename=\"" +patientHealthRecordName+"\"")
                    .body(byteArrayResource);
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }



    }


    @GetMapping("/getHealthRecordsByPatientId/{patientID}")
    public ResponseEntity<List<HealthRecord>> getHealthRecordsByPatientId(@PathVariable("patientID") Long patientID,@RequestAttribute String role) throws PatientNotFoundException, UnAuthorisedAccess {
        if(role.equals("DOC")){
            return ResponseEntity.ok(this.patientService.getHealthRecordsByPatientId(patientID));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }

    }

    @PostMapping("/getAppointmentById/{appointmentId}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable("appointmentId") Long appointmentId,@RequestAttribute String role) throws UnAuthorisedAccess {
        if(role.equals("DOC")){
            return ResponseEntity.ok(patientService.getAppointmentById(appointmentId));
        }else{
            throw new UnAuthorisedAccess("You are not Authorised to access this");
        }
    }
    @PostMapping("/initialData")
    public ResponseEntity<?> initialDoctorData(@RequestBody List<DoctorDTO> doctorDTOS){
        List<DoctorDTO> doctorDTOS1 = doctorService.getInitialDoctorData(doctorDTOS);
        return ResponseEntity.ok(doctorDTOS1);
    }
}
