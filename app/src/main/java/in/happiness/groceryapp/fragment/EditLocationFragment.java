package in.happiness.groceryapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.activity.LocationActivity;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.Data;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.UserLocation;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppSharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLocationFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View view;
    Spinner saveAs;
    EditText edLocation, edLandMark, edContactNumber, edContactPerson;
    LinearLayout llBack, llClose;
    TextView tvLocation, tvSave;
    String[] types = new String[]{"Home","Office","Others"};
    private String type, token, total, locationId, code, addLocation;
    private AppSharedPreferences sharedPreferences;
    private Data location;
    private int post;
    private boolean valid;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_edit_location, container, false);
        sharedPreferences=new AppSharedPreferences(getActivity());
        token=AppConstant.userToken;
        init();
        if (getArguments()!=null) {
            if (getArguments().containsKey("mainScreen")) {
                location=new Data();
                UserLocation location1= (UserLocation) getArguments().get("userLocationDetails");
                locationId=(String) getArguments().get("locationId");
                addLocation = (String) getArguments().get("addLocation");
                if (addLocation.equals("no")) {
                    location.setId(Integer.valueOf(locationId));
                } else {
                    tvSave.setText("Save");
                }
                location.setLandmark(location1.getLandMark());
                location.setFullname(location1.getContactPerson());
                location.setPhone(location1.getContactNumber());
                String userAddress = sharedPreferences.doGetFromSharedPreferences(AppConstant.userAddress);
                if (userAddress!=null) {
                    location.setAddress(sharedPreferences.doGetFromSharedPreferences(AppConstant.userAddress));
                }
                location.setTitle(location1.getType());
                assignLocation(location);
            } else if (getArguments().containsKey("message")) {
                tvSave.setText("Save");
            }
            if (getArguments().containsKey("location")){
                location= (Data) getArguments().get("location");
                assignLocation(location);
            }
        }
        setListener();
        return view;
    }

    private void assignLocation(Data location) {
        for (int i=0;i<types.length; i++) {
            if (types[i].equals(location.getTitle())) {
                saveAs.setSelection(i);
            }
        }
        edContactNumber.setText(location.getPhone());
        edContactPerson.setText(location.getFullname());
        if (location.getLandmark()!=null) {
            edLandMark.setText(location.getLandmark());
        }
        edLocation.setText(location.getAddress());
    }

    private void setListener() {
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLocation userLocationDetails = new UserLocation();
                userLocationDetails.setLandMark(edLandMark.getText().toString());
                userLocationDetails.setType(type);
                userLocationDetails.setContactNumber(edContactNumber.getText().toString());
                userLocationDetails.setContactPerson(edContactPerson.getText().toString());
                userLocationDetails.setLocationType(post);
                userLocationDetails.setValid(valid);
                if (valid) {
                    userLocationDetails.setcCode(code);
                }
                userLocationDetails.setTotal(total);
                userLocationDetails.setLocationId(locationId);
                if (tvSave.getText().toString().equals("Save")) {
                    getActivity().startActivity(new Intent(getActivity(), LocationActivity.class)
                            .putExtra("userLocationDetails", userLocationDetails).putExtra("location", "editLocation").putExtra("addLocation","yes"));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LocationActivity.class)
                            .putExtra("userLocationDetails", userLocationDetails).putExtra("location", "editLocation").putExtra("locationId", String.valueOf(location.getId())).putExtra("addLocation","no"));
                }
//                loadFragment(new AddLocationFragment());
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(edContactPerson, "contact person") && validateName(edContactPerson) &&
                        validate(edContactNumber, "contact number")  && validateMobile(edContactNumber) &&
                        validate(edLandMark, "land mark")  && validate(edLocation, "location") ) {
                    String locationAvailable = sharedPreferences.doGetFromSharedPreferences(AppConstant.locationAvailable);
                    if  (locationAvailable!=null && locationAvailable.equals("true")) {
                        if (tvSave.getText().toString().equals("Save")) {
                            saveLocation();
                        } else {
                            updateLocation();
                        }
                    }
                }
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LocationsFragment());
            }
        });
    }

    private boolean validateMobile(EditText edContactNumber) {
        String str = edContactNumber.getText().toString().trim();
        Pattern ptrn = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        //the matcher() method creates a matcher that will match the given input against this pattern
        Matcher match = ptrn.matcher(str);
        if (match.find() && match.group().equals(str)) {
            return true;
        } else {
            Toast.makeText(getActivity(), "Invalid mobile number.", Toast.LENGTH_SHORT).show();
            edContactNumber.setError("Invalid mobile number.");
            return false;
        }
    }

    private boolean validateName(EditText edContactPerson) {
        String fullName = edContactPerson.getText().toString().trim();
        boolean valid = true;
        String[] splited = fullName.split("\\s+");
        for (String s: splited) {
            if (!s.matches("^[a-zA-Z]*$")) {
                valid = false;
                edContactPerson.setError("It should be name.");
            };
        }
        return valid;
    }


    private void saveLocation() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody fullname = RequestBody.create(MediaType.parse("text/plain"), edContactPerson.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), edLocation.getText().toString());
        RequestBody landMark = RequestBody.create(MediaType.parse("text/plain"), edLandMark.getText().toString());
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userCity));
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"),  sharedPreferences.doGetFromSharedPreferences(AppConstant.userState));
        RequestBody zipcode = RequestBody.create(MediaType.parse("text/plain"),  sharedPreferences.doGetFromSharedPreferences(AppConstant.userPincode));
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), edContactNumber.getText().toString());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userLat));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userLong));
        Call<OTP> call = RetrofitClient.getApiInterface().addLocation("Bearer "+token, title, fullname, address, city, state, zipcode,
                phone, latitude, longitude, landMark);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        if (response.body().isStatus()) {
                            OTP respMsg = response.body();
                            Toast.makeText(getActivity(), respMsg.getMessage(), Toast.LENGTH_SHORT).show();
                            loadFragment(new LocationsFragment());
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

    private void updateLocation() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody locationId=RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.getId()));
        RequestBody fullname = RequestBody.create(MediaType.parse("text/plain"), edContactPerson.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), edLocation.getText().toString());
        RequestBody landmark = RequestBody.create(MediaType.parse("text/plain"), edLandMark.getText().toString());
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userCity));
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"),  sharedPreferences.doGetFromSharedPreferences(AppConstant.userState));
        RequestBody zipcode = RequestBody.create(MediaType.parse("text/plain"),  sharedPreferences.doGetFromSharedPreferences(AppConstant.userPincode));
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), edContactNumber.getText().toString());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userLat));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userLong));
        Call<OTP> call = RetrofitClient.getApiInterface().updateLocation("Bearer "+token,locationId, title, fullname, address, city, state, zipcode,
                phone, latitude, longitude, landmark);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        OTP respMsg = response.body();
                        if (respMsg.isStatus()) {
                            Toast.makeText(getActivity(), respMsg.getMessage(), Toast.LENGTH_SHORT).show();
                            loadFragment(new LocationsFragment());
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
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
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
        llBack=view.findViewById(R.id.llBack);
        llClose=view.findViewById(R.id.llClose);
        saveAs=view.findViewById(R.id.saveAs);
        tvLocation=view.findViewById(R.id.tvLocation);
        edLocation=view.findViewById(R.id.edLocation);
        edLocation.setEnabled(false);
        edLandMark=view.findViewById(R.id.edLandMark);
        edContactNumber=view.findViewById(R.id.edContactNumber);
        edContactPerson=view.findViewById(R.id.edContactPerson);
        saveAs.setOnItemSelectedListener(EditLocationFragment.this);
        tvSave=view.findViewById(R.id.tvSave);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,types);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        saveAs.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.saveAs:
                type = types[position];
                post = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}