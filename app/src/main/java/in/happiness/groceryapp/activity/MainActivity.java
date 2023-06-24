package in.happiness.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.MainData;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.utils.AppDBHelper;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.happiness.groceryapp.fragment.CartFragment;
import in.happiness.groceryapp.fragment.EditLocationFragment;
import in.happiness.groceryapp.fragment.FinalOrderFragment;
import in.happiness.groceryapp.fragment.LocationFragment;
import in.happiness.groceryapp.fragment.MainFragment;
import in.happiness.groceryapp.fragment.NotificationFragment;
import in.happiness.groceryapp.fragment.OrdersFragment;
import in.happiness.groceryapp.fragment.WalletFragment;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.HomeScreen;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.UserLocation;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppSharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity  extends AppCompatActivity {
    private static String TAG = "MainActivity";
    BottomNavigationView bottom_navigation;
    ImageView ivNotification, ivHelp;
    LinearLayout llTopMenu;
    TextView tvLocation;
    String token, location,locationRequest, locationId, orderId, addLocation, locationAvailable;
    AppSharedPreferences sharedPreferences;
    Geocoder geocoder;
    Context context;
    private  ProgressDialog progressDialog;
    private UserLocation userLocationDetails;
    private View notificationsBadge;
    private AppDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new AppDBHelper(this);
        sharedPreferences = new AppSharedPreferences(MainActivity.this);
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        context = this;
        token=sharedPreferences.doGetFromSharedPreferences(AppConstant.token);
        llTopMenu = findViewById(R.id.llTopMenu);
        bottom_navigation = findViewById(R.id.bottom_navigation);
//        llTopMenu.setVisibility(View.VISIBLE);
        tvLocation = findViewById(R.id.tvLocation);
        init();
        if (AppConstant.validate) {
            saveData();
        }
        bottom_navigation.setItemIconTintList(null);
        BottomNavigationMenuView mbottomNavigationMenuView = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);
        notificationsBadge = LayoutInflater.from(this).inflate(R.layout.custom_badge_layout,
                mbottomNavigationMenuView,false);
        Intent i = getIntent();
        locationAvailable = sharedPreferences.doGetFromSharedPreferences(AppConstant.locationAvailable);
        location=i.getStringExtra("location");
        orderId=i.getStringExtra("orderId");
//        getUserDetails();
        llTopMenu.setVisibility(View.VISIBLE);
        if (location!=null) {
            llTopMenu.setVisibility(View.GONE);
            userLocationDetails= (UserLocation) i.getSerializableExtra("userLocationDetail");
            locationRequest = (String) i.getSerializableExtra("locationRequest");
            if (locationRequest.equals("editLocation")) {
                locationId = (String) i.getSerializableExtra("locationId");
                addLocation = (String) i.getSerializableExtra("addLocation");
                loadFragment(new EditLocationFragment(), userLocationDetails, addLocation);
            } else {
                loadFragment(new LocationFragment(), userLocationDetails);
            }
        } else if (orderId!=null){
            String amount = i.getStringExtra("amount");
            String status = i.getStringExtra("status");
            loadFragment(new FinalOrderFragment(), orderId, amount, status);
        } else {
            String action = sharedPreferences.doGetFromSharedPreferences(AppConstant.click_action);
            if (action.equals("order_details")) {
                AppConstant.userToken=token;
                sharedPreferences.doSaveToSharedPreferences(AppConstant.click_action, "main");
                loadFragment(new OrdersFragment());
            } else {
                String lastSynchDate = sharedPreferences.doGetFromSharedPreferences(AppConstant.lastSynchDate);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date lastDate = null;
                Date currDate = new Date();
                try {
                    lastDate = sdf.parse(lastSynchDate);
                    String s = sdf.format(currDate);
                    currDate = sdf.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assert currDate != null;
                if (lastDate!=null) {
                    if (currDate.compareTo(lastDate) == 0) {
                        getBanner();
                    } else {
                        fetchBannerData(currDate);
                    }
                }

            }
        }
        getDeviceToken();
        setListener();
    }

    private void getBanner() {
        AppConstant.imageUrl = dbHelper.getBanner();
        loadFragment(new MainFragment());
    }

    private void fetchBannerData(Date currDate) {
        ArrayList<String> image_url = new ArrayList<>();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<HomeScreen> call = RetrofitClient.getApiInterface().appSetting("Bearer "+token);
        call.enqueue(new Callback<HomeScreen>() {
            @Override
            public void onResponse(Call<HomeScreen> call, Response<HomeScreen> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        dbHelper.truncateUserData();
                        for (String image : response.body().getHomeBanner()) {
                            dbHelper.addBanerImage(image);
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.lastSynchDate,sdf.format(currDate));
                        AppConstant.imageUrl=response.body().getHomeBanner();
                        loadFragment(new MainFragment());
                    } else {
                        progressDialog.dismiss();
                        AppConstant.imageUrl=image_url;
                        Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        loadFragment(new MainFragment());
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    AppConstant.imageUrl=image_url;
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    loadFragment(new MainFragment());
                }
            }

            @Override
            public void onFailure(Call<HomeScreen> call, Throwable t) {
                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
                Log.i("onFailure", t.getMessage());
                progressDialog.dismiss();
                AppConstant.imageUrl=image_url;
                Toast.makeText(MainActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                loadFragment(new MainFragment());
            }
        });
    }

    private void saveData() {
        MainData md = AppConstant.response;
        dbHelper = new AppDBHelper(this);
        if (md.getCategories().size()>0) {
            for (Category c:md.getCategories()) {
                dbHelper.addCategory(c);
            }
        }
        if (md.getSubcategories().size()>0) {
            for (SubCategory c:md.getSubcategories()) {
                dbHelper.addSubCategory(c);
            }
        }
        if (md.getChildcategories().size()>0) {
            for (Category c:md.getChildcategories()) {
                dbHelper.addChildCategory(c);
            }
        }
        if (md.getBrands().size()>0) {
            for (Category c:md.getBrands()) {
                dbHelper.addBrands(c);
            }
        }
        AppConstant.validate=false;
        fetchData();
    }

    private void fetchData() {
        AppConstant.categoryList = dbHelper.getAllCategory();
        Map<Integer, List<SubCategory>> subcategories = new HashMap<>();
        if (AppConstant.categoryList.size()>0) {
            for (Category c: AppConstant.categoryList) {
                List<SubCategory> subCategoryList= dbHelper.getAllSubCategory(c.getId());
                subcategories.put(c.getId(),subCategoryList);
            }
        }
        AppConstant.subCategoryList = subcategories;
    }

    private void loadFragment(Fragment fragment, String orderId, String amount, String status) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderId",orderId);
        bundle.putSerializable("amount",amount);
        bundle.putSerializable("status",status);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadFragment(Fragment fragment, UserLocation userLocationDetails, String addLocation) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("mainScreen","mainScreen");
        bundle.putSerializable("locationId",locationId);
        bundle.putSerializable("addLocation",addLocation);
        bundle.putSerializable("userLocationDetails",userLocationDetails);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (!task.isSuccessful()) {
                    Log.e("SplashActivity", "Failed to get the token.");
                    return;
                }

                String token1 = task.getResult();
                updateToken(token1);
                Log.d("SplashActivity", "Token : " + token1);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("SplashActivity", "Failed to get the token : " + e.getLocalizedMessage());
            }
        });
    }

    private void updateToken(String token1) {
        RequestBody userToken = RequestBody.create(MediaType.parse("text/plain"), token1);
        RequestBody versionCode = RequestBody.create(MediaType.parse("text/plain"), AppConstant.versionCode);
        Call<OTP> call = RetrofitClient.getApiInterface().updateToken("Bearer "+token, userToken, versionCode);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body().isStatus()) {
                            Log.i("token", "token updated successfully");
                        }
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(MainActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*GlobalList.medicineList.clear();
        setList = new HashSet<>(GlobalList.medicineList);
        setList.clear();
        sharedPreferences.doSaveArrayToSharedPreferences(AppConstant.searchMedicineList,setList);*/

        //getLastLocation();
    }

    private void loadFragment(Fragment fragment, UserLocation userLocationDetails) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("mainScreen","mainScreen");
        bundle.putSerializable("locationId",locationId);
        bundle.putSerializable("userLocationDetails",userLocationDetails);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void init() {
        ivHelp = (ImageView) findViewById(R.id.ivHelp);
        ivNotification = (ImageView) findViewById(R.id.ivNotification);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == -1) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void setListener() {
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if  (locationAvailable!=null && locationAvailable.equals("true")) {
                            loadFragment(new MainFragment());
                        } else {
                            showLocationDialog();
                        }
                        break;
                    case R.id.navigation_orders:
                        if  (locationAvailable!=null && locationAvailable.equals("true")) {
                            loadFragment(new OrdersFragment());
                        } else {
                            showLocationDialog();
                        }
                        break;
                    case R.id.navigation_wallet:
                        if  (locationAvailable!=null && locationAvailable.equals("true")) {
                            loadFragment(new WalletFragment());
                        } else {
                            showLocationDialog();
                        }
                        break;
                    case R.id.navigation_cart:
                        if  (locationAvailable!=null && locationAvailable.equals("true")) {
                            if (AppConstant.cart_item.size() > 0) {
                                loadFragment(new CartFragment());
                            } else {
                                Toast.makeText(MainActivity.this, "Cart is empty.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showLocationDialog();
                        }
                        break;
                }

                return true;
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
    }

    private void showLocationDialog() {
        final Dialog dialog = new Dialog(this);
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
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    /*@Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.llHome:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                //Your Operation
                break;

            case R.id.llRegister:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                //Your Operation
                break;

            case R.id.llAddMedicine:
                closeDrawer();
                loadFragment(new AddMedicineFragment());
                //Your Operation
                break;

            case R.id.llViewPrescription:
                closeDrawer();
                loadFragment(new ViewPrescriptionFragment());
                //Your Operation
                break;

            case R.id.llFindMedical:
                closeDrawer();
                loadFragment(new FindMedicalFragment());
                //Your Operation
                break;

            case R.id.llGooglePlaces:

                //Your Operation
                break;

            case R.id.llMakePayment:
                closeDrawer();
                loadFragment(new MakePaymentsFragment());
                //Your Operation
                break;

            case R.id.llNeedHelp:
                closeDrawer();
                loadFragment(new NeedHelpFragment());
                //Your Operation
                break;

            case R.id.llRefer:
                closeDrawer();
                loadFragment(new ReferEarnFragment());
                //Your Operation
                break;

            case R.id.llLogout:
                startActivity(new Intent(MainActivity.this, RoleSelectionActivity.class));
                //Your Operation
                break;

        }

    }*/

}