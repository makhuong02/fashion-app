package com.group25.ecommercefashionapp.data;

public class CartItem extends Item{
    private final Long productId;
    private int quantity;
    private final ProductColor selectedColor;
    private final ProductSize selectedSize;
    private String phoneNumber;

    public CartItem(Long productId, int quantity, ProductColor selectedColor, ProductSize selectedSize, String phoneNumber) {
        super("");
        this.productId = productId;
        this.quantity = quantity;
        this.selectedColor = selectedColor;
        this.selectedSize = selectedSize;
        this.phoneNumber = phoneNumber;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Long getProductId() {
        return productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public ProductColor getSelectedColor() {
        return selectedColor;
    }
    public ProductSize getSelectedSize() {
        return selectedSize;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CartItem) {
            CartItem cartItem = (CartItem) obj;
            return this.productId == cartItem.productId &&
                    this.selectedColor.equals(cartItem.selectedColor) &&
                    this.selectedSize.equals(cartItem.selectedSize) &&
                    this.phoneNumber.equals(cartItem.phoneNumber);
        }
        return false;
    }
}
