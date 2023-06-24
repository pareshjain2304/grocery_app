package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryCharges implements Serializable {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("delivery_charges")
    @Expose
    private String delivery_charges;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("distance_m")
    @Expose
    private String distance_m;
    @SerializedName("distance_km")
    @Expose
    private String distance_km;
    @SerializedName("charges")
    @Expose
    private String charges;
    @SerializedName("message")
    @Expose
    private String message;
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDistance_m() {
        return distance_m;
    }

    public void setDistance_m(String distance_m) {
        this.distance_m = distance_m;
    }

    public String getDistance_km() {
        return distance_km;
    }

    public void setDistance_km(String distance_km) {
        this.distance_km = distance_km;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
