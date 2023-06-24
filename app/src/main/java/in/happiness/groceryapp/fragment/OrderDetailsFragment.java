package in.happiness.groceryapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.activity.DoPaymentActivity;
import in.happiness.groceryapp.adaptar.OrderItemAdapter;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.DeliveryBoy;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.Order;
import in.happiness.groceryapp.model.OrderDetail;
import in.happiness.groceryapp.model.OrderItems;
import in.happiness.groceryapp.model.Vendor;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsFragment extends Fragment implements OrderItemAdapter.ItemClickListener{
    View view;
    LinearLayout llTopMenu, llClose, llBack, llDeliveyBoyDetails, llActionBtn;
    private TextView tvOrderId,tvCreateDate, tvStatus, tvDeliveryCharge, tvDiscount, tvTotal, tvSubTotal, storeName, shippingAddress, paymentMode;
    private TextView tvMobNumber, shopAddress, tvShopZipCode, tvDeliveryBoyName, tvDeliveryBoyNumber, tvBtnType, tvNotes, tvFeedback;
    private String order_id, token;
    private Order order;
    private RecyclerView.LayoutManager layoutManagerCart;
    private RecyclerView orderList;
    private OrderItemAdapter orderAdaptar;
    private ProgressDialog progressDialog;
    private Vendor vendor;

    public OrderDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_details, container, false);
        token = AppConstant.userToken;
        init();
        llTopMenu.setVisibility(View.GONE);
        if (getArguments()!=null) {
            order_id = (String) getArguments().get("order_id");
            orderDetails(order_id);
        }
        setListener();
        return view;
    }

    private void orderDetails(String order_id) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody orderId = RequestBody.create(MediaType.parse("text/plain"), order_id);
        Call<OrderDetail> call = RetrofitClient.getApiInterface().orderDetails("Bearer "+token, orderId);
        call.enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        order = response.body().getData();
                        tvOrderId.setText(order.getOrderNumber());
                        tvCreateDate.setText(order.getCreatedAt());
                        tvStatus.setText(order.getStatus());
                        tvDeliveryCharge.setText(order.getDeliveryCharges());
                        tvDiscount.setText(order.getDiscount());
                        tvTotal.setText(order.getGrandTotal());
                        tvNotes.setText(order.getNotes());
                        tvSubTotal.setText(order.getSubtotal());
                        shippingAddress.setText(order.getShippingAddress());
                        if (order.getStatus().toLowerCase().equals("delivered") && order.getFeedbackAvailable().equals("0")) {
                            tvFeedback.setVisibility(View.VISIBLE);
                        }
                        if (order.getPaymentMethod() != null) {
                            if (order.getStatus().toLowerCase().equals("pending") && order.getPaymentMethod().toLowerCase().equals("online")){
                                tvBtnType.setText("RETRY");
                                llActionBtn.setVisibility(View.VISIBLE);
                            }
                            if (order.getStatus().toLowerCase().equals("placed")){
                                tvBtnType.setText("CANCEL");
                                llActionBtn.setVisibility(View.VISIBLE);
                            }
                            if (order.getStatus().toLowerCase().equals("pending") && (order.getPaymentMethod().toLowerCase().equals("cash")
                            ||order.getPaymentMethod().toLowerCase().equals("wallet") )){
                                tvBtnType.setText("CANCEL");
                                llActionBtn.setVisibility(View.VISIBLE);
                            }
                            paymentMode.setText(order.getPaymentMethod().substring(0, 1).toUpperCase() + order.getPaymentMethod().substring(1).toLowerCase());
                        }
                        vendor = order.getVendorDetails();
                        if (vendor!=null) {
                            storeName.setText(vendor.getShop_name());
                            tvMobNumber.setText(vendor.getPrimary_mobile());
                            shopAddress.setText(vendor.getShop_address());
                            tvShopZipCode.setText(vendor.getPincode());
                        }
                        if (order.getDelivery_boy()!=null) {
                            llDeliveyBoyDetails.setVisibility(View.VISIBLE);
                            tvDeliveryBoyName.setText(order.getDelivery_boy().getName());
                            tvDeliveryBoyNumber.setText(order.getDelivery_boy().getMobile());
                        }
                        orderList(order.getOrderItems());
//                        getVendorDetails(order.getVendorId(), order.getDelivery_boy());
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
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getVendorDetails(String vendorId, DeliveryBoy deliveryBoy) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody vendor_id = RequestBody.create(MediaType.parse("text/plain"), vendorId);
        Call<Vendor> call = RetrofitClient.getApiInterface().vendorDetails(vendor_id);
        call.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        vendor = response.body();
                        if (vendor!=null) {
                            storeName.setText(vendor.getShop_name());
                            tvMobNumber.setText(vendor.getPrimary_mobile());
                            shopAddress.setText(vendor.getShop_address());
                            tvShopZipCode.setText(vendor.getPincode());
                        }
                        if (deliveryBoy!=null) {
                            llDeliveyBoyDetails.setVisibility(View.VISIBLE);
                            tvDeliveryBoyName.setText(deliveryBoy.getName());
                            tvDeliveryBoyNumber.setText(deliveryBoy.getMobile());
                        }
                        orderList(order.getOrderItems());
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
            public void onFailure(Call<Vendor> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void orderList(ArrayList<OrderItems> order_items) {
        orderAdaptar = new OrderItemAdapter(order_items, getActivity(), OrderDetailsFragment.this);
        layoutManagerCart = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        orderList.setLayoutManager(layoutManagerCart);
        orderList.setItemAnimator(new DefaultItemAnimator());
        orderList.setAdapter(orderAdaptar);
    }

    private void setListener() {
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FeedBackFormFragment(), order_id, vendor.getId(), order.getDelivery_boy().getId());
            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new OrdersFragment());
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        llActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvBtnType.getText().toString().equals("RETRY")) {
                    startActivity(new Intent(getActivity(), DoPaymentActivity.class).putExtra("request_id", order_id)
                            .putExtra("amount", order.getGrandTotal()).putExtra("token", token));
                }
                if (tvBtnType.getText().toString().equals("CANCEL")) {
                    cancelOrder(order.getId(), "cancelled");
                }
            }
        });
        tvMobNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+vendor.getPrimary_mobile()));
                getActivity().startActivity(i);
            }
        });
        tvDeliveryBoyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+order.getDelivery_boy().getMobile()));
                getActivity().startActivity(i);
            }
        });
    }

    private void loadFragment(Fragment fragment, String order_id, String vendor_id, String deliveryBoy_id) {
        Bundle bundle = new Bundle();
        bundle.putString("order_id", order_id);
        bundle.putString("vendor_id", vendor_id);
        bundle.putString("deliveryBoy_id", deliveryBoy_id);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void cancelOrder(String id, String orderStatus) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody order_id = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), orderStatus);
        Call<OTP> call = RetrofitClient.getApiInterface().userOrderStatus("Bearer "+token,order_id,status);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        if (response.body().isStatus()) {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            loadFragment(new OrdersFragment());
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
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        llClose = view.findViewById(R.id.llClose);
        llBack = view.findViewById(R.id.llBack);
        tvOrderId = view.findViewById(R.id.tvOrderId);
        tvCreateDate = view.findViewById(R.id.tvCreateDate);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvDeliveryCharge = view.findViewById(R.id.tvDeliveryCharge);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        orderList = view.findViewById(R.id.orderList);
        storeName = view.findViewById(R.id.storeName);
        shippingAddress = view.findViewById(R.id.shippingAddress);
        paymentMode=view.findViewById(R.id.paymentMode);
        tvMobNumber=view.findViewById(R.id.tvMobNumber);
        shopAddress=view.findViewById(R.id.shopAddress);
        tvShopZipCode=view.findViewById(R.id.tvShopZipCode);
        llDeliveyBoyDetails = view.findViewById(R.id.llDeliveyBoyDetails);
        llDeliveyBoyDetails.setVisibility(View.GONE);
        tvDeliveryBoyName = view.findViewById(R.id.tvDeliveryBoyName);
        tvDeliveryBoyNumber = view.findViewById(R.id.tvDeliveryBoyNumber);
        llActionBtn = view.findViewById(R.id.llActionBtn);
        llActionBtn.setVisibility(View.GONE);
        tvBtnType = view.findViewById(R.id.tvBtnType);
        tvNotes = view.findViewById(R.id.tvNotes);
        tvFeedback = view.findViewById(R.id.tvFeedback);
        tvFeedback.setVisibility(View.GONE);
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

    @Override
    public void subCategoryId(int lastSelectedPosition) {

    }
}