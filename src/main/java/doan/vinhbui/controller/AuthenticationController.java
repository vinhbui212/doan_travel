package doan.vinhbui.controller;

import doan.vinhbui.dto.AuthenticationResponse;
import doan.vinhbui.dto.LoginRequest;
import doan.vinhbui.dto.RegisterRequest;
import doan.vinhbui.service.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/registerCustomer")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.customerRegister(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody LoginRequest request
    ) {
        AuthenticationResponse token = service.authenticate(request);
        if (token.getToken().equals("Unauthorized"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(token);
        else
            return ResponseEntity.ok(token);
    }


}