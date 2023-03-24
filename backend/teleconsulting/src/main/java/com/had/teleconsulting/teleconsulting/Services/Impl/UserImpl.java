package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Exception.ResouseNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
import com.had.teleconsulting.teleconsulting.Repository.UserRepo;
import com.had.teleconsulting.teleconsulting.Services.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.matcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserImpl implements UserService {



    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new ModelMapper().map(userDTO, User.class);
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));
        userRepo.save(user);
        return new ModelMapper().map(user, UserDTO.class);
    }

    @Override
    public UserDTO loginUser(LoginModel loginModel) throws ResouseNotFoundException {
        String userEmail = loginModel.getEmail();
        String userPassword = loginModel.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userEmail, userPassword
                )
        );

        User users = this.userRepo.findByUserEmail(userEmail);
        return new ModelMapper().map(users, UserDTO.class);

    }

}
