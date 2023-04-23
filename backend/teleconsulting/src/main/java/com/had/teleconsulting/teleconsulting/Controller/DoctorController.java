package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Config.JwtService;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;

import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PrescriptionDTO;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
import com.had.teleconsulting.teleconsulting.Services.DoctorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.Prescription;
import com.had.teleconsulting.teleconsulting.Payloads.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(originPatterns = "*", exposedHeaders = "*" , allowedHeaders = "*")
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/getDoctorsAppointments/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getDoctorsAppointments(@PathVariable("doctorId") Long doctorID){
        List<AppointmentDTO> appointments = this.doctorService.getDoctorsAppointments(doctorID);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctorLogin")
    public ResponseEntity<DoctorDTO> loginUser(@RequestBody LoginModel loginModel, HttpServletResponse response) throws DoctorNotFoundException {
        DoctorDTO doctorDTO=this.doctorService.loginDoctor(loginModel);
        String authToken = null;
        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setDoctorEmail("DOC#"+ loginModel.getEmail());
        authToken = jwtService.generateToken(doctorDetails);
        response.setHeader("token", authToken);
        return new ResponseEntity<>(doctorDTO, HttpStatus.ACCEPTED);

    }

    @PostMapping("/registerDoctor")
    public ResponseEntity<DoctorDTO> createPatient(@RequestBody DoctorDTO doctorDTO){

        DoctorDTO createDoctorDTO=this.doctorService.registerDoctor(doctorDTO);
        return new ResponseEntity<>(createDoctorDTO, HttpStatus.CREATED);
    }

    @PostMapping("/generatePrescription")
    public ResponseEntity<PrescriptionDTO> createPrescription(@RequestBody Map<String, Object> prescDetails){
        return ResponseEntity.ok(this.doctorService.createPrescription(prescDetails));
    }

    @GetMapping("/edit")
    public ResponseEntity<String> edit(@RequestAttribute String role) {
//       ("token", authToken);

        return new ResponseEntity<>(role, HttpStatus.ACCEPTED);
    }

    @PostMapping("/uploadPrescription/{appointmentID}/{patientID}")
    public ResponseEntity<String> generatePdfReport(@RequestBody PrescriptionAppointmentRequestDTO request,@PathVariable Long patientID, @PathVariable Long appointmentID) throws IOException {
        System.out.println(request.toString());
        Prescription prescription = request.getPrescription();
        Appointment appointment = request.getAppointment();
        System.out.println(prescription);
        System.out.println(appointment);
        String uploadPrescription = this.doctorService.uploadPrescription(prescription,patientID,appointmentID,appointment);
        System.out.println("Outside Controller");
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadPrescription);
    }
}
