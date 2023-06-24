package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartItem implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("product_user_id")
    @Expose
    private int product_user_id;
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("vendor_id")
    @Expose
    private int vendor_id;
    @SerializedName("price")
    @Expose
    private float price;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("variant_id")
    @Expose
    private int variant_id;
    @SerializedName("product_attributes")
    @Expose
    private Object product_attributes;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("subtotal")
    @Expose
    private float subtotal;
    @SerializedName("category_id")
    @Expose
    private int category_id;
    @SerializedName("subcategory_id")
    @Expose
    private int subcategory_id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_user_id() {
        return product_user_id;
    }

    public void setProduct_user_id(int product_user_id) {
        this.product_user_id = product_user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
    }

    public Object getProduct_attributes() {
        return product_attributes;
    }

    public void setProduct_attributes(Object product_attributes) {
        this.product_attributes = product_attributes;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(int subcategory_id) {
        this.subcategory_id = subcategory_id;
    }
}
