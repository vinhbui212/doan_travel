package doan.vinhbui.service.impl;


import doan.vinhbui.config.JwtService;
import doan.vinhbui.dto.AuthenticationResponse;
import doan.vinhbui.model.Admin;
import doan.vinhbui.model.Customer;
import doan.vinhbui.repository.AdminRepository;
import doan.vinhbui.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWT;

@Service
@RequiredArgsConstructor
public class GmailAuthService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    public AuthenticationResponse googleRegister(String token) {
        DecodedJWT jwt = JWT.decode(token);
        //extract information from the token
        String email = jwt.getClaim("email").asString();
        String firstName = jwt.getClaim("given_name").asString();
        String lastName = jwt.getClaim("family_name").asString();

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);

        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            if(!customer.getIsGmail()){
                return AuthenticationResponse.builder().token("Already Exists").build();
            }
            var jwtToken = jwtService.generateToken(customer);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }else if (optionalAdmin.isPresent()){
            Admin admin = optionalAdmin.get();
            if(!admin.getIsGmail()){
                return AuthenticationResponse.builder().token("Already Exists").build();
            }
            var jwtToken = jwtService.generateToken(admin);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }else{
            Customer customer = new Customer(email, null, true, true, firstName, lastName);
            customerRepository.save(customer);
            var jwtToken = jwtService.generateToken(customer);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }
}
