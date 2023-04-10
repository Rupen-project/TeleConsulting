package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.Prescription;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;

import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;

import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PrescriptionDTO;
import com.had.teleconsulting.teleconsulting.Repository.AppointmentRepo;
import com.had.teleconsulting.teleconsulting.Repository.DoctorRepo;
import com.had.teleconsulting.teleconsulting.Repository.PrescriptionRepo;
import com.had.teleconsulting.teleconsulting.Services.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorImpl implements DoctorService {

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PrescriptionRepo prescriptionRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {

        DoctorDetails doctorDetails=new ModelMapper().map(doctorDTO,DoctorDetails.class);
        DoctorDetails savedDoctor=this.doctorRepo.save(doctorDetails);
        return new ModelMapper().map(savedDoctor,DoctorDTO.class);
    }

    @Override
    public PrescriptionDTO createPrescription(Map<String, Object> prescDetails){
        Prescription prescription = new Prescription();
        prescription.setEPrescription((String) prescDetails.get("ePrescription"));
        Prescription savedPrescription = this.prescriptionRepo.save(prescription);
        int aID = (int) prescDetails.get("appointmentID");
        Long apptID = Long.valueOf(aID);
        Optional<Appointment> appt = this.appointmentRepo.findById(apptID);
        if(appt!=null){
            appt.get().setPrescription(savedPrescription);
            this.appointmentRepo.save(appt.get());
        }
        return new ModelMapper().map(savedPrescription,PrescriptionDTO.class);
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
        return new ModelMapper().map(doctorDetails, DoctorDTO.class);
    }

    @Override
    public DoctorDTO registerDoctor(DoctorDTO doctorDTO) {
        DoctorDetails doctorDetails = new ModelMapper().map(doctorDTO, DoctorDetails.class);
        doctorDetails.setDoctorEmail(doctorDTO.getDoctorEmail());
        doctorDetails.setDoctorPassword(new BCryptPasswordEncoder().encode(doctorDetails.getDoctorPassword()));
        doctorRepo.save(doctorDetails);
        return new ModelMapper().map(doctorDetails, DoctorDTO.class);
    }

    @Override
    public List<AppointmentDTO> getDoctorsAppointments(Long doctorID) {
        Optional<DoctorDetails> doctor = this.doctorRepo.findById(doctorID);
        List<AppointmentDTO> appointments = this.appointmentRepo.findAllBydoctorDetailsOrderByAppointmentDateDesc(doctor.get()).stream().map(ele ->
                new ModelMapper().map(ele,AppointmentDTO.class)).collect(Collectors.toList());
        List<AppointmentDTO> firstFiftyAppointments = appointments.stream().limit(50).collect(Collectors.toList());
        return firstFiftyAppointments;
    }

}
