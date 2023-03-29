package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
import com.had.teleconsulting.teleconsulting.Repository.DoctorRepo;
import com.had.teleconsulting.teleconsulting.Services.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorImpl implements DoctorService {

    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {

        DoctorDetails doctorDetails=new ModelMapper().map(doctorDTO,DoctorDetails.class);
        DoctorDetails savedDoctor=this.doctorRepo.save(doctorDetails);
        return new ModelMapper().map(savedDoctor,DoctorDTO.class);
    }

    @Override
    public DoctorDTO loginDoctor(LoginModel loginModel) throws DoctorNotFoundException {
        String doctorEmail = loginModel.getEmail();
        String doctorPassword = loginModel.getPassword();


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        doctorEmail, doctorPassword
                )
        );


        DoctorDetails doctorDetails = this.doctorRepo.findByDoctorEmail(doctorEmail);
        if(doctorDetails==null) throw new DoctorNotFoundException("Doctor not Found");

        else{
            if(doctorDetails.getDoctorPassword().equals(doctorPassword)) {
                return new ModelMapper().map(doctorDetails, DoctorDTO.class);
            }else {
                // password not correct
                throw  new DoctorNotFoundException("Invalid Credentials");
            }
        }
    }

    @Override
    public DoctorDTO registerDoctor(DoctorDTO doctorDTO) {
        DoctorDetails doctorDetails = new ModelMapper().map(doctorDTO, DoctorDetails.class);
        doctorDetails.setDoctorEmail(doctorDTO.getDoctorEmail());
        doctorDetails.setDoctorPassword(new BCryptPasswordEncoder().encode(doctorDetails.getDoctorPassword()));
        doctorRepo.save(doctorDetails);
        return new ModelMapper().map(doctorDetails, DoctorDTO.class);
    }


//    public DoctorDetails dtoToDoctor(DoctorDTO doctorDTO){
//        DoctorDetails doctorDetails=new DoctorDetails();
//        doctorDetails.setDoctorID(doctorDTO.getDoctorID());
//        doctorDetails.setDoctorPassword(doctorDTO.getDoctorPassword());
//        doctorDetails.setDoctorSpecialisation(doctorDTO.getDoctorSpecialisation());
//        doctorDetails.setDoctorFirstName(doctorDTO.getDoctorFirstName());
//        doctorDetails.setDoctorLastName(doctorDTO.getDoctorLastName());
//
//        return doctorDetails;
//    }

//    public DoctorDTO doctorToDto(DoctorDetails doctorDetails){
//        DoctorDTO doctorDTO=new DoctorDTO();
//        doctorDTO.setDoctorID(doctorDetails.getDoctorID());
//        doctorDTO.setDoctorPassword(doctorDetails.getDoctorPassword());
//        doctorDTO.setDoctorSpecialisation(doctorDetails.getDoctorSpecialisation());
//        doctorDTO.setDoctorFirstName(doctorDetails.getDoctorFirstName());
//        doctorDTO.setDoctorLastName(doctorDetails.getDoctorLastName());
//
//        return doctorDTO;
//    }
}
