package in.happiness.groceryapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.cashfree.pg.CFPaymentService;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.GenerateToken;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppSharedPreferences;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_NOTIFY_URL;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;

public class DoPaymentActivity extends AppCompatActivity {
    String cashfree_token,request_id,amount,token;
    String customer_name,customer_email,customer_mobile;
    public static final String TAG="DoPaymentActivity";
    TextView tvPaymentProcess;
    AppSharedPreferences sharedPreferences;
    private User user;
    private  ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_payment);
        sharedPreferences = new AppSharedPreferences(DoPaymentActivity.this);

        tvPaymentProcess=findViewById(R.id.tvPaymentProcess);

        Intent intent=getIntent();

        token=intent.getStringExtra("token");
        request_id=intent.getStringExtra("request_id");
        amount=intent.getStringExtra("amount");
//        medicalName=intent.getStringExtra("medicalName");
        if (AppConstant.user!=null) {
            user = AppConstant.user;
            if (user.getName()!=null) {
                customer_name = user.getName();
            } else {
                customer_name = "Happiness India";
            }
            if (user.getEmail()!=null) {
                customer_email = user.getEmail();
            } else {
                customer_email = "care@thehappinessindia.com";
            }
            if (user.getMobile()!=null) {
                customer_mobile = user.getMobile();
            } else {
                customer_mobile = "9691214855";
            }
            generateToken(request_id, amount, "INR");
        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }

    }


    private void generateToken(String order_id, String order_amount,String order_currency) {
        progressDialog = new ProgressDialog(DoPaymentActivity.this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody orderId = RequestBody.create(MediaType.parse("text/plain"), order_id);
        RequestBody orderAmount= RequestBody.create(MediaType.parse("text/plain"), order_amount);
        Call<GenerateToken> call = RetrofitClient.getApiInterface().generateCashfreeToken("Bearer "+token, orderId, orderAmount);
        call.enqueue(new Callback<GenerateToken>() {
            @Override
            public void onResponse(Call<GenerateToken> call, Response<GenerateToken> response) {
                if (response.code()==200) {
                    progressDialog.dismiss();
                    GenerateToken resp = response.body();
                    if (resp.getStatus().equals("OK")) {
                        cashfree_token=response.body().getCftoken();
                        initiatePayment(cashfree_token);
                    } else {
                        Toast.makeText(DoPaymentActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(DoPaymentActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenerateToken> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        showCancelPaymentDialog();
        super.onBackPressed();
    }

    private void showCancelPaymentDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_cancel_payment);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(DoPaymentActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void initiatePayment(String cashfree_token){
        // String appId = "com.example.medprovider";
        String appId = "14349530f43b799bdad64d37ab594341"; //"3564782114746e1eec3192f2274653";
        String orderId = request_id;
        String orderAmount = amount;
        String orderNote = "User Order";
        String customerName = customer_name;
        String customerPhone = customer_mobile;
        String customerEmail = customer_email;
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        params.put(PARAM_NOTIFY_URL, "https://thehappinessindia.com/api/v1/payment-notify");

        // String token="Rb9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.hD0nI2UjNjRjZ3QWM1kjZ1IiOiQHbhN3XiwSNzkTO5EjNwYTM6ICc4VmIsIiUOlkI6ISej5WZyJXdDJXZkJ3biwCMwEjOiQnb19WbBJXZkJ3biwiIxADMwIXZkJ3TiojIklkclRmcvJye.XC-UNHcBLDkuhknlAFQc-psunnjZcdHMKq3j_xsrmt0HsxuRCoAHlPIlyUTMUJ3YBm";
        doPayment(params, cashfree_token, "PROD");
        // doPayment(params, this.cashfree_token, cashfree_token);
    }

    public  void  doPayment(Map<String, String> params, String token, String stage){
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        cfPaymentService.doPayment(DoPaymentActivity.this,params,token,stage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
//        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.d(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.d(TAG, key + " : " + bundle.getString(key));
                    }
                }
            String status;
            if (bundle.getString("txStatus")!=null && bundle.getString("txStatus").equalsIgnoreCase("SUCCESS")) {
                status="SUCCESS";
            } else {
                status="FAILED";
            }
            tvPaymentProcess.setText("Proceeding Payment... Please wait ");
//            Toast.makeText(this,status,Toast.LENGTH_LONG).show();
            savePaymentDetails(bundle.getString("paymentMode"),bundle.getString("referenceId"),
                    bundle.getString("txMsg"),status,"online");
        }
    }

    private void savePaymentDetails(String payment_mode, String reference_id, String message, String respStatus, String payment_type) {
        if (payment_mode!=null && reference_id!=null && message!=null) {
            progressDialog = new ProgressDialog(DoPaymentActivity.this);
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            RequestBody paymentToken = RequestBody.create(MediaType.parse("text/plain"), " ");
            RequestBody paymentMode = RequestBody.create(MediaType.parse("text/plain"), payment_mode);
            RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), request_id);
            RequestBody referenceId = RequestBody.create(MediaType.parse("text/plain"), reference_id);
            RequestBody Message = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody Amount = RequestBody.create(MediaType.parse("text/plain"), amount);
            RequestBody paymentStatus = RequestBody.create(MediaType.parse("text/plain"), respStatus);
            RequestBody paymentType = RequestBody.create(MediaType.parse("text/plain"), payment_type);
            Call<OTP> call = RetrofitClient.getApiInterface().savePaymentDetails("Bearer " + token, paymentToken, paymentMode, requestId,
                    referenceId, Message, Amount, paymentStatus, paymentType);
            call.enqueue(new Callback<OTP>() {
                @Override
                public void onResponse(Call<OTP> call, Response<OTP> response) {
                    if (response.body() != null) {
                        progressDialog.dismiss();
                        startActivity(new Intent(DoPaymentActivity.this, MainActivity.class).putExtra("orderId", request_id)
                                .putExtra("amount", amount).putExtra("status", respStatus));
                    }
                }
                @Override
                public void onFailure(Call<OTP> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        } else {
            startActivity(new Intent(DoPaymentActivity.this, MainActivity.class));
        }
    }


}