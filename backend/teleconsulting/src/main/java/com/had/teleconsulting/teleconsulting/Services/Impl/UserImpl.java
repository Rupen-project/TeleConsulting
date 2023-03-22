package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Exception.ResouseNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
import com.had.teleconsulting.teleconsulting.Repository.UserRepo;
import com.had.teleconsulting.teleconsulting.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserImpl implements UserService {



    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user=new ModelMapper().map(userDTO,User.class);
        User savedUser=this.userRepo.save(user);
        return new ModelMapper().map(savedUser,UserDTO.class);
    }

    @Override
    public UserDTO loginUser(LoginModel loginModel) throws ResouseNotFoundException {
        String userEmail = loginModel.getEmail();
        String userPassword = loginModel.getPassword();

        User users = this.userRepo.findByUserEmail(userEmail);

        if(users==null) throw new ResouseNotFoundException("User Not Found");
         else{
             if(users.getUserPassword().equals(userPassword)) {
                return new ModelMapper().map(users, UserDTO.class);

            } else {
                 throw  new ResouseNotFoundException("Invalid Credentials");
            }
        }

    }

}
