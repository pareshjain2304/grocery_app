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
import in.happiness.groceryapp.adaptar.HelpListAdaptar;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.MyTickets;
import in.happiness.groceryapp.model.Tickets;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeedHelpFragment extends Fragment implements HelpListAdaptar.ItemClickListener{
    private View view;
    private LinearLayout llBack;
    private RecyclerView rvHelpList;
    private RecyclerView.LayoutManager layoutManagerLocation;
    private TextView tvAddComplaint, tvHelpMsg;
    private String token;
    private HelpListAdaptar helpAdaptar;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_need_help, container, false);
        token= AppConstant.userToken;
        init();
        setListener();
        getMyTickets();
        return view;
    }

    private void getMyTickets() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<MyTickets> call = RetrofitClient.getApiInterface().myTickets("Bearer "+token);
        call.enqueue(new Callback<MyTickets>() {
            @Override
            public void onResponse(Call<MyTickets> call, Response<MyTickets> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        ArrayList<Tickets> tickets = response.body().getData();
                        if (tickets.size()>0) {
                            tvHelpMsg.setVisibility(View.GONE);
                            setTickets(tickets);
                        }else {
                            rvHelpList.setVisibility(View.GONE);
                            tvHelpMsg.setText("No complaints raised by you.");
                            tvHelpMsg.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<MyTickets> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void setTickets(ArrayList<Tickets> tickets) {
        helpAdaptar = new HelpListAdaptar(tickets, getActivity(), this);
        layoutManagerLocation = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        // gridLayoutManager=new GridLayoutManager(getActivity(),3);
        rvHelpList.setLayoutManager(layoutManagerLocation);
        rvHelpList.setItemAnimator(new DefaultItemAnimator());
        rvHelpList.setAdapter(helpAdaptar);
    }

    private void setListener() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tvAddComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AddHelpFragment());
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
        rvHelpList=view.findViewById(R.id.rvHelpList);
        tvAddComplaint=view.findViewById(R.id.tvAddComplaint);
        tvHelpMsg=view.findViewById(R.id.tvHelpMsg);
    }
}