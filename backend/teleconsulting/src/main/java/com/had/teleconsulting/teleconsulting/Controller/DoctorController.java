package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PrescriptionDTO;
import com.had.teleconsulting.teleconsulting.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/getDoctorsAppointments/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getDoctorsAppointments(@PathVariable("doctorId") Long doctorID){
        List<AppointmentDTO> appointments = this.doctorService.getDoctorsAppointments(doctorID);
        return ResponseEntity.ok(appointments);
    }
    @GetMapping("/doctorLogin")
    public ResponseEntity<DoctorDTO> loginUser(@RequestBody LoginModel loginModel) throws DoctorNotFoundException {
        DoctorDTO doctorDTO=this.doctorService.loginDoctor(loginModel);

        if(doctorDTO==null){
            return new ResponseEntity<>(null, HttpStatus.valueOf(401));
        }else{
            return new ResponseEntity<>(doctorDTO, HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/generatePrescription")
    public ResponseEntity<PrescriptionDTO> createPrescription(@RequestBody Map<String, Object> prescDetails){
        return ResponseEntity.ok(this.doctorService.createPrescription(prescDetails));
    }
}
