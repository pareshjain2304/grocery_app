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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.adaptar.OrderAdaptar;
import in.happiness.groceryapp.adaptar.OrderStatusAdaptar;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.Order;
import in.happiness.groceryapp.model.OrderList;
import in.happiness.groceryapp.model.OrderStatus;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment implements OrderAdaptar.ItemClickListener, OrderStatusAdaptar.ItemClickListener {
    private View v;
    private LinearLayout llBack, llTopMenu, llOrder, llTrackOrder;
    private String token;
    private ArrayList<Order> orders;
    private ArrayList<Order> filteredList;
    private OrderAdaptar orderAdaptar;
    private EditText edOrderId;
    private RecyclerView.LayoutManager layoutManagerCart, layoutManagerStatus;
    private RecyclerView recyclerViewOrder, recyclerViewOrderStatus;
    private ProgressDialog progressDialog;
    private String[] orderStatus = new String[] {"All", "Pending", "Placed", "Processing", "Packed", "Dispatched", "Delivered", "Cancelled"};
    private OrderStatusAdaptar orderStatusAdaptar;
    private ArrayList<OrderStatus> orderStatuses;

    public OrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_orders, container, false);
        init();
        token=AppConstant.userToken;
        assignOrderStatus();
        getOrderDetails("all");
        llTopMenu.setVisibility(View.GONE);
        setListener();
        return v;
    }

    private void assignOrderStatus() {
        orderStatuses = new ArrayList<>();
        for (String s: orderStatus) {
            OrderStatus os = new OrderStatus();
            os.setOrderStatus(s);
            if (s.equals("All")) {
                os.setSelected(true);
            } else {
                os.setSelected(false);
            }
            orderStatuses.add(os);
        }
        setOrderStatusList(orderStatuses);
    }

    private void setOrderStatusList(ArrayList<OrderStatus> orderStatuses) {
        if (orderStatuses.size() > 0) {
            orderStatusAdaptar = new OrderStatusAdaptar(orderStatuses, getActivity(), this);
            layoutManagerStatus = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            // gridLayoutManager=new GridLayoutManager(getActivity(),3);
            recyclerViewOrderStatus.setLayoutManager(layoutManagerStatus);
            recyclerViewOrderStatus.setItemAnimator(new DefaultItemAnimator());
            recyclerViewOrderStatus.setAdapter(orderStatusAdaptar);
        }
    }

    private void getOrderDetails(String status) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody order_status = RequestBody.create(MediaType.parse("text/plain"), status);
        Call<OrderList> call = RetrofitClient.getApiInterface().getOrders("Bearer "+token, order_status);
        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        orders = response.body().getData();
                        filteredList=new ArrayList<>();
                        filteredList = response.body().getData();
                        if (filteredList.size()>0) {
                            setOrdersList(filteredList);
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
            public void onFailure(Call<OrderList> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setOrdersList(ArrayList<Order> orders) {
        orderAdaptar = new OrderAdaptar(orders, getActivity(), this);
        layoutManagerCart = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        // gridLayoutManager=new GridLayoutManager(getActivity(),3);
        recyclerViewOrder.setLayoutManager(layoutManagerCart);
        recyclerViewOrder.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOrder.setAdapter(orderAdaptar);
    }

    private void setListener() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MainFragment());
            }
        });
        edOrderId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(orderAdaptar != null) {
                    if(orders.size()>0) {
                        filteredList=new ArrayList<>();
                        for (Order order : orders) {
                            if (order.getOrderNumber().toLowerCase().contains(s.toString().toLowerCase())) {
                                filteredList.add(order);
                            }
                        }
                        setOrdersList(filteredList);
                    }
                }
            }
        });
    }

    private void init() {
        llBack = v.findViewById(R.id.llBack);
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        recyclerViewOrder = v.findViewById(R.id.recyclerViewOrder);
        edOrderId = v.findViewById(R.id.edOrderId);
        recyclerViewOrderStatus = v.findViewById(R.id.recyclerViewOrderStatus);
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
    public void subCategoryId(int lastSelectedPosition) {

    }

    @Override
    public void orderStatus(int lastSelectedPosition) {
        String oStatus = orderStatuses.get(lastSelectedPosition).getOrderStatus();
        for (int i=0;i<orderStatuses.size(); i++) {
            orderStatuses.get(i).setSelected(false);
        }
        orderStatuses.get(lastSelectedPosition).setSelected(true);
        setOrderStatusList(orderStatuses);
        filteredList = new ArrayList<>();
        if (oStatus.equals("All")) {
            filteredList=orders;
        } else {
            for (Order ordr : orders) {
                if (ordr.getStatus().equals(oStatus.toLowerCase())) {
                    filteredList.add(ordr);
                }
            }
        }
        setOrdersList(filteredList);
    }
}