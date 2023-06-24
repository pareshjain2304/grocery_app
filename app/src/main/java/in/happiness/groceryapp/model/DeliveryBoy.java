package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryBoy implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified_at")
    @Expose
    private String email_verified_at;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("mobile_verified_at")
    @Expose
    private String mobile_verified_at;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("shop_name")
    @Expose
    private String shop_name;
    @SerializedName("shop_address")
    @Expose
    private String shop_address;
    @SerializedName("city_id")
    @Expose
    private String city_id;
    @SerializedName("area_id")
    @Expose
    private String area_id;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("whatsapp")
    @Expose
    private String whatsapp;
    @SerializedName("primary_mobile")
    @Expose
    private String primary_mobile;
    @SerializedName("shop_image")
    @Expose
    private String shop_image;
    @SerializedName("about_shop")
    @Expose
    private String about_shop;
    @SerializedName("shop_open_time")
    @Expose
    private String shop_open_time;
    @SerializedName("shop_close_time")
    @Expose
    private String shop_close_time;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("shop_approved")
    @Expose
    private String shop_approved;
    @SerializedName("shop_online")
    @Expose
    private String shop_online;
    @SerializedName("min_order")
    @Expose
    private String min_order;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("coupon_code")
    @Expose
    private String coupon_code;
    @SerializedName("referal_code")
    @Expose
    private String referal_code;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile_verified_at() {
        return mobile_verified_at;
    }

    public void setMobile_verified_at(String mobile_verified_at) {
        this.mobile_verified_at = mobile_verified_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getPrimary_mobile() {
        return primary_mobile;
    }

    public void setPrimary_mobile(String primary_mobile) {
        this.primary_mobile = primary_mobile;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getAbout_shop() {
        return about_shop;
    }

    public void setAbout_shop(String about_shop) {
        this.about_shop = about_shop;
    }

    public String getShop_open_time() {
        return shop_open_time;
    }

    public void setShop_open_time(String shop_open_time) {
        this.shop_open_time = shop_open_time;
    }

    public String getShop_close_time() {
        return shop_close_time;
    }

    public void setShop_close_time(String shop_close_time) {
        this.shop_close_time = shop_close_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getShop_approved() {
        return shop_approved;
    }

    public void setShop_approved(String shop_approved) {
        this.shop_approved = shop_approved;
    }

    public String getShop_online() {
        return shop_online;
    }

    public void setShop_online(String shop_online) {
        this.shop_online = shop_online;
    }

    public String getMin_order() {
        return min_order;
    }

    public void setMin_order(String min_order) {
        this.min_order = min_order;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getReferal_code() {
        return referal_code;
    }

    public void setReferal_code(String referal_code) {
        this.referal_code = referal_code;
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
