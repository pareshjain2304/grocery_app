package in.happiness.groceryapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.adaptar.TicketDetailsAdapter;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.MyTickets;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.ReplyTicket;
import in.happiness.groceryapp.model.Tickets;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketDetailsFragment extends Fragment {
    View v;
    LinearLayout llTopMenu,llBack;
    ImageView ivNotification,ivHelp;
    private List<Tickets> ticketDetails = new ArrayList<>();
    private List<ReplyTicket> replyDataList = new ArrayList<ReplyTicket>();
    private RecyclerView rvDetailsList;
    private TextView tvSubmit;
    private TicketDetailsAdapter ticketDetailsAdapter;
    LinearLayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    EditText edtMessage;
    AppSharedPreferences sharedPreferences;
    String token,mobile,ticket_id;
    Bundle bundle;
    TextView tvNoComplaintFound;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_ticket_details, container, false);
        sharedPreferences = new AppSharedPreferences(getActivity());
        bundle=new Bundle();

        init();

        llTopMenu.setVisibility(View.GONE);
        token= AppConstant.userToken;
        user=AppConstant.user;
        //   token=sharedPreferences.doGetFromSharedPreferences(AppConstant.userToken);
        mobile=user.getMobile();

        //getUserProfile();

        bundle = this.getArguments();
        if (bundle != null) {
            ticket_id = bundle.getString("ticket_id", null);
        }
        getHelpList();

        setListener();

        return v;
    }

    private void init(){
        llBack = v.findViewById(R.id.llBack);
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        ivHelp = v.findViewById(R.id.ivHelp);
        ivNotification = v.findViewById(R.id.ivNotification);
        //   edtDescription = (EditText) v.findViewById(R.id.edtDescription);
        tvSubmit = v.findViewById(R.id.tvSubmit);
        edtMessage = v.findViewById(R.id.edtMessage);
        rvDetailsList = v.findViewById(R.id.rvDetailsList);
        tvNoComplaintFound = v.findViewById(R.id.tvNoComplaintFound);
    }

    private void getHelpList() {
        RequestBody TicketId = RequestBody.create(MediaType.parse("text/plain"),ticket_id );
        Call<MyTickets> call = RetrofitClient.getApiInterface().getTicketDetails("Bearer "+token,TicketId);
        call.enqueue(new Callback<MyTickets>() {
            @Override
            public void onResponse(Call<MyTickets> call, Response<MyTickets> response) {
                replyDataList=new ArrayList<ReplyTicket>();
                try {
                    if (response.body().isStatus()) {
                        tvNoComplaintFound.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.body().getReply().size(); i++) {
                            replyDataList.add(response.body().getReply().get(i));

                            if(!response.body().getReply().get(i).getUser_id().equals("0")){
                                tvNoComplaintFound.setVisibility(View.GONE);
                            }
                        }

                        ticketDetailsAdapter = new TicketDetailsAdapter(replyDataList, getActivity());
                        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        // gridLayoutManager=new GridLayoutManager(getActivity(),3);
                        // layoutManager.setStackFromEnd(true);
                        rvDetailsList.setLayoutManager(layoutManager);
                        rvDetailsList.setItemAnimator(new DefaultItemAnimator());
                        rvDetailsList.setAdapter(ticketDetailsAdapter);
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    tvNoComplaintFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MyTickets> call, Throwable t) {
                tvNoComplaintFound.setVisibility(View.VISIBLE);
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                //Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setListener(){
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new NotificationFragment());
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtMessage.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please type message", Toast.LENGTH_LONG).show();
                }else {
                    addMessage();
                }
            }
        });
    }

    private void addMessage() {
        RequestBody TicketId = RequestBody.create(MediaType.parse("text/plain"),ticket_id );
        RequestBody message = RequestBody.create(MediaType.parse("text/plain"),edtMessage.getText().toString());

        Call<OTP> call = RetrofitClient.getApiInterface().replyToTicket("Bearer "+token,TicketId,message);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                if (response.body().isStatus()) {
                    getHelpList();
                    edtMessage.getText().clear();
                    //  progressDialog.dismiss();
                    //  loadFragment(new TicketDetailsFragment());
                    // Toast.makeText(getActivity(),"Submitted Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                //  Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                //  progressDialog.dismiss();
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