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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.Order;
import in.happiness.groceryapp.model.OrderDetail;
import in.happiness.groceryapp.utils.AppConstant;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackOrderFragment extends Fragment {
    private View v;
    private LinearLayout llTopMenu, llBack, llClose;
    private String order_id, token;
    private View view_order_placed,view_order_processed,view_order_pickup,view_order_dispatch,view_order_delivered,con_divider,ready_divider,ready_divider2, ready_divider1,placed_divider;
    private ImageView orderprocessed,orderpickup,orderplaced,orderdispatch,orderdelivered;
    private TextView textorderpickup,textorderprocessed,textorderdispatch,textorderplaced,textorderdeliver;
    private TextView textorderplacedtime,textorderplaceddate,textorderdeliverdate,textorderdelivertime,textorderdispatchdate,textorderdispatchtime,
            textorderpickupdate,textorderpickuptime,textorderprocesseddate,textorderprocessedtime;
    private TextView tvOrderId, tvTotal, tvAddress;
    private ProgressDialog progressDialog;

    public TrackOrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_track_order, container, false);
        init();
        token= AppConstant.userToken;
        if (getArguments()!=null) {
            order_id=(String) getArguments().get("order_id");
            getOrderDetail(order_id);
        }
        llTopMenu.setVisibility(View.GONE);
        setListener();
        return v;
    }

    private void getOrderDetail(String order_id) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody orderId = RequestBody.create(MediaType.parse("text/plain"), order_id);
        Call<OrderDetail> call = RetrofitClient.getApiInterface().orderDetail("Bearer "+token, orderId);
        call.enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        Order order = response.body().getData();
                        getOrderStatus(order);
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
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getOrderStatus(Order order) {
        tvOrderId.setText("Order ID: "+order.getId());
        tvTotal.setText(order.getGrandTotal());
        tvAddress.setText(order.getShippingAddress());
        if (order.getStatus().equals("placed")){
            boolean placedFlag = false;
            if (order.getPlacedAt()!=null){
                String[] placed = order.getPlacedAt().split(" ");
                if (placed.length==2) {
                    textorderplaceddate.setText(placed[0]);
                    textorderplacedtime.setText(placed[1]);
                    placedFlag = true;
                }
            }
            if (!placedFlag) {
                textorderplaceddate.setText("NA");
                textorderplacedtime.setText("NA");
            }
            float alfa= (float) 0.5;
            setStatus(alfa);
        } else if (order.getStatus().equals("processing")){
            boolean placedFlag = false;
            if (order.getPlacedAt()!=null){
                String[] placed = order.getPlacedAt().split(" ");
                if (placed.length==2) {
                    textorderplaceddate.setText(placed[0]);
                    textorderplacedtime.setText(placed[1]);
                    placedFlag = true;
                }
            }
            if (!placedFlag) {
                textorderplaceddate.setText("NA");
                textorderplacedtime.setText("NA");
            }
            boolean processedFlag = false;
            if (order.getProcessingAt()!=null){
                String[] processing = order.getProcessingAt().split(" ");
                if (processing.length==2) {
                    textorderprocesseddate.setText(processing[0]);
                    textorderprocessedtime.setText(processing[1]);
                    processedFlag = true;
                }
            }
            if (!processedFlag) {
                textorderprocesseddate.setText("NA");
                textorderprocessedtime.setText("NA");
            }
            float alfa= (float) 1;
            setStatus1(alfa);
        } else if (order.getStatus().equals("packed")){
            boolean placedFlag = false;
            if (order.getPlacedAt()!=null){
                String[] placed = order.getPlacedAt().split(" ");
                if (placed.length==2) {
                    textorderplaceddate.setText(placed[0]);
                    textorderplacedtime.setText(placed[1]);
                    placedFlag = true;
                }
            }
            if (!placedFlag) {
                textorderplaceddate.setText("NA");
                textorderplacedtime.setText("NA");
            }
            boolean processedFlag = false;
            if (order.getProcessingAt()!=null){
                String[] processing = order.getProcessingAt().split(" ");
                if (processing.length==2) {
                    textorderprocesseddate.setText(processing[0]);
                    textorderprocessedtime.setText(processing[1]);
                    processedFlag = true;
                }
            }
            if (!processedFlag) {
                textorderprocesseddate.setText("NA");
                textorderprocessedtime.setText("NA");
            }
            boolean packedFlag = false;
            if (order.getPackedAt()!=null){
                String[] packing = order.getPackedAt().split(" ");
                if (packing.length==2) {
                    textorderpickupdate.setText(packing[0]);
                    textorderpickuptime.setText(packing[1]);
                    packedFlag = true;
                }
            }
            if (!packedFlag) {
                textorderpickupdate.setText("NA");
                textorderpickuptime.setText("NA");
            }
            float alfa= (float) 1;
            setStatus2(alfa);
        } else if (order.getStatus().equals("dispatched")){
            boolean placedFlag = false;
            if (order.getPlacedAt()!=null){
                String[] placed = order.getPlacedAt().split(" ");
                if (placed.length==2) {
                    textorderplaceddate.setText(placed[0]);
                    textorderplacedtime.setText(placed[1]);
                    placedFlag = true;
                }
            }
            if (!placedFlag) {
                textorderplaceddate.setText("NA");
                textorderplacedtime.setText("NA");
            }
            boolean processedFlag = false;
            if (order.getProcessingAt()!=null){
                String[] processing = order.getProcessingAt().split(" ");
                if (processing.length==2) {
                    textorderprocesseddate.setText(processing[0]);
                    textorderprocessedtime.setText(processing[1]);
                    processedFlag = true;
                }
            }
            if (!processedFlag) {
                textorderprocesseddate.setText("NA");
                textorderprocessedtime.setText("NA");
            }
            boolean packedFlag = false;
            if (order.getPackedAt()!=null){
                String[] packing = order.getPackedAt().split(" ");
                if (packing.length==2) {
                    textorderpickupdate.setText(packing[0]);
                    textorderpickuptime.setText(packing[1]);
                    packedFlag = true;
                }
            }
            if (!packedFlag) {
                textorderpickupdate.setText("NA");
                textorderpickuptime.setText("NA");
            }
            boolean dispatchFlag = false;
            if (order.getDispatchedAt()!=null){
                String[] dispatch = order.getDispatchedAt().split(" ");
                if (dispatch.length==2) {
                    textorderdispatchdate.setText(dispatch[0]);
                    textorderdispatchtime.setText(dispatch[1]);
                    dispatchFlag = true;
                }
            }
            if (!dispatchFlag) {
                textorderdispatchdate.setText("NA");
                textorderdispatchdate.setText("NA");
            }
            float alfa= (float) 1;
            setStatus3(alfa);
        } else if (order.getStatus().equals("delivered")){
            boolean placedFlag = false;
            if (order.getPlacedAt()!=null){
                String[] placed = order.getPlacedAt().split(" ");
                if (placed.length==2) {
                    textorderplaceddate.setText(placed[0]);
                    textorderplacedtime.setText(placed[1]);
                    placedFlag = true;
                }
            }
            if (!placedFlag) {
                textorderplaceddate.setText("NA");
                textorderplacedtime.setText("NA");
            }
            boolean processedFlag = false;
            if (order.getProcessingAt()!=null){
                String[] processing = order.getProcessingAt().split(" ");
                if (processing.length==2) {
                    textorderprocesseddate.setText(processing[0]);
                    textorderprocessedtime.setText(processing[1]);
                    processedFlag = true;
                }
            }
            if (!processedFlag) {
                textorderprocesseddate.setText("NA");
                textorderprocessedtime.setText("NA");
            }
            boolean packedFlag = false;
            if (order.getPackedAt()!=null){
                String[] packing = order.getPackedAt().split(" ");
                if (packing.length==2) {
                    textorderpickupdate.setText(packing[0]);
                    textorderpickuptime.setText(packing[1]);
                    packedFlag = true;
                }
            }
            if (!packedFlag) {
                textorderpickupdate.setText("NA");
                textorderpickuptime.setText("NA");
            }
            boolean dispatchFlag = false;
            if (order.getDispatchedAt()!=null){
                String[] dispatch = order.getDispatchedAt().split(" ");
                if (dispatch.length==2) {
                    textorderdispatchdate.setText(dispatch[0]);
                    textorderdispatchtime.setText(dispatch[1]);
                    dispatchFlag = true;
                }
            }
            if (!dispatchFlag) {
                textorderdispatchdate.setText("NA");
                textorderdispatchtime.setText("NA");
            }
            boolean deliverFlag = false;
            if (order.getDeliveredAt()!=null){
                String[] deliver = order.getDeliveredAt().split(" ");
                if (deliver.length==2) {
                    textorderdeliverdate.setText(deliver[0]);
                    textorderdelivertime.setText(deliver[1]);
                    deliverFlag = true;
                }
            }
            if (!deliverFlag) {
                textorderdeliverdate.setText("NA");
                textorderdelivertime.setText("NA");
            }
            float alfa= (float) 1;
            setStatus4(alfa);
        }
    }

    private void setStatus(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderprocessed.setAlpha(myf);
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        placed_divider.setAlpha(alfa);
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderprocessed.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderpickup.setAlpha(alfa);
        view_order_delivered.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider1.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderdeliver.setAlpha(myf);
        orderdelivered.setAlpha(alfa);
        view_order_dispatch.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider2.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderdispatch.setAlpha(alfa);
        orderdispatch.setAlpha(alfa);
    }

    private void setStatus1(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderprocessed.setAlpha(alfa);
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        placed_divider.setAlpha(alfa);
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderprocessed.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderpickup.setAlpha(myf);
        view_order_delivered.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider1.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderdeliver.setAlpha(myf);
        orderdelivered.setAlpha(alfa);
        view_order_dispatch.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider2.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderdispatch.setAlpha(alfa);
        orderdispatch.setAlpha(alfa);
    }

    private void setStatus2(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderprocessed.setAlpha(alfa);
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        placed_divider.setAlpha(alfa);
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderprocessed.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderpickup.setAlpha(alfa);
        view_order_delivered.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider1.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderdeliver.setAlpha(alfa);
        orderdelivered.setAlpha(alfa);
        view_order_dispatch.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider2.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderdispatch.setAlpha(myf);
        orderdispatch.setAlpha(alfa);
    }

    private void setStatus3(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderprocessed.setAlpha(alfa);
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        placed_divider.setAlpha(alfa);
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderprocessed.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderpickup.setAlpha(alfa);
        view_order_delivered.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider1.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderdeliver.setAlpha(myf);
        orderdelivered.setAlpha(alfa);
        view_order_dispatch.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider2.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderdispatch.setAlpha(alfa);
        orderdispatch.setAlpha(alfa);
    }

    private void setStatus4(float alfa) {
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderprocessed.setAlpha(alfa);
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        placed_divider.setAlpha(alfa);
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderprocessed.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderpickup.setAlpha(alfa);
        view_order_delivered.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider1.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderdeliver.setAlpha(alfa);
        orderdelivered.setAlpha(alfa);
        view_order_dispatch.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider2.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderdispatch.setAlpha(alfa);
        orderdispatch.setAlpha(alfa);
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
                loadFragment(new OrdersFragment());
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
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        llBack = v.findViewById(R.id.llBack);
        llClose = v.findViewById(R.id.llClose);
        view_order_placed=v.findViewById(R.id.view_order_placed);
//        view_order_confirmed=v.findViewById(R.id.view_order_confirmed);
        view_order_processed=v.findViewById(R.id.view_order_processed);
        view_order_pickup=v.findViewById(R.id.view_order_pickup);
        view_order_dispatch=v.findViewById(R.id.view_order_dispatch);
        view_order_delivered=v.findViewById(R.id.view_order_delivered);
        placed_divider=v.findViewById(R.id.placed_divider);
        con_divider=v.findViewById(R.id.con_divider);
        ready_divider=v.findViewById(R.id.ready_divider);
        ready_divider2=v.findViewById(R.id.ready_divider2);
        ready_divider1=v.findViewById(R.id.ready_divider1);
        textorderplacedtime=v.findViewById(R.id.textorderplacedtime);
        textorderplaceddate=v.findViewById(R.id.textorderplaceddate);
        textorderpickup=v.findViewById(R.id.textorderpickup);
//        text_confirmed=v.findViewById(R.id.text_confirmed);
        textorderprocessed=v.findViewById(R.id.textorderprocessed);
//        img_orderconfirmed=v.findViewById(R.id.img_orderconfirmed);
        orderprocessed=v.findViewById(R.id.orderprocessed);
        orderpickup=v.findViewById(R.id.orderpickup);
        orderplaced=v.findViewById(R.id.orderplaced);
        orderdispatch=v.findViewById(R.id.orderdispatch);
        orderdelivered=v.findViewById(R.id.orderdelivered);
        tvOrderId=v.findViewById(R.id.tvOrderId);
        tvTotal=v.findViewById(R.id.tvTotal);
        tvAddress=v.findViewById(R.id.tvAddress);
        textorderplaced=v.findViewById(R.id.textorderplaced);
        textorderdispatch=v.findViewById(R.id.textorderdispatch);
        textorderdeliver=v.findViewById(R.id.textorderdeliver);
        textorderdeliverdate=v.findViewById(R.id.textorderdeliverdate);
        textorderdelivertime=v.findViewById(R.id.textorderdelivertime);
        textorderdispatchdate=v.findViewById(R.id.textorderdispatchdate);
        textorderdispatchtime=v.findViewById(R.id.textorderdispatchtime);
        textorderpickupdate=v.findViewById(R.id.textorderpickupdate);
        textorderpickuptime=v.findViewById(R.id.textorderpickuptime);
        textorderprocesseddate=v.findViewById(R.id.textorderprocesseddate);
        textorderprocessedtime=v.findViewById(R.id.textorderprocessedtime);
    }
}