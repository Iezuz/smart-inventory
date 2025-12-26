package com.smartinventory.transactions.dto;

public class TransactionRequest {
    private Long productId;
    private String type;
    private int quantity;

    public TransactionRequest() {}

    public TransactionRequest(Long productId, String type, int quantity) {
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
