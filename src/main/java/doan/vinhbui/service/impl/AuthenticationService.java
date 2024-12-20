package doan.vinhbui.service.impl;


import doan.vinhbui.config.JwtService;
import doan.vinhbui.dto.AuthenticationResponse;
import doan.vinhbui.dto.LoginRequest;
import doan.vinhbui.dto.RegisterRequest;
import doan.vinhbui.model.Admin;
import doan.vinhbui.model.Customer;
import doan.vinhbui.repository.AdminRepository;
import doan.vinhbui.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private Logger logger= Logger.getLogger(String.valueOf(AuthenticationService.class));
//    private final EmailService emailService;
    /**
     * This method is used to send verification email to the customer
     */
//    private void setupVerification(Customer customer) throws Exception {
//        var jwtToken = jwtService.generateToken(customer);
//        try {
//            emailService.sendEmail(customer.getEmail(), "Email Verification",
//                    "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 20px;\">\n" +
//                            "\n" +
//                            "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">\n" +
//                            "\n" +
//                            "        <h2 style=\"color: #333333;\">Email Verification</h2>\n" +
//                            "\n" +
//                            "        <p style=\"color: #666666;\">Please click on the button below to verify your account:</p>\n" +
//                            "\n" +
//                            "        <a href=\"http://localhost:3000/verification?token=" + jwtToken + "\"style=\"display: inline-block; background-color: #4caf50; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-top: 20px;\">Verify</a>\n" +
//                            "\n" +
//                            "    </div>\n" +
//                            "</body>");
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }

    public AuthenticationResponse customerRegister(RegisterRequest request) {
        try {
            Optional<Customer> customerCheck = customerRepository.findByEmail(request.getEmail());
            Optional<Admin> adminCheck = adminRepository.findByEmail(request.getEmail());

            if (adminCheck.isPresent()) {
                return AuthenticationResponse.builder().token("Already Exist").build();
            }
            String jwtToken = null;
            if (customerCheck.isPresent()) {
                Customer customer1 = customerCheck.get();
                if (customer1.getIsVerified() || customer1.getIsGmail()) {
                    return AuthenticationResponse.builder().token("Already Exist").build();
                }
                customer1.setFirstName(request.getFirstName());
                customer1.setLastName(request.getLastName());
                customer1.setPassword(passwordEncoder.encode(request.getPassword()));
                customerRepository.save(customer1);
//                setupVerification(customer1);
            } else {
                Customer customer = new Customer(request.getEmail(), passwordEncoder.encode(request.getPassword()), false
                        , false, request.getFirstName(), request.getLastName());
                customerRepository.save(customer);
//                setupVerification(customer);
                jwtToken = jwtService.generateToken(customer);
            }
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return AuthenticationResponse.builder().token(e.getMessage()).build();
        }
    }


    public AuthenticationResponse authenticate(LoginRequest request) throws NoSuchElementException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            return AuthenticationResponse.builder().token("Unauthorized").build();
        }

        Optional<Customer> customer = customerRepository.findByEmail(request.getEmail());
        Optional<Admin> admin = adminRepository.findByEmail(request.getEmail());
        if (customer.isPresent() && customer.get().getIsVerified() && !customer.get().getIsGmail()) {
            var jwtToken = jwtService.generateToken(customer.get());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else if (admin.isPresent() && admin.get().getIsVerified() && !admin.get().getIsGmail()) {
            var jwtToken = jwtService.generateToken(admin.get());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            return AuthenticationResponse.builder()
                    .token("Unauthorized")
                    .build();
        }
    }
}