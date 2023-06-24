package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Variant implements Serializable {
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("product_user_id")
    @Expose
    String product_user_id;
    @SerializedName("variants")
    @Expose
    String variants;
    @SerializedName("quantity")
    @Expose
    String quantity;
    @SerializedName("unit")
    @Expose
    String unit;
    @SerializedName("price")
    @Expose
    String price;
    @SerializedName("selling_price")
    @Expose
    String selling_price;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_user_id() {
        return product_user_id;
    }

    public void setProduct_user_id(String product_user_id) {
        this.product_user_id = product_user_id;
    }

    public String getVariants() {
        return variants;
    }

    public void setVariants(String variants) {
        this.variants = variants;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
