package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Config.JwtService;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.ResouseNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
import com.had.teleconsulting.teleconsulting.Services.DoctorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private JwtService jwtService;
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

    @GetMapping("/edit")
    public ResponseEntity<String> edit(@RequestAttribute String role) {
//       ("token", authToken);

        return new ResponseEntity<>(role, HttpStatus.ACCEPTED);
    }

}
