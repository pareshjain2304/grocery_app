package in.happiness.groceryapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.activity.DoPaymentActivity;
import in.happiness.groceryapp.adaptar.CartsAdaptar;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.Vendor;
import in.happiness.groceryapp.utils.AppConstant;

import in.happiness.groceryapp.utils.AppDBHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment {
    private View v;
    private LinearLayout llPayment, llTopMenu, llBack, llClose;
    private RadioGroup onlineRg, cashRg, walletRg;
    private RadioButton onlineRb, cashRb, walletRb;
    private TextView tvTotal, storeName;
    private EditText edNote;
    private String token, total, code, locationId, pType=null;
    private boolean valid;
    private ProgressDialog progressDialog;
    private int vendor_id;
    private Vendor vendor;
    private CartsAdaptar cartAdaptar;
    private RecyclerView.LayoutManager layoutManagerCart;
    private RecyclerView rvCart;
    private AppDBHelper dbHelper;

    public PaymentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_payment, container, false);
        init();
        dbHelper = new AppDBHelper(getContext());
        token=AppConstant.userToken;
        vendor_id = AppConstant.cart_item.get(0).getVendor_id();
        getVendorDetails(String.valueOf(vendor_id));
        if (getArguments()!=null) {
            total=(String) getArguments().get("total");
            locationId=(String) getArguments().get("locationId");
            if (getArguments().containsKey("code")) {
                code=(String) getArguments().get("code");
                valid=true;
            } else {
                code="";
                valid=false;
            }
        }
        llTopMenu.setVisibility(View.GONE);
//        generateToken(request_id,total,"INR");
        setListener();
        return v;
    }

    private void getVendorDetails(String vendorId) {
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
                        }
                        if (AppConstant.cart_item.size()>0) {
                            cartAdaptar = new CartsAdaptar(AppConstant.cart_item, getActivity());
                            layoutManagerCart = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            rvCart.setLayoutManager(layoutManagerCart);
                            rvCart.setItemAnimator(new DefaultItemAnimator());
                            rvCart.setAdapter(cartAdaptar);
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
            public void onFailure(Call<Vendor> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setListener() {
        tvTotal.setText(total);
        llPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pType!=null && pType.equals("cash")) {
                    confirmOrder(pType, locationId, code);
                } else if (pType!=null && pType.equals("online")) {
                    confirmOrder(pType, locationId, code);
                }
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CartFragment(),total, locationId, valid, code);
            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CartFragment(),total, locationId, valid, code);
            }
        });
        onlineRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashRg.clearCheck();
                walletRg.clearCheck();
                pType="online";
            }
        });
        cashRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlineRg.clearCheck();
                walletRg.clearCheck();
                pType="cash";
            }
        });
        walletRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashRg.clearCheck();
                onlineRg.clearCheck();
                pType="wallet";
            }
        });
    }

    private void loadFragment(Fragment fragment, String total, String locationId, boolean valid, String code) {
        Bundle bundle = new Bundle();
        bundle.putString("total",total);
        if (valid) {
            bundle.putString("code",code);
            bundle.putBoolean("valid", true);
        } else {
            bundle.putBoolean("valid", false);
        }
        bundle.putString("locationId", locationId);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void confirmOrder(String paymentType, String locationId, String code) {
        if (edNote.getText().toString()==null) {
            edNote.setText("");
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody rPaymentType = RequestBody.create(MediaType.parse("text/plain"), paymentType);
        RequestBody rNote = RequestBody.create(MediaType.parse("text/plain"), edNote.getText().toString());
        RequestBody rLocationId = RequestBody.create(MediaType.parse("text/plain"), locationId);
        RequestBody rCode = RequestBody.create(MediaType.parse("text/plain"), code);
        Call<OTP> call = RetrofitClient.getApiInterface().placeOrder("Bearer "+token, rPaymentType, rLocationId, rCode, rNote);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        dbHelper.truncateCart();
                        if (response.body().isStatus()) {
                            if (paymentType.equals("online")) {
                                startActivity(new Intent(getActivity(), DoPaymentActivity.class).putExtra("request_id", response.body().getOrder_id())
                                        .putExtra("amount", response.body().getOrder_amount()).putExtra("token", token));
                                tvTotal.setText("");
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                String order_id = response.body().getOrder_id();
                                loadFragment(new FinalOrderFragment(), order_id);
                            }
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            loadFragment(new MainFragment());
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

    private void loadFragment(Fragment fragment, String order_id) {
        Bundle bundle = new Bundle();
        bundle.putString("order_id",order_id);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
        llPayment = v.findViewById(R.id.llPayment);
        llBack = v.findViewById(R.id.llBack);
        llClose = v.findViewById(R.id.llClose);
        onlineRg = v.findViewById(R.id.onlineRg);
        cashRg = v.findViewById(R.id.cashRg);
        walletRg = v.findViewById(R.id.walletRg);
        onlineRb = v.findViewById(R.id.onlineRb);
        cashRb = v.findViewById(R.id.cashRb);
        walletRb = v.findViewById(R.id.walletRb);
        tvTotal = v.findViewById(R.id.tvTotal);
        edNote = v.findViewById(R.id.edNote);
        storeName = v.findViewById(R.id.storeName);
        rvCart = v.findViewById(R.id.rvCart);
    }
}