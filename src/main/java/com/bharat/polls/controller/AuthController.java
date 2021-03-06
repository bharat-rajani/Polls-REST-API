package com.bharat.polls.controller;

import com.bharat.polls.exceptions.AppException;
import com.bharat.polls.model.Role;
import com.bharat.polls.model.RoleName;
import com.bharat.polls.model.User;
import com.bharat.polls.payload.ApiResponse;
import com.bharat.polls.payload.JwtAuthenticationResponse;
import com.bharat.polls.payload.LoginRequest;
import com.bharat.polls.payload.SignUpRequest;
import com.bharat.polls.repository.RoleRepository;
import com.bharat.polls.repository.UserRepository;
import com.bharat.polls.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(),loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
        if(userRepository.existsByUsername(signUpRequest.getUsername())){
            return new ResponseEntity(new ApiResponse(false,"Username is already taken."), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity(new ApiResponse(false,"Email is already taken."),HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest.getName(),signUpRequest.getUsername(),signUpRequest.getEmail(),passwordEncoder.encode(signUpRequest.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(()->new AppException("User role is not set"));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        /* <p> URIComponent builder
            It helps to create UriComponent instances by providing fine-grained control over all the aspects of preparing
            a URI including construction, expansion from template variables and encoding.

            Here I am using it to construct a URIComponent instance to perform a redirection to that instance.

            In a single line -> It is used to redirect user to his/her profile page after the login is successful.
        * */
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
