package doan.vinhbui.service.impl;

import doan.vinhbui.dto.CustomerDTO;
import doan.vinhbui.model.Customer;
import doan.vinhbui.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private  BCryptPasswordEncoder passwordEncoder; // Inject BCryptPasswordEncoder

    // Đổi thông tin người dùng
    public CustomerDTO updateCustomerInfo(String email, CustomerDTO updatedInfo) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));

        // Cập nhật thông tin
        customer.setFirstName(updatedInfo.getFirstName());
        customer.setLastName(updatedInfo.getLastName());
        customer.setPhoneNumber(updatedInfo.getPhoneNumber());
        customer.setAddress(updatedInfo.getAddress());

        // Lưu lại thông tin
        customer = customerRepository.save(customer);

        // Chuyển đổi Entity sang DTO
        return mapToDTO(customer);
    }

    // Đổi mật khẩu
    public String updatePassword(String email, String oldPassword, String newPassword) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));

        // Kiểm tra mật khẩu cũ
        if (!customer.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }

        // Cập nhật mật khẩu
        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);

        return "Đổi mật khẩu thành công!";
    }

    // Phương thức chuyển đổi từ Entity sang DTO
    private CustomerDTO mapToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getEmail(),
                null, // Không trả về mật khẩu
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber(),
                customer.getAddress()
        );
    }
}

