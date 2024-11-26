package doan.vinhbui.controller;


import doan.vinhbui.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@org.springframework.stereotype.Controller
@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:3000/")

public class VNPAYController {
    @Autowired
    private VNPayService vnPayService;



    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("id") long orderId,
                            HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderId, baseUrl);

        // Redirect the user to the VNPAY payment gateway
        return vnpayUrl;
    }


    @GetMapping("/vnpay-payment")
    public String vnpayPayment(HttpServletRequest request,Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "vnpay/ordersuccess" : "vnpay/orderfail";
    }
}
