package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Exception.ResouseNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
import com.had.teleconsulting.teleconsulting.Repository.UserRepo;
import com.had.teleconsulting.teleconsulting.Services.UserService;
import com.had.teleconsulting.teleconsulting.Services.Util.EncryptDecrypt;
import com.had.teleconsulting.teleconsulting.Services.Util.giveEncryptDecrypt;
import org.modelmapper.ModelMapper;
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
        try {
            user.setUserEmail(userDTO.getUserEmail());
            user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));
            giveEncryptDecrypt.encryptUser(user);
            userRepo.save(user);
            giveEncryptDecrypt.decryptUser(user);
        } catch (Exception e) {
            System.out.println("exception = " + e);
            throw new RuntimeException(e);
        }

        return new ModelMapper().map(user, UserDTO.class);
    }


    @Override
    public UserDTO loginUser(LoginModel loginModel) throws ResouseNotFoundException {
        String userEmail ;
        try {
            userEmail = EncryptDecrypt.encrypt(loginModel.getEmail(), giveEncryptDecrypt.SECRET_KEY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String userPassword = loginModel.getPassword();


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userEmail, userPassword
                )
        );

        User users;
        try {
            users = this.userRepo.findByUserEmail(userEmail);
            giveEncryptDecrypt.decryptUser(users);
        } catch (Exception e) {
            System.out.println("exception = " + e);
            throw new RuntimeException(e);
        }
        return new ModelMapper().map(users, UserDTO.class);

    }

}
