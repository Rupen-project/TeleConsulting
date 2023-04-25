package com.had.teleconsulting.teleconsulting.Services.Impl;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Exception.ResouseNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
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

import java.util.List;
import java.util.stream.Collectors;


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

    @Override
    public List<UserDTO> getInitialUserData(List<UserDTO> userDTOS) {
        List<User> users=userDTOS.stream().map(userDTO -> new ModelMapper().map(userDTO,User.class)).collect(Collectors.toList());
        try {
            for(int i=0;i<users.size();i++){
                users.get(i).setUserPassword(new BCryptPasswordEncoder().encode(users.get(i).getUserPassword()));
                giveEncryptDecrypt.encryptUser(users.get(i));
                userRepo.save(users.get(i));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<UserDTO> userDTOSaved= users.stream().map(user -> new ModelMapper().map(user,UserDTO.class)).collect(Collectors.toList());
        return userDTOSaved;
    }

}
