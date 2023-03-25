package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Exception.ResouseNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
import com.had.teleconsulting.teleconsulting.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class    UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){

        UserDTO createUserDTO=this.userService.createUser(userDTO);
        return new ResponseEntity<>(createUserDTO, HttpStatus.CREATED);
    }

    @GetMapping("/userLogin")
    public ResponseEntity<UserDTO> loginUser(@RequestBody LoginModel loginModel) throws ResouseNotFoundException {
        UserDTO userDTO=this.userService.loginUser(loginModel);
        System.out.println(userDTO);
        if(userDTO==null){
            return new ResponseEntity<>(null, HttpStatus.valueOf(401));
        }else{
            return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
        }

    }
}
