package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("deliveryboy_id")
    @Expose
    private String deliveryboyId;
    @SerializedName("feedback_available")
    @Expose
    private String feedbackAvailable;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("item_count")
    @Expose
    private String itemCount;
    @SerializedName("is_paid")
    @Expose
    private String isPaid;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("shipping_fullname")
    @Expose
    private String shippingFullname;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("shipping_city")
    @Expose
    private String shippingCity;
    @SerializedName("shipping_state")
    @Expose
    private String shippingState;
    @SerializedName("shipping_zipcode")
    @Expose
    private String shippingZipcode;
    @SerializedName("shipping_phone")
    @Expose
    private String shippingPhone;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("billing_fullname")
    @Expose
    private String billingFullname;
    @SerializedName("billing_address")
    @Expose
    private String billingAddress;
    @SerializedName("billing_city")
    @Expose
    private String billingCity;
    @SerializedName("billing_state")
    @Expose
    private String billingState;
    @SerializedName("billing_zipcode")
    @Expose
    private String billingZipcode;
    @SerializedName("billing_phone")
    @Expose
    private String billingPhone;
    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("processing_at")
    @Expose
    private String processingAt;
    @SerializedName("dispatched_at")
    @Expose
    private String dispatchedAt;
    @SerializedName("cancelled_at")
    @Expose
    private String cancelledAt;
    @SerializedName("delivered_at")
    @Expose
    private String deliveredAt;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("delivery_charges")
    @Expose
    private String deliveryCharges;
    @SerializedName("placed_at")
    @Expose
    private String placedAt;
    @SerializedName("packed_at")
    @Expose
    private String packedAt;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("vendor_details")
    @Expose
    private Vendor vendorDetails;
    @SerializedName("delivery_boy")
    @Expose
    private DeliveryBoy delivery_boy;
    @SerializedName("order_items")
    @Expose
    private ArrayList<OrderItems> orderItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getDeliveryboyId() {
        return deliveryboyId;
    }

    public void setDeliveryboyId(String deliveryboyId) {
        this.deliveryboyId = deliveryboyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getShippingFullname() {
        return shippingFullname;
    }

    public void setShippingFullname(String shippingFullname) {
        this.shippingFullname = shippingFullname;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingZipcode() {
        return shippingZipcode;
    }

    public void setShippingZipcode(String shippingZipcode) {
        this.shippingZipcode = shippingZipcode;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBillingFullname() {
        return billingFullname;
    }

    public void setBillingFullname(String billingFullname) {
        this.billingFullname = billingFullname;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    public String getBillingState() {
        return billingState;
    }

    public void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    public String getBillingZipcode() {
        return billingZipcode;
    }

    public void setBillingZipcode(String billingZipcode) {
        this.billingZipcode = billingZipcode;
    }

    public String getBillingPhone() {
        return billingPhone;
    }

    public void setBillingPhone(String billingPhone) {
        this.billingPhone = billingPhone;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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

    public String getProcessingAt() {
        return processingAt;
    }

    public void setProcessingAt(String processingAt) {
        this.processingAt = processingAt;
    }

    public String getDispatchedAt() {
        return dispatchedAt;
    }

    public void setDispatchedAt(String dispatchedAt) {
        this.dispatchedAt = dispatchedAt;
    }

    public String getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(String cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(String deliveredAt) {
        this.deliveredAt = deliveredAt;
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

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(String placedAt) {
        this.placedAt = placedAt;
    }

    public String getPackedAt() {
        return packedAt;
    }

    public void setPackedAt(String packedAt) {
        this.packedAt = packedAt;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Vendor getVendorDetails() {
        return vendorDetails;
    }

    public void setVendorDetails(Vendor vendorDetails) {
        this.vendorDetails = vendorDetails;
    }

    public DeliveryBoy getDelivery_boy() {
        return delivery_boy;
    }

    public void setDelivery_boy(DeliveryBoy delivery_boy) {
        this.delivery_boy = delivery_boy;
    }

    public ArrayList<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public String getFeedbackAvailable() {
        return feedbackAvailable;
    }

    public void setFeedbackAvailable(String feedbackAvailable) {
        this.feedbackAvailable = feedbackAvailable;
    }
}
