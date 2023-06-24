package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeScreen implements Serializable {
    @SerializedName("home_banner")
    @Expose
    private List<String> homeBanner = null;
    @SerializedName("add_banner")
    @Expose
    private List<String> addBanner = null;
    @SerializedName("order_details_banner")
    @Expose
    private List<Object> orderDetailsBanner = null;
    @SerializedName("privacy_policy")
    @Expose
    private String privacyPolicy;
    @SerializedName("terms_and_condition")
    @Expose
    private String termsAndCondition;

    public List<String> getHomeBanner() {
        return homeBanner;
    }

    public void setHomeBanner(List<String> homeBanner) {
        this.homeBanner = homeBanner;
    }

    public List<String> getAddBanner() {
        return addBanner;
    }

    public void setAddBanner(List<String> addBanner) {
        this.addBanner = addBanner;
    }

    public List<Object> getOrderDetailsBanner() {
        return orderDetailsBanner;
    }

    public void setOrderDetailsBanner(List<Object> orderDetailsBanner) {
        this.orderDetailsBanner = orderDetailsBanner;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getTermsAndCondition() {
        return termsAndCondition;
    }

    public void setTermsAndCondition(String termsAndCondition) {
        this.termsAndCondition = termsAndCondition;
    }
}
