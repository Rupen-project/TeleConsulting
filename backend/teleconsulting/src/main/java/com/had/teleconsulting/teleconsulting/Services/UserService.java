package com.had.teleconsulting.teleconsulting.Services;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Exception.ResouseNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;


public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO loginUser(LoginModel loginModel) throws ResouseNotFoundException;
}
