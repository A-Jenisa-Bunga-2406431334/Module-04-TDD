package id.ac.ui.cs.advprog.eshop.enums;

public enum PaymentStatus {
    SUCCESS("SUCCESS"),
    REJECTED("REJECTED"),
    WAITING_PAYMENT("WAITING_PAYMENT");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}