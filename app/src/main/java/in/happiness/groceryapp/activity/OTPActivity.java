package in.happiness.groceryapp.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import in.happiness.groceryapp.R;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class OTPActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    LinearLayout llLoginBtn;
    EditText edtMobile;
    AppSharedPreferences sharedPreferences;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private String[] permissions;
    private boolean resultBool;
    private GoogleApiClient googleApiClient;
    private static final int RC_HINT = 1000;
    private ProgressDialog progressDialog;
    private ArrayList<Category> categories;
    private AppDBHelper dbHelper;
    private ArrayList<SubCategory> subCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        dbHelper = new AppDBHelper(this);
        sharedPreferences = new AppSharedPreferences(OTPActivity.this);
        init();
        checkForPermission();
        getCategories();
        //set google api client for hint request
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).enableAutoManage(this, this).addApi(Auth.CREDENTIALS_API).build();
        requestHint();
        setListener();
    }

    public void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build();
        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);
        IntentSenderRequest.Builder intentSenderRequest = new IntentSenderRequest.Builder(intent.getIntentSender());
        hintLauncher.launch(intentSenderRequest.build());
    }


    ActivityResultLauncher<IntentSenderRequest> hintLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
        if (result != null && result.getData() != null) {
            Intent data = result.getData();
            Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
            // credential.getId();  <-- will need to process phone number string
            if (credential!=null) {
                String number = "";
                number = credential.getId();
                edtMobile.setText(number.replace("+91", ""));
            }
        }
    });

    private void setListener() {
        llLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(edtMobile, "mobile number")) {
                    getOTP();
                }
            }
        });
    }

    private void checkForPermission() {
        permissions = new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private ArrayList<String> findUnAskedPermissions(String[] wanted) {
        ArrayList<String> result = new ArrayList<String>();
        boolean valid = true;
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            } else {
                resultBool = true;
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel(getString(R.string.mandatoryPermission), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                    }
                                }
                            });
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this).setMessage(message).setPositiveButton(getString(R.string.ok), okListener).setNegativeButton(getString(R.string.cancel), null).create().show();
    }

    private void getCategories() {
        Call<ArrayList<Category>> call = RetrofitClient.getApiInterface().getCategories();
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                try {
                    if (response.code() == 200) {
                        categories = response.body();
                        if (categories.size() > 0) {
                            loadCategories(categories);
                        }
                    } else {
                        Toast.makeText(OTPActivity.this, String.valueOf(response.errorBody()), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(OTPActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                Log.i("onFailure", t.getMessage());
                Toast.makeText(OTPActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
            }
        });

    }

    private void loadCategories(ArrayList<Category> categories) {
        for (Category c : categories) {
            dbHelper.addCategory(c);
            ArrayList<SubCategory> sc = getSubCategories(c.getId());
            for (SubCategory s : sc) {
                dbHelper.addSubCategory(s);
            }
        }
    }

    private ArrayList<SubCategory> getSubCategories(int categoryId) {
        subCategories = new ArrayList<>();
        Call<ArrayList<SubCategory>> call = RetrofitClient.getApiInterface().getSubCategories(categoryId);
        call.enqueue(new Callback<ArrayList<SubCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<SubCategory>> call, Response<ArrayList<SubCategory>> response) {
                try {
                    if (response.code() == 200) {
                        subCategories = response.body();
                    } else {
                        Toast.makeText(OTPActivity.this, String.valueOf(response.errorBody()), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(OTPActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SubCategory>> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(OTPActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return subCategories;
    }


    private void getOTP() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody mobileNumber = RequestBody.create(MediaType.parse("text/plain"), edtMobile.getText().toString());
        Call<OTP> call = RetrofitClient.getApiInterface().getOTP(mobileNumber);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        OTP respMsg = response.body();
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.isNewUser, String.valueOf(respMsg.isNewUser()));
                        Intent verifyOTP = new Intent(OTPActivity.this, VerifyOTPActivity.class);
                        verifyOTP.putExtra("mobileNumber", edtMobile.getText().toString());
                        verifyOTP.putExtra("otp", String.valueOf(response.body().getOtp()));
                        startActivity(verifyOTP);
                        Toast.makeText(OTPActivity.this, respMsg.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(OTPActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(OTPActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                Log.i("onFailure", t.getMessage());
                Toast.makeText(OTPActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
            }
        });
    }

    private void init() {
        llLoginBtn = findViewById(R.id.llLoginBtn);
        edtMobile = findViewById(R.id.edtMobile);
    }

    private boolean validate(EditText editText, String type) {
        String value = editText.getText().toString();
        if (value.equals("") || value.isEmpty() || value == null) {
            Toast.makeText(this, "please enter " + type, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}