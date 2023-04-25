package com.had.teleconsulting.teleconsulting.Services;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Exception.ResouseNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;

import java.util.List;


public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO loginUser(LoginModel loginModel) throws ResouseNotFoundException;

    List<UserDTO> getInitialUserData(List<UserDTO> userDTOS);
}
