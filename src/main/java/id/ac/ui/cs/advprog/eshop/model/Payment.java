package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

import java.util.Map;

public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;
    Order order;

    public Payment(String id, String method, Order order, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.order = order;
        this.paymentData = paymentData;
        this.status = validatePayment(method, paymentData);
    }

    private String validatePayment(String method, Map<String, String> paymentData) {
        if (method.equals(PaymentMethod.VOUCHER.getValue())) {
            return validateVoucher(paymentData.get("voucherCode"));
        } else if (method.equals(PaymentMethod.BANK_TRANSFER.getValue())) {
            return validateBankTransfer(paymentData);
        }
        return PaymentStatus.REJECTED.getValue();
    }

    private String validateVoucher(String voucherCode) {
        if (voucherCode == null) return PaymentStatus.REJECTED.getValue();
        if (voucherCode.length() != 16) return PaymentStatus.REJECTED.getValue();
        if (!voucherCode.startsWith("ESHOP")) return PaymentStatus.REJECTED.getValue();

        long numCount = voucherCode.chars()
                .filter(Character::isDigit)
                .count();
        if (numCount != 8) return PaymentStatus.REJECTED.getValue();

        return PaymentStatus.SUCCESS.getValue();
    }

    private String validateBankTransfer(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");

        if (bankName == null || bankName.isEmpty()) return PaymentStatus.REJECTED.getValue();
        if (referenceCode == null || referenceCode.isEmpty()) return PaymentStatus.REJECTED.getValue();

        return PaymentStatus.SUCCESS.getValue();
    }

    public String getId() { return id; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Map<String, String> getPaymentData() { return paymentData; }
    public Order getOrder() { return order; }
}