package com.had.teleconsulting.teleconsulting.Services;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PrescriptionDTO;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;

import java.util.List;
import java.util.Map;

import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO);

    DoctorDTO loginDoctor(LoginModel loginModel) throws DoctorNotFoundException;

    DoctorDTO registerDoctor(DoctorDTO doctorDTO);

    PrescriptionDTO createPrescription(Map<String, Object> prescDetails);

    List<AppointmentDTO> getDoctorsAppointments(Long doctorID);

}
