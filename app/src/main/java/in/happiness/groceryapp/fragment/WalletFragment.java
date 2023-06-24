package in.happiness.groceryapp.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.adaptar.MyWalletAdapter;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.Transaction;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppSharedPreferences;
import in.happiness.groceryapp.utils.MonthYearPickerDialog;
import in.happiness.groceryapp.utils.NetworkConnection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletFragment extends Fragment {
    View v;
    private List<Transaction> myPointsList = new ArrayList<>();
    private RecyclerView rvWalletList;
    private MyWalletAdapter myPointsAdapter;
    RecyclerView.LayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    LinearLayout llTopMenu,llBack,llNoNetwork,llRefresh,llMyWallet;
    EditText spnMonth,spnStatus;
    ImageView ivNotification,ivHelp;
    SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
    SimpleDateFormat filter = new SimpleDateFormat("MM/yyyy");
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    String monthYearStr;
    SwipeRefreshLayout swipeRefresh;
    String token;
    AppSharedPreferences sharedPreferences;
    List<Transaction> walletTransactions, walletTransactions1;
    TextView tvCurrentBalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_wallet, container, false);
        sharedPreferences = new AppSharedPreferences(getActivity());

        //    token = sharedPreferences.doGetFromSharedPreferences(AppConstant.userToken);
        token= AppConstant.userToken;
        //    System.out.print("tokenforlogin"+token);


        init();
        getWalletDetails();

        llTopMenu.setVisibility(View.GONE);

        ///Add spinner data
        /*String[] arraySpinner = new String[] {
                "Month", "January", "February"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMonth.setAdapter(adapter);

        //*************Array status
        String[] arrayStatus = new String[] {
                "Status", "Credited", "Debited"
        };

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arrayStatus);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapterStatus);*/
        //*****************************

        if (!NetworkConnection.isNetworkAvailable(getActivity())) {
            llNoNetwork.setVisibility(View.VISIBLE);
            llMyWallet.setVisibility(View.GONE);
        } else {
            llNoNetwork.setVisibility(View.GONE);
            llMyWallet.setVisibility(View.VISIBLE);
        }
        setListener();
        return v;
    }

    private void getWalletDetails() {

        System.out.println("tokenid==="+token);
        Call<OTP> call= RetrofitClient.getApiInterface().userTransactions("Bearer "+token);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                OTP walletDetailsModel=response.body();
                if (response.code()==200 || response.code()==201){
                    if(walletDetailsModel.isStatus()) {
                        if (walletDetailsModel.getTransactions() != null) {
                            walletTransactions = walletDetailsModel.getTransactions();
                            tvCurrentBalance.setText(walletDetailsModel.getTotal());
                            walletDetails();
                        } else {
                            Toast.makeText(getContext(), "Wallet is empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), walletDetailsModel.getError(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                System.out.print("error"+t.toString());
            }
        });

    }

    private void init(){
        walletTransactions=new ArrayList<>();
        rvWalletList = v.findViewById(R.id.rvWalletList);
        llMyWallet = v.findViewById(R.id.llMyWallet);
        llBack = v.findViewById(R.id.llBack);
        llRefresh = v.findViewById(R.id.llRefresh);
        llNoNetwork = v.findViewById(R.id.llNoNetwork);
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        spnMonth = (EditText) v.findViewById(R.id.spnMonth);
        spnStatus = (EditText) v.findViewById(R.id.spnStatus);
        ivHelp = (ImageView) v.findViewById(R.id.ivHelp);
        ivNotification = (ImageView) v.findViewById(R.id.ivNotification);
        swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
        tvCurrentBalance=(TextView)v.findViewById(R.id.tvCurrentBalance);
    }

    private void setListener(){
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!NetworkConnection.isNetworkAvailable(getActivity())){
                    llNoNetwork.setVisibility(View.VISIBLE);
                    llMyWallet.setVisibility(View.GONE);
                }else {
                    llNoNetwork.setVisibility(View.GONE);
                    llMyWallet.setVisibility(View.VISIBLE);
//                    addMedicineList();
                    spnMonth.setText("Month");
                    spnStatus.setText("Status");
                }
                swipeRefresh.setRefreshing(false);
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new MainFragment());
            }
        });
        spnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addMedicineList();
                MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        if (month<10)
                            monthYearStr = year + "-0" + month +"-"+i2;
                        else
                            monthYearStr = year + "-" + month +"-"+i2;

                        if(walletTransactions.size()>0) {
                            walletTransactions1 = new ArrayList<>();

                            for (Transaction wt:walletTransactions) {
                                String m1 = wt.getCreated_at();
                                m1 = m1.substring(0,7);
                                if (wt.getCreated_at().substring(0,7).equalsIgnoreCase(monthYearStr.substring(0,7))) {
                                    walletTransactions1.add(wt);
                                }
                                Log.i("m1",m1);
                            }
                            myPointsAdapter = new MyWalletAdapter(walletTransactions1,getActivity());
                            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            // gridLayoutManager=new GridLayoutManager(getActivity(),3);
                            rvWalletList.setLayoutManager(layoutManager);
                            rvWalletList.setItemAnimator(new DefaultItemAnimator());
                            rvWalletList.setAdapter(myPointsAdapter);
                            String dateVal=year + "-" + (month + 1) + "-" + i2;
                            spnMonth.setText(formatMonthYear(dateVal));
                        }
                    }
                });
                pickerDialog.show(getActivity().getSupportFragmentManager(), "MonthYearPickerDialog");

            }
        });
        spnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(historyListAdapter != null) {
                    historyListAdapter.getFilter().filter(spnStatus.getSelectedItem().toString());
                }*/
                showStatusDialog();
            }
        });

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new NotificationFragment());
            }
        });
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new NeedHelpFragment());
            }
        });
        llRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isNetworkAvailable(getActivity())) {
                    llNoNetwork.setVisibility(View.VISIBLE);
                    llMyWallet.setVisibility(View.GONE);
                } else {
                    llNoNetwork.setVisibility(View.GONE);
                    llMyWallet.setVisibility(View.VISIBLE);
//                    addMedicineList();
                }
            }
        });
    }
    private void showStatusDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_status_wallet);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();

        TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkay);
        final TextView tvCredited = (TextView) dialog.findViewById(R.id.tvCredited);
        final TextView tvDebited = (TextView) dialog.findViewById(R.id.tvDebited);

        tvCredited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myPointsAdapter != null) {
                    myPointsAdapter.getFilter().filter(tvCredited.getText().toString().toLowerCase());
                }
                spnStatus.setText(tvCredited.getText().toString());
                dialog.dismiss();
            }
        });
        tvDebited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myPointsAdapter != null) {
                    myPointsAdapter.getFilter().filter(tvDebited.getText().toString().toLowerCase());
                }
                spnStatus.setText(tvDebited.getText().toString());
                dialog.dismiss();
            }
        });

        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    String formatMonthYear(String str) {
        Date date = null;
        try {
            date = input.parse(str);
            // historyListAdapter.getFilter().filter(filter.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }

    private void walletDetails(){
        myPointsList=new ArrayList<Transaction>();
//        prepareMedicineList();
        myPointsAdapter = new MyWalletAdapter(walletTransactions,getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        // gridLayoutManager=new GridLayoutManager(getActivity(),3);
        rvWalletList.setLayoutManager(layoutManager);
        rvWalletList.setItemAnimator(new DefaultItemAnimator());
        rvWalletList.setAdapter(myPointsAdapter);

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