package com.had.teleconsulting.teleconsulting.Services;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;


public interface DoctorService {
        DoctorDTO createDoctor(DoctorDTO doctorDTO);

        DoctorDTO loginDoctor(LoginModel loginModel) throws DoctorNotFoundException;
}
