package com.had.teleconsulting.teleconsulting.Controller;

import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.User;
import com.had.teleconsulting.teleconsulting.Config.JwtService;
import com.had.teleconsulting.teleconsulting.Exception.ResouseNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
import com.had.teleconsulting.teleconsulting.Services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/registerUser")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){

        UserDTO createUserDTO=this.userService.createUser(userDTO);
        return new ResponseEntity<>(createUserDTO, HttpStatus.CREATED);
    }

    @GetMapping("/userLogin")
    public ResponseEntity<UserDTO> loginUser(@RequestBody LoginModel loginModel, HttpServletResponse response) throws ResouseNotFoundException {
        UserDTO userDTO=this.userService.loginUser(loginModel);

        String authToken = null;
        User user = new User();
        user.setUserEmail("USR#"+ loginModel.getEmail());
        authToken = jwtService.generateToken(user);
        response.setHeader("token", authToken);
        return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
    }

    @GetMapping("/edit")
    public ResponseEntity<String> edit(@RequestAttribute String role) throws ResouseNotFoundException {
//       ("token", authToken);

        return new ResponseEntity<>(role, HttpStatus.ACCEPTED);
    }
}