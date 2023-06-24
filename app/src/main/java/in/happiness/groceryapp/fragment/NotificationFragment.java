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
import android.widget.Toast;

import in.happiness.groceryapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import in.happiness.groceryapp.adaptar.NotificationAdaptar;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.NotificationBody;
import in.happiness.groceryapp.model.Notifications;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    View view;
    RecyclerView recylcerViewNotification;
    RecyclerView.LayoutManager layoutManager;
    NotificationAdaptar notificationAdaptar;
    LinearLayout llClose, llBack, llTopMenu;
    String token;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        init();
        token = AppConstant.userToken;
        llTopMenu.setVisibility(View.GONE);
        setListener();
        getNotification();
        return view;
    }

    private void getNotification() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<Notifications> call = RetrofitClient.getApiInterface().getNotifications("Bearer "+token);
        call.enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        ArrayList<NotificationBody> notifications = response.body().getData();
                        if (notifications.size()>0) {
                            notificationAdaptar = new NotificationAdaptar(notifications,getActivity());
                            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            // gridLayoutManager=new GridLayoutManager(getActivity(),3);
                            recylcerViewNotification.setLayoutManager(layoutManager);
                            recylcerViewNotification.setItemAnimator(new DefaultItemAnimator());
                            recylcerViewNotification.setAdapter(notificationAdaptar);
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
            public void onFailure(Call<Notifications> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setListener() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MainFragment());
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
        llBack = view.findViewById(R.id.llBack);
        llClose = view.findViewById(R.id.llClose);
        recylcerViewNotification = view.findViewById(R.id.recylcerViewNotification);
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
    }
}