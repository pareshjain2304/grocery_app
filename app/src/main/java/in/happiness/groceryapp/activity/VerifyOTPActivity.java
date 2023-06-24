package in.happiness.groceryapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.MainData;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.receiver.SmsBroadcastReceiver;
import in.happiness.groceryapp.receiver.SmsBroadcastReceiverListener;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;
import in.happiness.groceryapp.utils.GenericTextWatcher;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity implements SmsBroadcastReceiverListener {
    LinearLayout llLoginBtn;
    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four;
    String mobile_number, otp;
    AppSharedPreferences sharedPreferences;
    private TextView txtMobileNumber, tvTimer, tvResend;
    private ProgressDialog progressDialog;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private static final int REQ_USER_CONSENT = 200;
    private AppDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        sharedPreferences = new AppSharedPreferences(VerifyOTPActivity.this);
        Intent i = getIntent();
        mobile_number = (String) i.getExtras().get("mobileNumber");
        otp = (String) i.getExtras().get("otp");
        init();
        txtMobileNumber.setText("+ 91 " + mobile_number);
        startTimer(1);
        startSmartUserConsent();
        setListener();

    }

    private void startSmartUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        Pattern otpPattern = Pattern.compile("(\\d{4})");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()) {
            String otp1 = matcher.group(0);
            otp_textbox_one.setText(otp1.substring(0, 1));
            otp_textbox_two.setText(otp1.substring(1, 2));
            otp_textbox_three.setText(otp1.substring(2, 3));
            otp_textbox_four.setText(otp1.substring(3, 4));
            verifyOTP();
        }
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {
                someActivityResultLauncher.launch(intent);
            }

            @Override
            public void onFailure() {
            }
        };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    // There are no request codes
                    String message = result.getData().getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    getOtpFromMessage(message);
                }
            });


    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

    @Override
    public void onSuccess(Intent intent) {

    }

    @Override
    public void onFailure() {

    }

    private void startTimer(int noOfMinutes) {
        tvResend.setVisibility(View.GONE);

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tvTimer.setText("You can resend your OTP within (" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds) + " )");
            }

            public void onFinish() {
                tvTimer.setText("Time Out!");
                tvResend.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void setListener() {
        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four};
        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(otp_textbox_one, edit));
        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(otp_textbox_two, edit));
        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(otp_textbox_three, edit));
        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(otp_textbox_four, edit));
        llLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(otp_textbox_one, "otp") && validate(otp_textbox_one, "otp") &&
                        validate(otp_textbox_one, "otp") && validate(otp_textbox_one, "otp")) {
                    verifyOTP();
                }
            }
        });
        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOTP();
            }
        });
    }

    private void getOTP() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody mobileNumber = RequestBody.create(MediaType.parse("text/plain"), mobile_number);
        Call<OTP> call = RetrofitClient.getApiInterface().getOTP(mobileNumber);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        OTP respMsg = response.body();
                        otp = String.valueOf(respMsg.getOtp());
                        startSmartUserConsent();
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.isNewUser, String.valueOf(respMsg.isNewUser()));
                        startTimer(1);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(VerifyOTPActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(VerifyOTPActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                Log.i("onFailure", t.getMessage());
                Toast.makeText(VerifyOTPActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
            }
        });
    }

    private void verifyOTP() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        String otp = otp_textbox_one.getText().toString() + otp_textbox_two.getText().toString() + otp_textbox_three.getText().toString()
                + otp_textbox_four.getText().toString();
        RequestBody mobileNumber = RequestBody.create(MediaType.parse("text/plain"), mobile_number);
        RequestBody userOtp = RequestBody.create(MediaType.parse("text/plain"), otp);
        Call<OTP> call = RetrofitClient.getApiInterface().verifyOTP(mobileNumber, userOtp);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        OTP respMsg = response.body();
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.token, respMsg.getToken());
                        Log.e("tokenKey", "=" + respMsg.getToken());
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.firstTimeLaunch, "true");
                        String isNewUser = sharedPreferences.doGetFromSharedPreferences(AppConstant.isNewUser);
                        Toast.makeText(VerifyOTPActivity.this, respMsg.getMessage(), Toast.LENGTH_SHORT).show();
                        getData(respMsg.getToken());
                        if (isNewUser.equals("true")) {
                            Intent verifyOTP = new Intent(VerifyOTPActivity.this, SignupActivity.class);
                            startActivity(verifyOTP);
                        } else {
                            getUserDetails(respMsg.getToken(), mobile_number);
                        }
                    } else if (response.code() == 404) {
                        progressDialog.dismiss();
                        Toast.makeText(VerifyOTPActivity.this, "OTP is not verified", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(VerifyOTPActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(VerifyOTPActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                Log.i("onFailure", t.getMessage());
                Toast.makeText(VerifyOTPActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
            }
        });
    }

    private void getData(String token) {
        dbHelper = new AppDBHelper(this);
        RequestBody last_date = RequestBody.create(MediaType.parse("text/plain"), "");
        Call<MainData> call = RetrofitClient.getApiInterface().getData("Bearer " + token, last_date);
        call.enqueue(new Callback<MainData>() {
            @Override
            public void onResponse(Call<MainData> call, Response<MainData> response) {
                try {
                    if (response.code() == 200) {
                        Date date = new Date();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, -1);
                        Date d = calendar.getTime();
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.lastDate, sdf1.format(date));
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.lastSynchDate, sdf2.format(d));
                        AppConstant.response = response.body();
                        AppConstant.validate = true;
                        dbHelper.truncateData();
                    } else {
                        Toast.makeText(VerifyOTPActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(VerifyOTPActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainData> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(VerifyOTPActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserDetails(String token, String mobile_number) {
        dbHelper = new AppDBHelper(this);
        User user = dbHelper.getUser(token, mobile_number);
        if (user != null) {
            sharedPreferences.doSaveToSharedPreferences(AppConstant.isLoggedIn, "true");
            AppConstant.user = user;
            Intent verifyOTP = new Intent(VerifyOTPActivity.this, MainActivity.class);
            startActivity(verifyOTP);
        } else {
            Call<User> call = RetrofitClient.getApiInterface().getUserDetails("Bearer " + token);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    try {
                        if (response.code() == 200) {
                            sharedPreferences.doSaveToSharedPreferences(AppConstant.isLoggedIn, "true");
                            Date date = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            sharedPreferences.doSaveToSharedPreferences(AppConstant.lastDate, sdf.format(date));
                            AppConstant.user = response.body();
                            dbHelper.addUser(response.body(), token);
                            Intent verifyOTP = new Intent(VerifyOTPActivity.this, MainActivity.class);
                            startActivity(verifyOTP);
                        } else {
                            Toast.makeText(VerifyOTPActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.i("error", e.toString());
                        Toast.makeText(VerifyOTPActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("onFailure", t.getMessage());
                    Toast.makeText(VerifyOTPActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void init() {
        llLoginBtn = findViewById(R.id.llLoginBtn);
        otp_textbox_one = findViewById(R.id.otp_edit_box1);
        otp_textbox_two = findViewById(R.id.otp_edit_box2);
        otp_textbox_three = findViewById(R.id.otp_edit_box3);
        otp_textbox_four = findViewById(R.id.otp_edit_box4);
        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        tvTimer = findViewById(R.id.tvTimer);
        tvResend = findViewById(R.id.tvResend);
    }

    private boolean validate(EditText editText, String type) {
        String value = editText.getText().toString();
        if (value.equals("") || value.isEmpty() || value == null) {
            Toast.makeText(this, "please enter " + type, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}