package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundExeption;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @PostMapping("/create")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO){

        PatientDTO createPatientDTO=this.patientService.createPatient(patientDTO);
        return new ResponseEntity<>(createPatientDTO, HttpStatus.CREATED);
    }

    @GetMapping("/mobileNumber/{patientMobileNumber}")
    public ResponseEntity<Boolean> getPatientByMobileNumber(@PathVariable String patientMobileNumber){
        return ResponseEntity.ok(this.patientService.getPatientByMobileNumber(patientMobileNumber));
    }

    @GetMapping("/allPatient")
    public ResponseEntity<List<PatientDTO>> getAllPatient(){
        return ResponseEntity.ok(this.patientService.getAllPatient());
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<PatientDTO> getPatientDetailsByID(@PathVariable("id") Long patientID) throws PatientNotFoundExeption {
        return ResponseEntity.ok(this.patientService.getPatientByID(patientID));
    }

    @GetMapping("/AvailableSpecialisationsOfAvailableDoctors")
    public ResponseEntity<ArrayList<String>> getAvailableSpecialisationsOfAvailableDoctors(){
        return ResponseEntity.ok(this.patientService.getAvailableSpecialisationsfromAvailableDoctors());
    }

    @GetMapping("/AvailableDoctorsBySpecialisation")
    public ResponseEntity<List<DoctorDTO>> getAvailableDoctorsBySpecialisation(@RequestParam String category) throws DoctorNotFoundException {
        return ResponseEntity.ok(this.patientService.getAvailableDoctorsBySpecialisation(category));
    }



}
