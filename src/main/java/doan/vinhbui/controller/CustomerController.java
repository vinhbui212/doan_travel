package doan.vinhbui.controller;

import doan.vinhbui.dto.CustomerDTO;
import doan.vinhbui.service.impl.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PutMapping("/update-info")
    public ResponseEntity<CustomerDTO> updateCustomerInfo(@RequestParam String email, @RequestBody CustomerDTO updatedInfo) {
        CustomerDTO updatedCustomer = customerService.updateCustomerInfo(email, updatedInfo);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        String response = customerService.updatePassword(email, oldPassword, newPassword);
        return ResponseEntity.ok(response);
    }
}

