package in.happiness.groceryapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.Transaction;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppSharedPreferences;
import in.happiness.groceryapp.utils.NetworkConnection;


public class WalletDetailsFragment extends Fragment {

    private View v;
    private Bundle bundle;
    private SwipeRefreshLayout swipeRefresh;
    private String flag,request_id,token;
    private LinearLayout llBack,llHelp,llNoNetwork,llRefresh,llHistoryDetails;
    private AppSharedPreferences sharedPreferences;
    private TextView tvStatus, tvTransactionType, tvMsg, tvUserName, tvTotalAmnt, tvReqId;
    private Transaction walletTransaction;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_wallet_details, container, false);
        sharedPreferences = new AppSharedPreferences(getActivity());
        walletTransaction = new Transaction();
        init();

        //     token = sharedPreferences.doGetFromSharedPreferences(AppConstant.userToken);
        token= AppConstant.token;

        bundle = this.getArguments();
        if (bundle != null) {
            flag = bundle.getString("Flag",null);
            walletTransaction = (Transaction) bundle.getSerializable("wTransaction");
            user = AppConstant.user;
            tvUserName.setText(user.getName());
            tvTotalAmnt.setText(walletTransaction.getAmount());
            tvStatus.setText(walletTransaction.getAmount());
            tvReqId.setText(walletTransaction.getId());
            if ( "debited".equals(walletTransaction.getType())){
                tvTransactionType.setText("Debited From");
            } else if ( "credited".equals(walletTransaction.getType())){
                tvTransactionType.setText("Credited To");
            }
        }



        if(!NetworkConnection.isNetworkAvailable(getActivity())){
            llNoNetwork.setVisibility(View.VISIBLE);
            llHistoryDetails.setVisibility(View.GONE);
        }else {
            llNoNetwork.setVisibility(View.GONE);
            llHistoryDetails.setVisibility(View.VISIBLE);
            //  getRequestDetails();
        }

        setListener();

        onBackPressed();


        return v;
    }

    private void init(){
        llHistoryDetails = (LinearLayout) v.findViewById(R.id.llHistoryDetails);
        llRefresh = (LinearLayout) v.findViewById(R.id.llRefresh);
        llNoNetwork = (LinearLayout) v.findViewById(R.id.llNoNetwork);
        llBack = (LinearLayout) v.findViewById(R.id.llBack);
        llHelp = (LinearLayout) v.findViewById(R.id.llHelp);
        swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
        tvStatus = (TextView) v.findViewById(R.id.tvStatus);
        tvTransactionType = (TextView) v.findViewById(R.id.tvTransactionType);
        tvMsg = (TextView) v.findViewById(R.id.tvMsg);
        tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvTotalAmnt  = (TextView) v.findViewById(R.id.tvTotalAmnt);
        tvReqId = (TextView) v.findViewById(R.id.tvReqId);
    }

    private void onBackPressed(){
        //You need to add the following line for this solution to work; thanks skayred
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    // loadFragment(new MainFragment());/

                    return true;
                }
                return false;
            }
        } );
    }

    private void setListener(){

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!NetworkConnection.isNetworkAvailable(getActivity())){
                    llNoNetwork.setVisibility(View.VISIBLE);
                    llHistoryDetails.setVisibility(View.GONE);
                }else {
                    llNoNetwork.setVisibility(View.GONE);
                    llHistoryDetails.setVisibility(View.VISIBLE);
                    //  loadFragment(new HistoryDetailsFragment());
                    //getRequestDetails();
                }
                swipeRefresh.setRefreshing(false);
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        llHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag.equals("MyPoints"))
                    loadFragment(new NeedHelpFragment());
                else
                    loadFragment(new NeedHelpFragment());
            }
        });

        llRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkConnection.isNetworkAvailable(getActivity())){
                    llNoNetwork.setVisibility(View.VISIBLE);
                    llHistoryDetails.setVisibility(View.GONE);
                }else {
                    llNoNetwork.setVisibility(View.GONE);
                    llHistoryDetails.setVisibility(View.VISIBLE);
                    //  loadFragment(new HistoryDetailsFragment());
                    //getRequestDetails();
                }
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

}