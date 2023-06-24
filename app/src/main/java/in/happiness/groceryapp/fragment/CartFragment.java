package in.happiness.groceryapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.adaptar.CartAdaptar;
import in.happiness.groceryapp.adaptar.LocationAdaptar;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.CartItem;
import in.happiness.groceryapp.model.Coupon;
import in.happiness.groceryapp.model.Data;
import in.happiness.groceryapp.model.DeliveryCharges;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;

import in.happiness.groceryapp.utils.AppDBHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements CartAdaptar.ItemClickListener, LocationAdaptar.ItemClickListener {
    private View v;
    private LinearLayout llTopMenu, llBack, llClose, llPayment, llCCode, llDeliveryCharge;
    private TextView tvSubTotal, tvDiscount, tvTotal, tvDelivery;
    private ImageView tvAddLocation;
    private RecyclerView rvCart;
    private CartAdaptar cartAdaptar;
    private EditText edCCode;
    private RecyclerView.LayoutManager layoutManagerCart;
    private RecyclerView userLocationView;
    private ArrayList<Data> userLocation;
    private RecyclerView.LayoutManager locationManager;
    private LocationAdaptar locationAdaptar;
    private String token, code;
    private String locationId=null;
    private boolean applyCoupon=false;
    private boolean paymentScreen=false;
    private String total;
    private ProgressDialog progressDialog;
    private AppDBHelper dbHelper;
    public CartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cart, container, false);
        init();
        dbHelper=new AppDBHelper(getContext());
        AppConstant.cart_item = dbHelper.getCartItem();
        saveCartItem();
        int subTotal = dbHelper.getSubTotal();
        tvSubTotal.setText(String.valueOf(subTotal));
        tvTotal.setText(String.valueOf(subTotal));
        llTopMenu.setVisibility(View.GONE);
        token=AppConstant.userToken;
        setListener();
        if (getArguments()!=null) {
            total=(String) getArguments().get("total");
            tvTotal.setText(total);
            locationId=(String) getArguments().get("locationId");
            if (getArguments().containsKey("valid")) {
                applyCoupon = (boolean) getArguments().get("valid");
                if (applyCoupon) {
                    code = (String) getArguments().get("code");
                    edCCode.setText(code);
                    applyCode(edCCode.getText().toString(), true);
                }
                calculateDeliveryCharge(locationId);
            }
        }
        getUserLocation();
        onBackPressed();
        return v;
    }

    private void saveCartItem() {
        for (CartItem c : AppConstant.cart_item) {
            changeItemQtyWithVariant(String.valueOf(c.getProduct_user_id()), String.valueOf(c.getQuantity()), String.valueOf(c.getVariant_id()));
        }
        if (AppConstant.cart_item.size()>0) {
            cartAdaptar = new CartAdaptar(AppConstant.cart_item, getActivity(), this, dbHelper);
            layoutManagerCart = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rvCart.setLayoutManager(layoutManagerCart);
            rvCart.setItemAnimator(new DefaultItemAnimator());
            rvCart.setAdapter(cartAdaptar);
        }
    }

    private void changeItemQtyWithVariant(String productId, String qty, String variant) {
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), productId);
        RequestBody quantity = RequestBody.create(MediaType.parse("text/plain"), qty);
        RequestBody variantId = RequestBody.create(MediaType.parse("text/plain"), variant);
        Call<OTP> call = RetrofitClient.getApiInterface().addToCart("Bearer " + AppConstant.userToken, product_id, quantity, variantId);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() != 200) {
                        String msg = "";
                        if (response.body().getMessage()!=null) {
                            msg=response.body().getMessage();
                        }
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
            }
        });
    }

    private void onBackPressed() {
        //You need to add the following line for this solution to work; thanks skayred
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                      loadFragment(new MainFragment());
                    return true;
                }
                return false;
            }
        });
    }

    private void getUserLocation() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<OTP> call = RetrofitClient.getApiInterface().getUserLocation("Bearer "+token);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        OTP location = response.body();
                        if (location.isStatus()) {

                        }
                        if (location.isStatus()) {
                            userLocation = location.getData();
                            if (userLocation.size()>0) {
                                if (locationId!=null) {
                                    for (int i = 0; i < userLocation.size(); i++) {
                                        if (Integer.valueOf(locationId)==userLocation.get(i).getId()) {
                                            userLocation.get(i).setFlag(true);
                                        }
                                    }
                                }
                                setLocation(userLocation);
                            } else {
                                userLocationView.setVisibility(View.GONE);
                            }
                        }
                        Log.i("Hello", "hello");
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLocation(ArrayList<Data> userLocation) {
        userLocationView.setVisibility(View.VISIBLE);
        locationAdaptar = new LocationAdaptar(userLocation,getActivity(),CartFragment.this);
        locationManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        userLocationView.setLayoutManager(locationManager);
        userLocationView.setItemAnimator(new DefaultItemAnimator());
        userLocationView.setAdapter(locationAdaptar);
    }

    private void setListener() {
        tvAddLocation.setOnClickListener(v->{
            if (locationId==null) {
                locationId="0";
            }
            loadFragment(new LocationFragment(), tvTotal.getText().toString(), locationId);
        });
        llBack.setOnClickListener(v->{
            loadFragment(new MainFragment());
        });
        llCCode.setOnClickListener(v->{
            if (validate(edCCode, "promo code")) {
                applyCode(edCCode.getText().toString(), false);
            }
        });
        llClose.setOnClickListener(v->{
            loadFragment(new MainFragment());
        });
        llPayment.setOnClickListener(v->{
            if (paymentScreen) {
                loadFragment(new PaymentFragment(), tvTotal.getText().toString(), locationId);
            } else {
                Toast.makeText(getActivity(), "Select your location.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFragment(Fragment fragment, String total, String locationId) {
        Bundle bundle = new Bundle();
        bundle.putString("total",total);
        if (applyCoupon) {
            bundle.putString("code",edCCode.getText().toString());
            bundle.putBoolean("valid", true);
        }
        bundle.putString("locationId", locationId);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void applyCode(String couponCode, boolean flag) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody cCode = RequestBody.create(MediaType.parse("text/plain"), couponCode);
        Call<Coupon> call = RetrofitClient.getApiInterface().applyCouponCode("Bearer "+token, cCode);
        call.enqueue(new Callback<Coupon>() {
            @Override
            public void onResponse(Call<Coupon> call, Response<Coupon> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        Coupon msg = response.body();
                        if (Boolean.valueOf(msg.isStatus())!=null && msg.isStatus()) {
                            if (!flag) {
                                Toast.makeText(getActivity(), msg.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            applyCoupon=true;
                            tvSubTotal.setText(msg.getSubtotal());
                            tvDiscount.setText(msg.getDiscount());
                            tvDelivery.setText(msg.getDelivery_charges());
                            tvTotal.setText(msg.getTotal());
                        } else {
                            Toast.makeText(getActivity(), msg.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Coupon> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate(EditText editText, String type) {
        String value = editText.getText().toString();
        if (value.equals("") || value.isEmpty()) {
            Toast.makeText(getActivity(), "please enter "+type, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void init() {
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        llBack = v.findViewById(R.id.llBack);
        llClose = v.findViewById(R.id.llClose);
        llPayment = v.findViewById(R.id.llPayment);
        llDeliveryCharge = v.findViewById(R.id.llDeliveryCharge);
        tvAddLocation = v.findViewById(R.id.tvAddLocation);
        llCCode = v.findViewById(R.id.llCCode);
        edCCode = v.findViewById(R.id.edCCode);
        tvSubTotal = v.findViewById(R.id.tvSubTotal);
        tvDiscount = v.findViewById(R.id.tvDiscount);
        tvTotal = v.findViewById(R.id.tvTotal);
        tvDelivery = v.findViewById(R.id.tvDelivery);
        rvCart = v.findViewById(R.id.rvCart);
        userLocationView = v.findViewById(R.id.userLocation);
        userLocationView.setVisibility(View.GONE);
    }

    @Override
    public void subCategoryId(int lastSelectedPosition) {
    }

    @Override
    public void onLocationId(int position) {
        for (int i=0; i<userLocation.size(); i++) {
            userLocation.get(i).setFlag(false);
        }
        userLocation.get(position).setFlag(true);
        locationId=String.valueOf(userLocation.get(position).getId());
        setLocation(userLocation);
        calculateDeliveryCharge(String.valueOf(userLocation.get(position).getId()));
    }

    private void calculateDeliveryCharge(String location_id) {
        RequestBody locationId = RequestBody.create(MediaType.parse("text/plain"), location_id);
        Call<DeliveryCharges> call = RetrofitClient.getApiInterface().deliveryCharges("Bearer "+token, locationId);
        call.enqueue(new Callback<DeliveryCharges>() {
            @Override
            public void onResponse(Call<DeliveryCharges> call, Response<DeliveryCharges> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body().isStatus()) {
                            llDeliveryCharge.setVisibility(View.VISIBLE);
                            tvSubTotal.setText(response.body().getSubtotal());
                            tvDiscount.setText(response.body().getDiscount());
                            tvDelivery.setText(response.body().getDelivery_charges());
                            tvTotal.setText(response.body().getTotal());
                            paymentScreen = true;
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeliveryCharges> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void deleteLocation(int position) {
        delLocation(userLocation.get(position).getId());
    }

    private void delLocation(int id) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody location_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
        Call<OTP> call = RetrofitClient.getApiInterface().deleteLocation("Bearer "+token, location_id);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        OTP msg = response.body();
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                        loadFragment(new CartFragment());
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}