package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProfile implements Serializable {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("subtotal")
    @Expose
    private double subtotal;
    @SerializedName("discount")
    @Expose
    private float discount;
    @SerializedName("delivery_charges")
    @Expose
    private float delivery_charges;
    @SerializedName("total")
    @Expose
    private float total;
    @SerializedName("cart_items")
    @Expose
    private ArrayList<CartItem> cart_items;
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(float delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public ArrayList<CartItem> getCart_items() {
        return cart_items;
    }

    public void setCart_items(ArrayList<CartItem> cart_items) {
        this.cart_items = cart_items;
    }
}
