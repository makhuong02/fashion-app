package com.group25.ecommercefashionapp.data;

public class CartItem extends Item{
    private final int productId;
    private int quantity;
    private final ProductColor selectedColor;
    private final ProductSize selectedSize;

    public CartItem(int productId, int quantity, ProductColor selectedColor, ProductSize selectedSize) {
        super("");
        this.productId = productId;
        this.quantity = quantity;
        this.selectedColor = selectedColor;
        this.selectedSize = selectedSize;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getProductId() {
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CartItem) {
            CartItem cartItem = (CartItem) obj;
            return this.productId == cartItem.productId &&
                    this.selectedColor.equals(cartItem.selectedColor) &&
                    this.selectedSize.equals(cartItem.selectedSize);
        }
        return false;
    }
}
