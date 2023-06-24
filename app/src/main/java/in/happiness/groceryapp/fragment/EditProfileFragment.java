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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    private View view;
    private EditText edName, edMobNo, edEmail;
    private TextView tvUpdate;
    private User user;
    private LinearLayout llBack;
    private String token;
    private ProgressDialog progressDialog;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        token = AppConstant.userToken;
        user = AppConstant.user;
        init();
        setListener();
        return view;
    }

    private void setListener() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(edName, "name") && validateName(edName) &&
                        validate(edMobNo, "mobile number") && validateMobile(edMobNo)
                        && validate(edEmail, "email") && verifyEmail(edEmail)) {
                    updateProfile(edName.getText().toString(), edMobNo.getText().toString(), edEmail.getText().toString());
                }
            }
        });
    }

    private boolean validateMobile(EditText edMobNo) {
        String str = edMobNo.getText().toString().trim();
        Pattern ptrn = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        //the matcher() method creates a matcher that will match the given input against this pattern
        Matcher match = ptrn.matcher(str);
        if (match.find() && match.group().equals(str)) {
            return true;
        } else {
            Toast.makeText(getActivity(), "Invalid mobile number.", Toast.LENGTH_SHORT).show();
            edMobNo.setError("Invalid mobile number.");
            return false;
        }
    }

    private boolean verifyEmail(EditText edEmail) {
        String emailAddress = edEmail.getText().toString().trim();
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!emailAddress.matches(regex)) {
            edEmail.setError("It should be valid email address");
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

    private void updateProfile(String name, String mobile, String email) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody userMobile = RequestBody.create(MediaType.parse("text/plain"), mobile);
        RequestBody userEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        Call<OTP> call = RetrofitClient.getApiInterface().updateProfile("Bearer "+token, userName, userMobile, userEmail);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        if (response.body().isStatus()) {
                            getUserDetails();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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

    private boolean validate(EditText editText, String type) {
        String value = editText.getText().toString();
        if (value.equals("") || value.isEmpty() || value==null) {
            Toast.makeText(getActivity(), "please enter "+type, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getUserDetails() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<User> call = RetrofitClient.getApiInterface().getUserDetails("Bearer "+token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                        AppConstant.user=response.body();
                        loadFragment(new MainFragment());
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
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void init() {
        edName=view.findViewById(R.id.edName);
        edName.setText(user.getName());
        edMobNo=view.findViewById(R.id.edMobNo);
        edMobNo.setText(user.getMobile());
        edEmail=view.findViewById(R.id.edEmail);
        edEmail.setText(user.getEmail());
        tvUpdate=view.findViewById(R.id.tvUpdate);
        llBack=view.findViewById(R.id.llBack);
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
}