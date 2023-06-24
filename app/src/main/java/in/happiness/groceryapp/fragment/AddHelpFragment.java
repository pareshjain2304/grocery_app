package in.happiness.groceryapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.NetworkConnection;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHelpFragment extends Fragment {
    private LinearLayout llBack;
    private View view;
    private EditText edtDescription;
    private RadioGroup rgIssues;
    private RadioButton rbPayments;
    private TextView tvAddComplaint;
    private ProgressDialog progressDialog;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_add_help, container, false);
        token= AppConstant.userToken;
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
        rgIssues.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // get selected radio button from radioGroup
                int selectedId = group.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rbPayments = view.findViewById(selectedId);
            }
        });
        tvAddComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtDescription.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please Add Description", Toast.LENGTH_LONG).show();
                }else if(rgIssues.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getActivity(),"Please select Issue", Toast.LENGTH_LONG).show();
                }else {
                    if(!NetworkConnection.isNetworkAvailable(getActivity())){
                        Toast.makeText(getActivity(),"No Network Available, Please Check", Toast.LENGTH_LONG).show();
                    }else {
                        addHelp();
                    }
                }
            }
        });
    }

    private void addHelp() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"),rbPayments.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"),edtDescription.getText().toString());
        Call<OTP> call = RetrofitClient.getApiInterface().generateTickets("Bearer "+token,title,description);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                OTP respMsg = response.body();
                if (respMsg.isStatus()) {
                    progressDialog.dismiss();
                    loadFragment(new NeedHelpFragment());
                    Toast.makeText(getActivity(),"Submitted Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(),"Submit Failed", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
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
        edtDescription=view.findViewById(R.id.edtDescription);
        tvAddComplaint=view.findViewById(R.id.tvAddComplaint);
        rgIssues=view.findViewById(R.id.rgIssues);
    }
}