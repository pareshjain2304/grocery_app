package in.happiness.groceryapp.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import in.happiness.groceryapp.R;
import in.happiness.groceryapp.activity.MapActivity;
import in.happiness.groceryapp.adaptar.MainCategoryAdaptar;
import in.happiness.groceryapp.adaptar.StoresAdaptar;
import in.happiness.groceryapp.adaptar.ViewPagerAdapter;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.CartItem;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.Stores;
import in.happiness.groceryapp.model.UserProfile;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment  extends Fragment implements MainCategoryAdaptar.ItemClickListener, StoresAdaptar.ItemClickListener  {
    View v;
    LinearLayout llTopMenu, llExploreCategory, llExploreStore, llMenu;
    TextView tvLocation, shopCount, loadMoreShops, tvCartCount;
    ImageView ivNotification, ivCart;
    String token, locationAvailable;
    int numberOfColumns;
    AppSharedPreferences sharedPreferences;
    MainCategoryAdaptar adapter;
    ArrayList<Category> categories;
    List<Address> addresses;
    private ArrayList<Stores> stores, stores1;
    private ViewPager pager;
    Geocoder geocoder;
    StoresAdaptar storesAdaptar;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewStores, coursesGV;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ProgressDialog progressDialog;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 8000; // time in milliseconds between successive task executions.
    private int NUM_PAGES;
    private AppDBHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        sharedPreferences = new AppSharedPreferences(getActivity());
        init();
        dbHelper = new AppDBHelper(getActivity());
        token=sharedPreferences.doGetFromSharedPreferences(AppConstant.token);
        AppConstant.userToken=token;
        categories=new ArrayList<>();
        numberOfColumns = 3;
        setLayout(AppConstant.imageUrl);
        getCategories();
        llTopMenu.setVisibility(View.VISIBLE);
        setListener();
        onBackPressed();
        return v;
    }

    private void getCategories() {
        if (AppConstant.categoryList.size()>0) {
            coursesGV.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
            adapter = new MainCategoryAdaptar(getActivity(), AppConstant.categoryList);
            adapter.setClickListener(MainFragment.this);
            coursesGV.setAdapter(adapter);
        }
        locationAvailable = sharedPreferences.doGetFromSharedPreferences(AppConstant.locationAvailable);
        if  (locationAvailable!=null && locationAvailable.equals("true")) {
            String latitude = sharedPreferences.doGetFromSharedPreferences(AppConstant.userLat);
            String longitude = sharedPreferences.doGetFromSharedPreferences(AppConstant.userLong);
            tvLocation.setText(sharedPreferences.doGetFromSharedPreferences(AppConstant.userLocality));
            if ((latitude!=null || latitude!="") && (longitude!=null || longitude!="")) {
//                stores = dbHelper.shopList("local", latitude, longitude);
                getNearbyShops(latitude, longitude, true);
//                if (stores.size()<4) {
//                    getNearbyShops(latitude, longitude, true);
//                } else {
//                    showShops(stores, latitude, longitude);
//                    getNearbyShops(latitude, longitude, false);
//                }
            }
        }
        getCartItems();
    }

   private void getCartItems() {
        Call<UserProfile> call = RetrofitClient.getApiInterface().getCartItem("Bearer "+token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {
                    if (response.code() == 200) {
                        AppConstant.userProfile = new UserProfile();
                        AppConstant.userProfile = response.body();
                        addToCart(AppConstant.userProfile.getCart_items());
                        Log.i("item",tvCartCount.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addToCart(ArrayList<CartItem> cart_items) {
        if (cart_items.size()>0) {
            for (CartItem c : cart_items) {
                CartItem cI = dbHelper.getPriceCategory(String.valueOf(c.getVariant_id()));
                if (cI!=null) {
                    dbHelper.addToCart("update",String.valueOf(c.getProduct_user_id()),c.getQuantity(),String.valueOf(c.getVariant_id()),c.getSubtotal(),cI);
                } else {
                    dbHelper.addToCart("add",String.valueOf(c.getProduct_user_id()),c.getQuantity(),String.valueOf(c.getVariant_id()),c.getSubtotal(),cI);
                }
            }
        }
        AppConstant.cart_item = new ArrayList<CartItem>();
        AppConstant.cart_item = dbHelper.getCartItem();
        tvCartCount.setText(String.valueOf(AppConstant.cart_item.size()));
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d("location", location.toString());
                    sharedPreferences.doSaveToSharedPreferences(AppConstant.userLat, String.valueOf(location.getLatitude()));
                    sharedPreferences.doSaveToSharedPreferences(AppConstant.userLong, String.valueOf(location.getLongitude()));
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses != null) {
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.userAddress, addresses.get(0).getAddressLine(0));
                        String address = addresses.get(0).getSubAdminArea();
                        String locality = addresses.get(0).getLocality();
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.userCity, address);
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.userLocality, locality);
                        if (locality != null && !locality.isEmpty()) {
                            tvLocation.setText(locality);
                        }
                    }
                }
            }
        });

        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Please grant the location permission", Toast.LENGTH_SHORT).show();
            }
        });

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.userLat, String.valueOf(location.getLatitude()));
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.userLong, String.valueOf(location.getLongitude()));
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        if (addresses != null) {
                            String address = addresses.get(0).getLocality();
                            tvLocation.setText(address);
                        } else {
                            getCartItems();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getNearbyShops(String latitude, String longitude, boolean b) {
        RequestBody userLat = RequestBody.create(MediaType.parse("text/plain"), latitude);      //latitude
        RequestBody userLong = RequestBody.create(MediaType.parse("text/plain"), longitude);     //longitude
        Call<ArrayList<Stores>> call = RetrofitClient.getApiInterface().getLocalShops("Bearer "+token, userLat, userLong);
        call.enqueue(new Callback<ArrayList<Stores>>() {
            @Override
            public void onResponse(Call<ArrayList<Stores>> call, Response<ArrayList<Stores>> response) {
                try {
                    if (response.code() == 200) {
                        stores=response.body();
                        if (stores.size()>0) {
                            showShops(stores, latitude, longitude);
                            for (Stores s : stores) {
                                int count = dbHelper.shopListById(s.getId());
                                if (count==0) {
                                    dbHelper.addShops(s, latitude, longitude, "add");
                                } else {
                                    dbHelper.addShops(s, latitude, longitude, "update");
                                }
                            }
                        } else {
                            recyclerViewStores.setVisibility(View.INVISIBLE);
                            shopCount.setText("No Shops found near you");
                        }
                    } else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Stores>> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showShops(ArrayList<Stores> stores, String latitude, String longitude) {
        if (stores.size()>5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stores1 = (ArrayList<Stores>) stores.stream().limit(5).collect(Collectors.toList());
            }
        } else {
            stores1 = stores;
        }
        String shopsAvailable="";
        if (stores.size()>5) {
            shopsAvailable="5+ Shops found near you";
            loadMoreShops.setVisibility(View.VISIBLE);
        } else {
            shopsAvailable=stores.size()+" Shops found near you";
            loadMoreShops.setVisibility(View.GONE);
        }
        shopCount.setText(shopsAvailable);
        storesAdaptar = new StoresAdaptar(stores1,getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        // gridLayoutManager=new GridLayoutManager(getActivity(),3);
        storesAdaptar.setClickListener(MainFragment.this);
        recyclerViewStores.setVisibility(View.VISIBLE);
        recyclerViewStores.setLayoutManager(layoutManager);
        recyclerViewStores.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStores.setAdapter(storesAdaptar);
    }

    @Override
    public void onItemClick(View view, int position) {
        loadFragment(new CategoryFragment());
    }

    private void setLayout(List<String> img_url) {
        if (img_url==null) {
            img_url = new ArrayList<>();
        }
        NUM_PAGES = img_url.size();
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setAdapter(new ViewPagerAdapter(getActivity(), img_url));
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                Log.i("timer",String.valueOf(System.currentTimeMillis()));
                pager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void init() {
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        coursesGV = v.findViewById(R.id.idGVcourses);
        ivNotification = getActivity().findViewById(R.id.ivNotification);
        ivCart = getActivity().findViewById(R.id.ivCart);
        llExploreCategory = v.findViewById(R.id.llExploreCategory);
        llExploreStore = v.findViewById(R.id.llExploreStore);
        tvCartCount = getActivity().findViewById(R.id.tvCartCount);
        tvCartCount.setText("0");
        llMenu = getActivity().findViewById(R.id.llMenu);
        tvLocation = getActivity().findViewById(R.id.tvLocation);
        recyclerViewStores = v.findViewById(R.id.recyclerViewStores);
        recyclerViewStores.setVisibility(View.INVISIBLE);
        shopCount = v.findViewById(R.id.shopCount);
        loadMoreShops=v.findViewById(R.id.loadMoreShops);
        loadMoreShops.setVisibility(View.GONE);
    }

    private void onBackPressed() {
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void setListener() {
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), MapActivity.class));
            }
        });
        llMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (locationAvailable!=null && locationAvailable.equals("true")) {
                    loadFragment(new UserProfileFragment());
                } else {
                    showLocationDialog();
                }
            }
        });
        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (locationAvailable!=null && locationAvailable.equals("true")) {
                    loadFragment(new NotificationFragment());
                } else {
                    showLocationDialog();
                }
            }
        });
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (locationAvailable!=null && locationAvailable.equals("true")) {
                    if (AppConstant.cart_item.size() > 0) {
                        loadFragment(new CartFragment());
                    } else {
                        Toast.makeText(getActivity(), "Cart is empty.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showLocationDialog();
                }
            }
        });
        llExploreCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (locationAvailable!=null && locationAvailable.equals("true")) {
                    loadFragment(new CategoryFragment());
                } else {
                    showLocationDialog();
                }
            }
        });
        llExploreStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (locationAvailable!=null && locationAvailable.equals("true")) {
                    loadFragment(new StoresFragment());
                } else {
                    showLocationDialog();
                }
            }
        });
        loadMoreShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (locationAvailable!=null && locationAvailable.equals("true")) {
                    loadFragment(new StoresFragment());
                } else {
                    showLocationDialog();
                }
            }
        });
    }

    private void showLocationDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_location_assistance);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();

        TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkay);

        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(getActivity(), MapActivity.class));
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


    @Override
    public void onItemClickStores(View view, int position) {
        if (stores1.get(position).getShopOnline().equals("1")) {
            if (AppConstant.cart_item.size() > 0) {
                if (AppConstant.cart_item.get(0).getVendor_id() == stores1.get(position).getId()) {
                        loadFragment(new CategoryFragment(), stores1.get(position));
                } else {
                Toast.makeText(getActivity(), "Please clear previous items saved in cart", Toast.LENGTH_SHORT).show();
                }
            } else {
                loadFragment(new CategoryFragment(), stores1.get(position));
            }
        } else {
            Toast.makeText(getActivity(), "Currently store is unable to accept orders", Toast.LENGTH_SHORT).show();
        }
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