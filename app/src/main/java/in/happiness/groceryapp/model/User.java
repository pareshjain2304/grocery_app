package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified_at")
    @Expose
    private String emailVerifiedAt;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("mobile_verified_at")
    @Expose
    private String mobileVerifiedAt;
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
    private String  cityId;
    @SerializedName("area_id")
    @Expose
    private String  areaId;
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
    private String aboutShop;
    @SerializedName("shop_open_time")
    @Expose
    private String shopOpenTime;
    @SerializedName("shop_close_time")
    @Expose
    private String shopCloseTime;
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
    private int minOrder;
    @SerializedName("otp")
    @Expose
    private int otp;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("referal_code")
    @Expose
    private String referalCode;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileVerifiedAt() {
        return mobileVerifiedAt;
    }

    public void setMobileVerifiedAt(String mobileVerifiedAt) {
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
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

    public String getAboutShop() {
        return aboutShop;
    }

    public void setAboutShop(String aboutShop) {
        this.aboutShop = aboutShop;
    }

    public String getShopOpenTime() {
        return shopOpenTime;
    }

    public void setShopOpenTime(String shopOpenTime) {
        this.shopOpenTime = shopOpenTime;
    }

    public String getShopCloseTime() {
        return shopCloseTime;
    }

    public void setShopCloseTime(String shopCloseTime) {
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

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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
}
