package in.happiness.groceryapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.adaptar.MainCategoryAdaptar;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.ShopProduct;
import in.happiness.groceryapp.model.Stores;
import in.happiness.groceryapp.model.StoresProducts;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.model.Variant;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment implements MainCategoryAdaptar.ItemClickListener {
    View v;
    LinearLayout llUploadPresc, llTopMenu, llBack, llClose  ;
    RecyclerView coursesGV;
    MainCategoryAdaptar adapter;
    private List<Category> categories;
    ArrayList<SubCategory> subcategories;
    int numberOfColumns;
    StoresProducts storesProducts;
    ArrayList<ShopProduct> shopProducts;
    Stores store;
    String token;
    boolean flag=false;
    AppSharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private AppDBHelper dbHelper;
    private ArrayList<ShopProduct> shopProds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_category, container, false);
        sharedPreferences=new AppSharedPreferences(getActivity());
        init();
        dbHelper = new AppDBHelper(getContext());
        categories=new ArrayList<>();
        numberOfColumns=2;
        llTopMenu.setVisibility(View.GONE);
        coursesGV = v.findViewById(R.id.idGVcourses);
        token=sharedPreferences.doGetFromSharedPreferences(AppConstant.token);
        if(getArguments()!=null && getArguments().containsKey("store")) {
            store= (Stores) getArguments().get("store");
            flag=true;
            getStoreProducts(store);
        } else {
            flag=false;
            categories = dbHelper.getAllCategory();
            showCategories(categories);
//            getCategories();
        }
        setListener();
        return v;
    }

    private void showCategories(List<Category> categories) {
        if (categories.size()>0) {
            coursesGV.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
            adapter = new MainCategoryAdaptar(getActivity(), categories);
            adapter.setClickListener(CategoryFragment.this);
            coursesGV.setAdapter(adapter);
        }
    }

    private void getStoreProducts(Stores store) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody shop_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(store.getId()));
//        RequestBody shop_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(30));
        Call<StoresProducts> call = RetrofitClient.getApiInterface().getStoreProducts("Bearer "+token, shop_id);
        call.enqueue(new Callback<StoresProducts>() {
            @Override
            public void onResponse(Call<StoresProducts> call, Response<StoresProducts> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        storesProducts=response.body();
                        shopProducts=storesProducts.getShopProducts();
                        categories=storesProducts.getCategories();
                        subcategories=storesProducts.getSubcategories();
                        if (categories.size()>0) {
                            coursesGV.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
                            adapter = new MainCategoryAdaptar(getActivity(), categories);
                            adapter.setClickListener(CategoryFragment.this);
                            coursesGV.setAdapter(adapter);
                        }
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
            public void onFailure(Call<StoresProducts> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void getCategories() {
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.show();
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setContentView(R.layout.progress_dialog);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        Call<ArrayList<Category>> call = RetrofitClient.getApiInterface().getCategories();
//        call.enqueue(new Callback<ArrayList<Category>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
//                try {
//                    if (response.code() == 200) {
//                        progressDialog.dismiss();
//                        categories=response.body();
//                        if (categories.size()>0) {
//                            coursesGV.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
//                            adapter = new MainCategoryAdaptar(getActivity(), categories);
//                            adapter.setClickListener(CategoryFragment.this);
//                            coursesGV.setAdapter(adapter);
//                        }
//                    } else {
//                        progressDialog.dismiss();
//                        Toast.makeText(getActivity(), String.valueOf(response.errorBody()), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    progressDialog.dismiss();
//                    Log.i("error", e.toString());
//                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
//                progressDialog.dismiss();
//                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
//                Log.i("onFailure", t.getMessage());
//                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
//                //progressDialog.dismiss();
//            }
//        });
//
//    }

    private void init(){
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        llBack = v.findViewById(R.id.llBack);
        llClose = v.findViewById(R.id.llClose);
    }

    private void setListener(){
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

    private void loadFragment(Fragment fragment, String id) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryid",id);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onItemClick(View view, int position) {
        if (flag) {
            loadFragment(new ProductListFragment(), String.valueOf(categories.get(position).getId()), String.valueOf(store.getId()));
        } else {
            loadFragment(new StoresFragment(), String.valueOf(categories.get(position).getId()));
        }
    }

    private void loadFragment(Fragment fragment, String categoryId, String storeId) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryid",categoryId);
        bundle.putString("storeId",storeId);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}