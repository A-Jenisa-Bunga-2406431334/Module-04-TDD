package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
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

@WebMvcTest(OrderController.class)
class OrderControllerFunctionalTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    PaymentService paymentService;

    List<Order> orders;
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
        orders = new ArrayList<>();
        orders.add(order);
    }

    @Test
    void testGetCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"));
    }

    @Test
    void testGetHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"));
    }

    @Test
    void testPostHistory() throws Exception {
        when(orderService.findAllByAuthor("Safira Sudrajat")).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                .param("author", "Safira Sudrajat"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void testGetPayPage() throws Exception {
        when(orderService.findById(order.getId())).thenReturn(order);

        mockMvc.perform(get("/order/pay/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPay"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPostPayVoucher() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1",
                PaymentMethod.VOUCHER.getValue(), order, paymentData);

        when(orderService.findById(order.getId())).thenReturn(order);
        when(paymentService.addPayment(any(), anyString(), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/" + order.getId())
                .param("method", "VOUCHER")
                .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPayResult"))
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(model().attributeExists("status"));
    }

    @Test
    void testPostPayBankTransfer() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF123");
        Payment payment = new Payment("payment-2",
                PaymentMethod.BANK_TRANSFER.getValue(), order, paymentData);

        when(orderService.findById(order.getId())).thenReturn(order);
        when(paymentService.addPayment(any(), anyString(), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/" + order.getId())
                .param("method", "BANK_TRANSFER")
                .param("bankName", "BCA")
                .param("referenceCode", "REF123"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPayResult"))
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(model().attributeExists("status"));
    }
}