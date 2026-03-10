package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    private List<Product> products;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("test-product-id");
        product.setProductName("Test Product");
        product.setProductQuantity(10);

        products = new ArrayList<>();
        products.add(product);

        order = new Order();
    }

    // Unhappy: Test to create the order with empty Product
    @Test
    void testCreateOrderWithEmptyProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            order.setProducts(new ArrayList<>());
        });
    }

    // Unhappy: Test to create the order with no status defined
    @Test
    void testCreateOrderWithNoStatusDefined() {
        order.setProducts(products);
        order.setAuthor("Test Author");
        order.setStatus(null);
        
        assertNull(order.getStatus());
    }

    // Happy: Test to create the order status of "SUCCESS"
    @Test
    void testCreateOrderWithSuccessStatus() {
        order.setProducts(products);
        order.setAuthor("Test Author");
        order.setStatus("SUCCESS");

        assertEquals("SUCCESS", order.getStatus());
        assertEquals("Test Author", order.getAuthor());
        assertNotNull(order.getId());
        assertNotNull(order.getOrderTime());
    }

    // Unhappy: Test to create the order with invalid status
    @Test
    void testCreateOrderWithInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            order.setStatus("INVALID_STATUS");
        });
    }

    // Happy: Test to edit the order with one of correct status
    @Test
    void testEditOrderWithCorrectStatus() {
        order.setProducts(products);
        order.setAuthor("Test Author");
        order.setStatus("WAITING_PAYMENT");

        assertEquals("WAITING_PAYMENT", order.getStatus());

        order.setStatus("SUCCESS");
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testGetOrderId() {
        assertNotNull(order.getId());
    }

    @Test
    void testGetProducts() {
        order.setProducts(products);
        assertEquals(1, order.getProducts().size());
        assertEquals("Test Product", order.getProducts().get(0).getProductName());
    }

    @Test
    void testGetOrderTime() {
        assertNotNull(order.getOrderTime());
    }

    @Test
    void testGetAuthor() {
        order.setAuthor("John Doe");
        assertEquals("John Doe", order.getAuthor());
    }
}