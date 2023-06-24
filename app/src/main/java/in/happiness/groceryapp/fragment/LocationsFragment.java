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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.adaptar.LocationsAdaptar;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.Data;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsFragment extends Fragment implements LocationsAdaptar.ItemClickListener {
    private View view;
    private String token;
    private LinearLayout llClose, llBack;
    private RecyclerView rvLocations;
    private LocationsAdaptar locationsAdaptar;
    private RecyclerView.LayoutManager layoutManagerLocation;
    private ArrayList<Data> userLocation;
    private ProgressDialog progressDialog;
    private TextView tvAddLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_locations, container, false);
        token= AppConstant.userToken;
        init();
        setListener();
        getUserLocation();
        return view;
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
                            userLocation = location.getData();
                            setLocations(userLocation);
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

    private void setLocations(ArrayList<Data> userLocation) {
        locationsAdaptar = new LocationsAdaptar(userLocation, getActivity(), this);
        layoutManagerLocation = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvLocations.setLayoutManager(layoutManagerLocation);
        rvLocations.setItemAnimator(new DefaultItemAnimator());
        rvLocations.setAdapter(locationsAdaptar);
    }

    private void setListener() {
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new UserProfileFragment());
            }
        });
        tvAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new EditLocationFragment(),"addLocation");
            }
        });
    }

    private void loadFragment(Fragment fragment, String message) {
        Bundle bundle = new Bundle();
        bundle.putString("message",message);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void init() {
        llClose=view.findViewById(R.id.llClose);
        llBack=view.findViewById(R.id.llBack);
        rvLocations=view.findViewById(R.id.rvLocations);
        tvAddLocation=view.findViewById(R.id.tvAddLocation);
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
    public void editLocation(int position) {
        loadFragment(new EditLocationFragment(), userLocation.get(position));
    }

    private void loadFragment(Fragment fragment, Data data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("location", data);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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
                        loadFragment(new LocationsFragment());
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