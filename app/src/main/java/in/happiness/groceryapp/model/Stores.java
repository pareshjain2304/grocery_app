package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stores implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified_at")
    @Expose
    private Object emailVerifiedAt;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("mobile_verified_at")
    @Expose
    private Object mobileVerifiedAt;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_address")
    @Expose
    private String shopAddress;
    @SerializedName("city_id")
    @Expose
    private long cityId;
    @SerializedName("area_id")
    @Expose
    private long areaId;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("whatsapp")
    @Expose
    private String whatsapp;
    @SerializedName("primary_mobile")
    @Expose
    private String primaryMobile;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;
    @SerializedName("about_shop")
    @Expose
    private Object aboutShop;
    @SerializedName("shop_open_time")
    @Expose
    private Object shopOpenTime;
    @SerializedName("shop_close_time")
    @Expose
    private Object shopCloseTime;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("shop_approved")
    @Expose
    private String shopApproved;
    @SerializedName("shop_online")
    @Expose
    private String shopOnline;
    @SerializedName("min_order")
    @Expose
    private long minOrder;
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
    private String referalCode;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("distance")
    @Expose
    private String distance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Object getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Object emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getMobileVerifiedAt() {
        return mobileVerifiedAt;
    }

    public void setMobileVerifiedAt(Object mobileVerifiedAt) {
        this.mobileVerifiedAt = mobileVerifiedAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
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

    public String getPrimaryMobile() {
        return primaryMobile;
    }

    public void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile = primaryMobile;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public Object getAboutShop() {
        return aboutShop;
    }

    public void setAboutShop(Object aboutShop) {
        this.aboutShop = aboutShop;
    }

    public Object getShopOpenTime() {
        return shopOpenTime;
    }

    public void setShopOpenTime(Object shopOpenTime) {
        this.shopOpenTime = shopOpenTime;
    }

    public Object getShopCloseTime() {
        return shopCloseTime;
    }

    public void setShopCloseTime(Object shopCloseTime) {
        this.shopCloseTime = shopCloseTime;
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

    public String getShopApproved() {
        return shopApproved;
    }

    public void setShopApproved(String shopApproved) {
        this.shopApproved = shopApproved;
    }

    public String getShopOnline() {
        return shopOnline;
    }

    public void setShopOnline(String shopOnline) {
        this.shopOnline = shopOnline;
    }

    public long getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(long minOrder) {
        this.minOrder = minOrder;
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

    public String getReferalCode() {
        return referalCode;
    }

    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
