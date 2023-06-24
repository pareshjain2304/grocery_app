package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductTime implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("vendor_id")
    @Expose
    private int vendor_id;
    @SerializedName("product_id")
    @Expose
    private int product_id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("close_from_date")
    @Expose
    private String close_from_date;
    @SerializedName("close_to_date")
    @Expose
    private String close_to_date;
    @SerializedName("open_time")
    @Expose
    private String open_time;
    @SerializedName("close_time")
    @Expose
    private String close_time;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClose_from_date() {
        return close_from_date;
    }

    public void setClose_from_date(String close_from_date) {
        this.close_from_date = close_from_date;
    }

    public String getClose_to_date() {
        return close_to_date;
    }

    public void setClose_to_date(String close_to_date) {
        this.close_to_date = close_to_date;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }
}
