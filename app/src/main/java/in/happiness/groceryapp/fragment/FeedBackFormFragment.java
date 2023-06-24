package in.happiness.groceryapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.Order;
import in.happiness.groceryapp.model.OrderDetail;
import in.happiness.groceryapp.utils.AppConstant;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackFormFragment extends Fragment {
    private View view;
    private TextView tvSumbit;
    private EditText edDelFeedback, edVendorFeedback;
    private ImageView ivVendorVSad, ivVendorSad, ivVendorNeutral, ivVendorHappy, ivVendorVHappy;
    private ImageView ivDelVSad, ivDelSad, ivDelNeutral, ivDelHappy, ivDelVHappy;
    private String token, order_id, vendor_id,deliveryBoy_id, vendorRating="", deliveryBoyRating="";
    private ProgressDialog progressDialog;
    private boolean statusMsg=false;
    private LinearLayout llBack, llClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_feed_back_form, container, false);
        token= AppConstant.userToken;
        init();
        if (getArguments()!=null) {
            order_id=(String) getArguments().get("order_id");
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
                        Order order = response.body().getData();
                        if (order.getFeedbackAvailable().equals("0")) {
                            deliveryBoy_id = order.getDeliveryboyId();
                            vendor_id = order.getVendorId();
                        } else {
                            Toast.makeText(getActivity(), "Order feedback already saved.", Toast.LENGTH_SHORT).show();
                            loadFragment(new OrderDetailsFragment(), order_id);
                        }
                    } else {
                        progressDialog.dismiss();
                        loadFragment(new OrderDetailsFragment(), order_id);
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    loadFragment(new OrderDetailsFragment(), order_id);
                    Log.i("error", e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                progressDialog.dismiss();
                loadFragment(new OrderDetailsFragment(), order_id);
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setListener() {
        ivVendorHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorRatings();
                ivVendorHappy.setImageResource(R.drawable.happy_col);
                vendorRating="4";
            }
        });
        ivVendorVHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorRatings();
                ivVendorVHappy.setImageResource(R.drawable.very_happy_col);
                vendorRating="5";
            }
        });
        ivVendorSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorRatings();
                ivVendorSad.setImageResource(R.drawable.sad_col);
                vendorRating="2";
            }
        });
        ivVendorVSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorRatings();
                ivVendorVSad.setImageResource(R.drawable.ver_sad_col);
                vendorRating="1";
            }
        });
        ivVendorNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorRatings();
                ivVendorNeutral.setImageResource(R.drawable.neutral_col);
                vendorRating="3";
            }
        });
        ivDelHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryBoyRatings();
                ivDelHappy.setImageResource(R.drawable.happy_col);
                deliveryBoyRating="4";
            }
        });
        ivDelVHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryBoyRatings();
                ivDelVHappy.setImageResource(R.drawable.very_happy_col);
                deliveryBoyRating="5";
            }
        });
        ivDelSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryBoyRatings();
                ivDelSad.setImageResource(R.drawable.sad_col);
                deliveryBoyRating="2";
            }
        });
        ivDelVSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryBoyRatings();
                ivDelVSad.setImageResource(R.drawable.ver_sad_col);
                deliveryBoyRating="1";
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new OrderDetailsFragment(), order_id);
            }
        });
        ivDelNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryBoyRatings();
                ivDelNeutral.setImageResource(R.drawable.neutral_col);
                deliveryBoyRating="3";
            }
        });
        tvSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!vendorRating.equals("") && !deliveryBoyRating.equals("")) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    String venFeedback = edVendorFeedback.getText().toString();
                    if (venFeedback.equals("") || venFeedback.isEmpty() || venFeedback==null) {
                        venFeedback = "";
                    }
                    sumbitFeedback(order_id, vendor_id, vendorRating, venFeedback,"vendor");
                    String delFeedback = edDelFeedback.getText().toString();
                    if (delFeedback.equals("") || delFeedback.isEmpty() || delFeedback==null) {
                        delFeedback = "";
                    }
                    sumbitFeedback(order_id, deliveryBoy_id, deliveryBoyRating, delFeedback,"deliveryboy");
                } else if (vendorRating.equals("")) {
                    Toast.makeText(getActivity(), "Please select rating options for vendor.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please select rating options for delivery boy.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sumbitFeedback(String orderId,String userId,String userRating,String feedback, String userType) {
        RequestBody ordId = RequestBody.create(MediaType.parse("text/plain"), orderId);
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody rating = RequestBody.create(MediaType.parse("text/plain"), userRating);
        RequestBody user_feedback = RequestBody.create(MediaType.parse("text/plain"), feedback);
        Call<OTP> call;
        if (userType.equals("vendor")) {
            call = RetrofitClient.getApiInterface().submitVendorFeedback("Bearer "+token, ordId, user_id, rating, user_feedback);
        } else {
            call = RetrofitClient.getApiInterface().submitDeliveryBoyFeedback("Bearer "+token, ordId, user_id, rating, user_feedback);
        }
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body().isStatus()) {
                            if (userType.equals("deliveryboy")){
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                loadFragment(new OrderDetailsFragment(), order_id);
                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
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

    private void vendorRatings() {
        ivVendorHappy.setImageResource(R.drawable.happy);
        ivVendorVHappy.setImageResource(R.drawable.very_happy);
        ivVendorSad.setImageResource(R.drawable.sad);
        ivVendorVSad.setImageResource(R.drawable.ver_sad);
        ivVendorNeutral.setImageResource(R.drawable.neutral);
    }

    private void deliveryBoyRatings() {
        ivDelHappy.setImageResource(R.drawable.happy);
        ivDelVHappy.setImageResource(R.drawable.very_happy);
        ivDelSad.setImageResource(R.drawable.sad);
        ivDelVSad.setImageResource(R.drawable.ver_sad);
        ivDelNeutral.setImageResource(R.drawable.neutral);
    }

    private void init() {
        llClose = view.findViewById(R.id.llClose);
        llBack = view.findViewById(R.id.llBack);
        tvSumbit=view.findViewById(R.id.tvSumbit);
        edDelFeedback=view.findViewById(R.id.edDelFeedback);
        edVendorFeedback=view.findViewById(R.id.edVendorFeedback);
        ivVendorVSad=view.findViewById(R.id.ivVendorVSad);
        ivVendorSad=view.findViewById(R.id.ivVendorSad);
        ivVendorNeutral=view.findViewById(R.id.ivVendorNeutral);
        ivVendorHappy=view.findViewById(R.id.ivVendorHappy);
        ivVendorVHappy=view.findViewById(R.id.ivVendorVHappy);
        ivDelVSad=view.findViewById(R.id.ivDelVSad);
        ivDelSad=view.findViewById(R.id.ivDelSad);
        ivDelNeutral=view.findViewById(R.id.ivDelNeutral);
        ivDelHappy=view.findViewById(R.id.ivDelHappy);
        ivDelVHappy=view.findViewById(R.id.ivDelVHappy);
    }
}