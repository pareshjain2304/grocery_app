package in.happiness.groceryapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.activity.DoPaymentActivity;
import in.happiness.groceryapp.utils.AppConstant;


public class FinalOrderFragment extends Fragment {
    private View v;
    private LinearLayout llBack, llClose, llTopMenu, llTrackStatus, llHome, llRetry;
    private String order_id, amount, status, token;
    private TextView tvOrderStatus, tvOrderMsg;
    private ImageView ivPayment;

    public FinalOrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_final_order, container, false);
        token= AppConstant.userToken;
        init();
        llTopMenu.setVisibility(View.GONE);
        if (getArguments()!=null && getArguments().containsKey("orderId")) {
            order_id=(String) getArguments().get("orderId");
            amount=(String) getArguments().get("amount");
            status=(String) getArguments().get("status");
            if (status.equals("FAILED")) {
                failureScenarion();
            }
        } else if (getArguments()!=null && getArguments().containsKey("order_id")) {
            order_id=(String) getArguments().get("order_id");
        }
        setListener();
        return v;
    }

    private void failureScenarion() {
        llTrackStatus.setVisibility(View.GONE);
        llRetry.setVisibility(View.VISIBLE);
        tvOrderStatus.setText("Order Failed");
        tvOrderMsg.setText("Your payment was not successful.");
        ivPayment.setImageResource(R.drawable.payment_failure);
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
        llTrackStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new TrackOrderFragment(), order_id);
            }
        });
        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MainFragment());
            }
        });
        llRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DoPaymentActivity.class).putExtra("request_id", order_id)
                        .putExtra("amount", amount).putExtra("token", token));
            }
        });
    }

    private void loadFragment(Fragment fragment, String order_id) {
        Bundle bundle = new Bundle();
        bundle.putString("order_id",order_id);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void init() {
        llBack = v.findViewById(R.id.llBack);
        llClose = v.findViewById(R.id.llClose);
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        llTrackStatus = v.findViewById(R.id.llTrackStatus);
        llHome = v.findViewById(R.id.llHome);
        llRetry = v.findViewById(R.id.llRetry);
        llRetry.setVisibility(View.GONE);
        tvOrderStatus = v.findViewById(R.id.tvOrderStatus);
        ivPayment = v.findViewById(R.id.ivPayment);
        tvOrderMsg = v.findViewById(R.id.tvOrderMsg);
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