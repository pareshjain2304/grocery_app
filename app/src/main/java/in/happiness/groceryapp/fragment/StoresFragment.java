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
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.adaptar.StoresAdaptar;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.CartItem;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.Stores;
import in.happiness.groceryapp.model.UserProfile;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoresFragment extends Fragment implements StoresAdaptar.ItemClickListener {
    View view;
    LinearLayout llCart, llBack, llTopMenu;
    Category category;
    String token, categoryid;
    EditText edStores;
    AppSharedPreferences sharedPreferences;
    TextView shopCount, tvCartCount;
    ArrayList<Stores> stores;
    StoresAdaptar storesAdaptar;
    boolean flag=false;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewStores;
    private AppDBHelper dbHelper;
    private ArrayList<Stores> filteredList;
    private ProgressDialog progressDialog;

    public StoresFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stores, container, false);
        sharedPreferences=new AppSharedPreferences(getActivity());
        token=sharedPreferences.doGetFromSharedPreferences(AppConstant.token);
        stores=new ArrayList<>();
        init();
        dbHelper = new AppDBHelper(getContext());
        getCartItems();
//        tvCartCount.setText(String.valueOf(AppConstant.cart_item.size()));
        String locationAvailable = sharedPreferences.doGetFromSharedPreferences(AppConstant.locationAvailable);
        if  (locationAvailable!=null && locationAvailable.equals("true")) {
            if (getArguments().containsKey("categoryid")) {
                categoryid = (String) getArguments().get("categoryid");
                getStores(categoryid);
                flag = true;
            } else {
                flag = false;
                String latitude = sharedPreferences.doGetFromSharedPreferences(AppConstant.userLat);
                String longitude = sharedPreferences.doGetFromSharedPreferences(AppConstant.userLong);
                stores = dbHelper.shopList("all", latitude, longitude);
                showShops(stores);
//                if ((latitude!=null || latitude!="") && (longitude!=null || longitude!="")) {
//                    getNearbyShops();
//                }
            }
        }
        llTopMenu.setVisibility(View.GONE);
        setListener();
        return view;
    }

    private void showShops(ArrayList<Stores> stores) {
        filteredList=stores;
        if (stores.size()>0) {
            assignStores(filteredList);
        } else {
            shopCount.setText("No Shops found near you");
        }
    }

    private void getCartItems() {
        AppConstant.cart_item = dbHelper.getCartItem();
        tvCartCount.setText(String.valueOf(AppConstant.cart_item.size()));
    }

//    private void getCartItems1() {
//        Call<UserProfile> call = RetrofitClient.getApiInterface().getCartItem("Bearer "+token);
//        call.enqueue(new Callback<UserProfile>() {
//            @Override
//            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
//                try {
//                    if (response.code() == 200) {
//                        AppConstant.userProfile = new UserProfile();
//                        AppConstant.userProfile = response.body();
//                        AppConstant.cart_item = new ArrayList<CartItem>();
//                        AppConstant.cart_item = AppConstant.userProfile.getCart_items();
//                        tvCartCount.setText(String.valueOf(AppConstant.cart_item.size()));
//                        Log.i("item",tvCartCount.getText().toString());
//                    } else {
//                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    Log.i("error", e.toString());
//                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserProfile> call, Throwable t) {
//                Log.i("onFailure", t.getMessage());
//                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }


    private void getNearbyShops() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userLat));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userLong));
        Call<ArrayList<Stores>> call = RetrofitClient.getApiInterface().getLocalShops("Bearer "+token, latitude, longitude);
        call.enqueue(new Callback<ArrayList<Stores>>() {
            @Override
            public void onResponse(Call<ArrayList<Stores>> call, Response<ArrayList<Stores>> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
//                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                        stores=response.body();
                        filteredList=response.body();
                        if (stores.size()>0) {
                            assignStores(filteredList);
                        } else {
                            shopCount.setText("No Shops found near you");
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Stores>> call, Throwable t) {
                progressDialog.dismiss();
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
            }
        });
    }

    private void getStores(String categoryid) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody search = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody category_id = RequestBody.create(MediaType.parse("text/plain"), categoryid);
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userLat));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.doGetFromSharedPreferences(AppConstant.userLong));
        Call<ArrayList<Stores>> call = RetrofitClient.getApiInterface().getStores("Bearer "+token, category_id, search, latitude, longitude);
        call.enqueue(new Callback<ArrayList<Stores>>() {
            @Override
            public void onResponse(Call<ArrayList<Stores>> call, Response<ArrayList<Stores>> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        stores=response.body();
                        filteredList=new ArrayList<>();
                        filteredList=response.body();
                        assignStores(filteredList);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), String.valueOf(response.errorBody()), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Stores>> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void assignStores(ArrayList<Stores> filteredList) {
        if (filteredList.size()>0) {
            String shopsAvailable=filteredList.size()+" Shops found near you";
            shopCount.setText(shopsAvailable);
            storesAdaptar = new StoresAdaptar(filteredList,getActivity());
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            storesAdaptar.setClickListener(StoresFragment.this);
            recyclerViewStores.setLayoutManager(layoutManager);
            recyclerViewStores.setItemAnimator(new DefaultItemAnimator());
            recyclerViewStores.setAdapter(storesAdaptar);
        } else {
            shopCount.setText("No Shops found near you");
        }
    }

    private void setListener() {
        edStores.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(storesAdaptar != null) {
                    if(stores.size()>0) {
                        filteredList=new ArrayList<>();
                        for (Stores prod : stores) {
                            if (prod.getShopName().toLowerCase().contains(s.toString().toLowerCase())) {
                                filteredList.add(prod);
                            }
                        }
                        assignStores(filteredList);
//                        filteredList.addAll(productsList);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConstant.cart_item.size()>0) {
                    loadFragment(new CartFragment());
                } else {
                    Toast.makeText(getActivity(), "Cart is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        llStoreItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new ProductListFragment());
//            }
//        });
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
        llBack = view.findViewById(R.id.llBack);
        llCart = view.findViewById(R.id.llCart);
        recyclerViewStores = view.findViewById(R.id.recyclerViewStores);
        shopCount = view.findViewById(R.id.shopCount);
        edStores = view.findViewById(R.id.edStores);
        tvCartCount = view.findViewById(R.id.tvCartCount);
    }

    @Override
    public void onItemClickStores(View view, int position) {
        if (filteredList.get(position).getShopOnline().equals("1")) {
            if (AppConstant.cart_item != null) {
                if (flag) {
                    if (AppConstant.cart_item.size() > 0) {
                        if (AppConstant.cart_item.get(0).getVendor_id() == filteredList.get(position).getId()) {
                            loadFragment(new ProductListFragment(), filteredList.get(position).getId());
                        } else {
                            Toast.makeText(getActivity(), "Please clear previous items saved in cart", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        loadFragment(new ProductListFragment(), filteredList.get(position).getId());
                    }
                } else {
                    if (AppConstant.cart_item.size() > 0) {
                        if (AppConstant.cart_item.get(0).getVendor_id() == filteredList.get(position).getId()) {
                            loadFragment(new ProductListFragment(), filteredList.get(position).getId());
                        } else {
                            Toast.makeText(getActivity(), "Please clear previous items saved in cart", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        loadFragment(new ProductListFragment(), filteredList.get(position).getId());
                    }
                }
            } else {
                if (flag) {
                    loadFragment(new ProductListFragment(), filteredList.get(position).getId());
                } else {
                    loadFragment(new ProductListFragment(), filteredList.get(position).getId());
                }
            }
        } else {
            Toast.makeText(getActivity(), "Currently store is unable to accept orders", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment, long storeId) {
        Bundle bundle = new Bundle();
        bundle.putString("storeId",String.valueOf(storeId));
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadFragment(Fragment fragment, Stores store) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("store",store);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}