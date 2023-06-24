package in.happiness.groceryapp.json;

import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.Coupon;
import in.happiness.groceryapp.model.DeliveryCharges;
import in.happiness.groceryapp.model.FavoriteMain;
import in.happiness.groceryapp.model.GenerateToken;
import in.happiness.groceryapp.model.HomeScreen;
import in.happiness.groceryapp.model.MainData;
import in.happiness.groceryapp.model.MyTickets;
import in.happiness.groceryapp.model.Notifications;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.OrderDetail;
import in.happiness.groceryapp.model.OrderList;
import in.happiness.groceryapp.model.StoreProducts;
import in.happiness.groceryapp.model.Stores;
import in.happiness.groceryapp.model.StoresProducts;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.model.UserProfile;
import in.happiness.groceryapp.model.Vendor;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @Multipart
    @POST("user-otp-request")
    Call<OTP> getOTP(@Part("mobile") RequestBody mobile);

    @Multipart
    @POST("user-otp-varification")
    Call<OTP> verifyOTP(@Part("mobile") RequestBody mobile, @Part("otp") RequestBody otp);

    @Multipart
    @POST("user-signup")
    Call<OTP> userSignUp(@Header("Authorization") String token, @Part("name") RequestBody userName,
                              @Part("email") RequestBody userEmail,  @Part("referal_code") RequestBody referenceCode);

    @GET("categories")
    Call<ArrayList<Category>> getCategories();

    @GET("subcategories/{id}")
    Call<ArrayList<SubCategory>> getSubCategories(@Path("id") int id);

    @Multipart
    @POST("user-shop-list")
    Call<ArrayList<Stores>> getStores(@Header("Authorization") String token, @Part("category_id") RequestBody category_id,
                                      @Part("search") RequestBody search, @Part("latitude") RequestBody latitude, @Part("longitude")  RequestBody longitude);

    @Multipart
    @POST("user-product-list")
    Call<StoresProducts> getStoreProducts(@Header("Authorization") String token, @Part("shop_id") RequestBody shop_id);

    @Multipart
    @POST("user-product-details")
    Call<StoreProducts> getProductDetails(@Header("Authorization") String token, @Part("product_user_id") RequestBody product_user_id,
                                          @Part("search") RequestBody search);

    @Multipart
    @POST("user-shop-list")
    Call<ArrayList<Stores>> getLocalShops(@Header("Authorization") String token, @Part("latitude") RequestBody latitude,@Part("longitude")  RequestBody longitude);

    @GET("details")
    Call<User> getUserDetails(@Header("Authorization")  String token);

    @Multipart
    @POST("add-to-cart")
    Call<OTP> addToCart(@Header("Authorization") String token,@Part("product_user_id") RequestBody product_id,@Part("quantity") RequestBody quantity,@Part("variant_id") RequestBody variantId);

    @Multipart
    @POST("add-to-cart")
    Call<OTP> addToCart(@Header("Authorization") String token,@Part("product_user_id") RequestBody product_id,@Part("quantity") RequestBody quantity);

    @Multipart
    @POST("delete-cart-item")
    Call<OTP> removeItemCart(@Header("Authorization") String token, @Part("product_user_id") RequestBody product_id);

    @POST("get-cart-items")
    Call<UserProfile> getCartItem(@Header("Authorization")  String token);

    @POST("get-locations")
    Call<OTP> getUserLocation(@Header("Authorization")  String token);

    @Multipart
    @POST("user-apply-coupon")
    Call<Coupon> applyCouponCode(@Header("Authorization")  String token, @Part("coupon_code") RequestBody cCode);

    @Multipart
    @POST("user-place-order")
    Call<OTP> placeOrder(@Header("Authorization")  String token, @Part("payment_method")  RequestBody rPaymentType ,@Part("location_id")  RequestBody rLocationId,
                         @Part("coupon_code") RequestBody rCode, @Part("notes") RequestBody rNote);

    @Multipart
    @POST("track-order")
    Call<OrderDetail> orderDetail(@Header("Authorization")  String token, @Part("order_id") RequestBody orderId);

    @Multipart
    @POST("user-my-orders")
    Call<OrderList>  getOrders(@Header("Authorization")  String token, @Part("status") RequestBody status);

    @Multipart
    @POST("add-location")
    Call<OTP> addLocation(@Header("Authorization") String token, @Part("title") RequestBody title, @Part("fullname") RequestBody fullname,@Part("address") RequestBody address,
                          @Part("city") RequestBody city,@Part("state") RequestBody state,@Part("zipcode") RequestBody zipcode,@Part("phone") RequestBody phone,@Part("latitude") RequestBody latitude,
                          @Part("longitude") RequestBody longitude, @Part("landmark") RequestBody landmark);

    @Multipart
    @POST("delete-location")
    Call<OTP> deleteLocation(@Header("Authorization") String token,@Part("location_id")  RequestBody location_id);

    @Multipart
    @POST("order-details")
    Call<OrderDetail> orderDetails(@Header("Authorization") String token,@Part("order_id") RequestBody orderId);

    @Multipart
    @POST("calculate-delivery-charges")
    Call<DeliveryCharges> deliveryCharges(@Header("Authorization") String token, @Part("location_id") RequestBody locationId);

    @GET("app_settings")
    Call<HomeScreen> appSetting(@Header("Authorization")  String token);

    @Multipart
    @POST("generate-cashfree-token")
    Call<GenerateToken> generateCashfreeToken(@Header("Authorization") String token, @Part("order_id") RequestBody orderId,
                                              @Part("order_amount") RequestBody orderAmount);

    @Multipart
    @POST("user-order-payment")
    Call<OTP> savePaymentDetails(@Header("Authorization") String token,@Part("token") RequestBody paymentToken,@Part("payment_mode") RequestBody paymentMode,
                                 @Part("order_id") RequestBody requestId,@Part("reference_id") RequestBody referenceId,@Part("message") RequestBody message,
                                 @Part("amount") RequestBody amount,@Part("status") RequestBody paymentStatus,@Part("payment_type") RequestBody paymentType);

    @Multipart
    @POST("user-update-profile")
    Call<OTP> updateProfile(@Header("Authorization") String token,@Part("name") RequestBody userName,@Part("mobile") RequestBody userMobile,
                            @Part("email") RequestBody userEmail);

    @Multipart
    @POST("device-token")
    Call<OTP> updateToken(@Header("Authorization") String token,@Part("token") RequestBody userToken,@Part("app_version") RequestBody versionCode);

    @Multipart
    @POST("update-location")
    Call<OTP> updateLocation(@Header("Authorization") String token, @Part("location_id") RequestBody locationId, @Part("title") RequestBody title, @Part("fullname") RequestBody fullname,@Part("address") RequestBody address,
                          @Part("city") RequestBody city,@Part("state") RequestBody state,@Part("zipcode") RequestBody zipcode,@Part("phone") RequestBody phone,@Part("latitude") RequestBody latitude,
                          @Part("longitude") RequestBody longitude, @Part("landmark") RequestBody landmark);

    @POST("my-tickets")
    Call<MyTickets> myTickets(@Header("Authorization") String token);

    @Multipart
    @POST("generate-ticket")
    Call<OTP> generateTickets(@Header("Authorization") String token, @Part("title") RequestBody title, @Part("description") RequestBody description);

    @POST("user-wallet-transactions")
    Call<OTP> userTransactions(@Header("Authorization") String token);

    @Multipart
    @POST("ticket-details")
    Call<MyTickets> getTicketDetails(@Header("Authorization") String token,
                                     @Part("ticket_id") RequestBody ticket_id);

    @Multipart
    @POST("reply-to-ticket")
    Call<OTP> replyToTicket(@Header("Authorization") String token,@Part("ticket_id") RequestBody ticketId,@Part("reply_message") RequestBody message);

    @POST("get-notifications")
    Call<Notifications> getNotifications(@Header("Authorization") String token);

    @Multipart
    @POST("get-user-details")
    Call<Vendor> vendorDetails(@Part("user_id") RequestBody vendor_id);

    @Multipart
    @POST("user-order-status")
    Call<OTP> userOrderStatus(@Header("Authorization") String token,@Part("order_id") RequestBody order_id,@Part("status") RequestBody status);

    @Multipart
    @POST("user-favourite-store")
    Call<OTP> favorites(@Header("Authorization") String token,@Part("favourite_id") RequestBody order_id);

    @POST("user-favourite-store")
    Call<FavoriteMain> favoriteList(@Header("Authorization") String token);

    @Multipart
    @POST("user-feedback-store")
    Call<OTP> submitVendorFeedback(@Header("Authorization") String s,@Part("order_id") RequestBody order_id,@Part("vendor_id") RequestBody vendor_id,
                             @Part("rate") RequestBody rate,@Part("message") RequestBody message);

    @Multipart
    @POST("user-feedback-store")
    Call<OTP> submitDeliveryBoyFeedback(@Header("Authorization") String s,@Part("order_id") RequestBody order_id,@Part("deliveryboy_id") RequestBody deliveryboy_id,
                                   @Part("rate") RequestBody rate,@Part("message") RequestBody message);

    @Multipart
    @POST("notify_data")
    Call<MainData> getData(@Header("Authorization") String token, @Part("last_date") RequestBody lastDate);
}
