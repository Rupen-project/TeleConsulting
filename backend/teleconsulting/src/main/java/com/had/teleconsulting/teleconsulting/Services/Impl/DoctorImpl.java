package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Repository.DoctorRepo;
import com.had.teleconsulting.teleconsulting.Repository.PatientRepo;
import com.had.teleconsulting.teleconsulting.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorImpl implements DoctorService {

    @Autowired
    private DoctorRepo doctorRepo;

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {

        DoctorDetails doctorDetails=this.dtoToDoctor(doctorDTO);
        DoctorDetails savedDoctor=this.doctorRepo.save(doctorDetails);
        return this.doctorToDto(savedDoctor);
    }



    public DoctorDetails dtoToDoctor(DoctorDTO doctorDTO){
        DoctorDetails doctorDetails=new DoctorDetails();
        doctorDetails.setDoctorID(doctorDTO.getDoctorID());
        doctorDetails.setDoctorPassword(doctorDTO.getDoctorPassword());
        doctorDetails.setDoctorSpecialisation(doctorDTO.getDoctorSpecialisation());
        doctorDetails.setDoctorFirstName(doctorDTO.getDoctorFirstName());
        doctorDetails.setDoctorLastName(doctorDTO.getDoctorLastName());

        return doctorDetails;
    }

    public DoctorDTO doctorToDto(DoctorDetails doctorDetails){
        DoctorDTO doctorDTO=new DoctorDTO();
        doctorDTO.setDoctorID(doctorDetails.getDoctorID());
        doctorDTO.setDoctorPassword(doctorDetails.getDoctorPassword());
        doctorDTO.setDoctorSpecialisation(doctorDetails.getDoctorSpecialisation());
        doctorDTO.setDoctorFirstName(doctorDetails.getDoctorFirstName());
        doctorDTO.setDoctorLastName(doctorDetails.getDoctorLastName());

        return doctorDTO;
    }
}
