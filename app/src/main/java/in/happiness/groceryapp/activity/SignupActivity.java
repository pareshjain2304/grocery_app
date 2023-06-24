package in.happiness.groceryapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.MainData;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    LinearLayout llLoginBtn;
    EditText signUpName, signUpEmail, signUpRefCode;
    AppSharedPreferences sharedPreferences;
    String token;
    private ProgressDialog progressDialog;
    private AppDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sharedPreferences = new AppSharedPreferences(SignupActivity.this);
        init();
        if (AppConstant.validate) {
            MainData md = AppConstant.response;
            dbHelper = new AppDBHelper(this);
            if (md.getCategories().size()>0) {
                for (Category c:md.getCategories()) {
                    dbHelper.addCategory(c);
                }
            }
            if (md.getSubcategories().size()>0) {
                for (SubCategory c:md.getSubcategories()) {
                    dbHelper.addSubCategory(c);
                }
            }
            if (md.getChildcategories().size()>0) {
                for (Category c:md.getChildcategories()) {
                    dbHelper.addChildCategory(c);
                }
            }
            if (md.getBrands().size()>0) {
                for (Category c:md.getBrands()) {
                    dbHelper.addBrands(c);
                }
            }
            AppConstant.validate=false;
        }
        token=sharedPreferences.doGetFromSharedPreferences(AppConstant.token);
        setListener();
    }

    private void setListener() {
        llLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(signUpName, "name") && validateName(signUpName) && validate(signUpEmail, "email address")
                        && validateEmail(signUpEmail)) {
                    userSignUp();
                }
            }
        });
    }

    private boolean validateEmail(EditText signUpEmail) {
        String emailAddress = signUpEmail.getText().toString().trim();
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!emailAddress.matches(regex)) {
            signUpEmail.setError("It should be valid email address");
            return false;
        };
        return true;
    }

    private boolean validateName(EditText signUpName) {
        String fullName = signUpName.getText().toString().trim();
        boolean valid = true;
        String[] splited = fullName.split("\\s+");
        for (String s: splited) {
            if (!s.matches("^[a-zA-Z]*$")) {
                valid = false;
                signUpName.setError("It should be name.");
            };
        }
        return valid;
    }

    private void userSignUp() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), signUpName.getText().toString());
        RequestBody userEmail = RequestBody.create(MediaType.parse("text/plain"), signUpEmail.getText().toString());
        RequestBody referenceCode = RequestBody.create(MediaType.parse("text/plain"), signUpRefCode.getText().toString());
        Call<OTP> call = RetrofitClient.getApiInterface().userSignUp("Bearer "+token, userName, userEmail, referenceCode);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        OTP respMsg = response.body();
                        if (respMsg.isStatus()) {
                            getUserDetails(token);
                        } else {
                            Toast.makeText(SignupActivity.this, respMsg.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignupActivity.this, String.valueOf(response.errorBody()), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                Log.i("onFailure", t.getMessage());
                Toast.makeText(SignupActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
            }
        });
    }

    private void getUserDetails(String token) {
        dbHelper = new AppDBHelper(this);
        Call<User> call = RetrofitClient.getApiInterface().getUserDetails("Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.code() == 200) {
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.isLoggedIn, "true");
                        AppConstant.user = response.body();
                        dbHelper.addUser(response.body(), token);
                        Intent verifyOTP = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(verifyOTP);
                    } else {
                        Toast.makeText(SignupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(SignupActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private  void init() {
        llLoginBtn=findViewById(R.id.llLoginBtn);
        signUpName=findViewById(R.id.signUpName);
        signUpEmail=findViewById(R.id.signUpEmail);
        signUpRefCode=findViewById(R.id.signUpRefCode);
    }

    private boolean validate(EditText editText, String type) {
        String value = editText.getText().toString();
        if (value.equals("") || value.isEmpty() || value==null) {
            Toast.makeText(this, "please enter "+type, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}