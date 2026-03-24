package com.ShareText.demo.services.AuthService;


import com.ShareText.demo.dto.RegisterUserRequest;
import com.ShareText.demo.dto.UserLoginRequest;
import com.ShareText.demo.models.User;
import com.ShareText.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String register(RegisterUserRequest request){
        try{

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("User already exists");
            }
            User user = new User();
            user.setName(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            this.userRepository.save(user);
            return  "User registered successfully";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User login(UserLoginRequest request){
        User user = this.userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("User Not Found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid Credentials");
        }
        return user;
    }
}
