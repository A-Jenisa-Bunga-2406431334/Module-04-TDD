package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerFunctionalTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PaymentService paymentService;

    List<Payment> payments;
    Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        payments = new ArrayList<>();
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1",
                PaymentMethod.VOUCHER.getValue(), order, paymentData);
        payments.add(payment);
    }

    @Test
    void testGetPaymentDetailForm() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"));
    }

    @Test
    void testGetPaymentDetailById() throws Exception {
        Payment payment = payments.get(0);
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testGetAdminList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testGetAdminDetail() throws Exception {
        Payment payment = payments.get(0);
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPostSetStatus() throws Exception {
        Payment payment = payments.get(0);
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);
        when(paymentService.setStatus(any(Payment.class), anyString()))
                .thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/" + payment.getId())
                .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"));
    }
}