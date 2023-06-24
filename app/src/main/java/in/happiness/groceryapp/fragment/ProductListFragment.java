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


import com.google.errorprone.annotations.Var;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.adaptar.StoreProductAdaptar;
import in.happiness.groceryapp.adaptar.SubCategoryAdaptar;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.FavoriteMain;
import in.happiness.groceryapp.model.ShopProduct;
import in.happiness.groceryapp.model.StoresProducts;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.model.Variant;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment  implements StoreProductAdaptar.ItemClickListener, SubCategoryAdaptar.ItemClickListener {
    private View v;
    private LinearLayout llCart, llBack, llTopMenu;
    private AppSharedPreferences sharedPreferences;
    private EditText edProduct;
    private String token, storeId, categoryid;
    private ArrayList<ShopProduct> shopProducts, shopProducts1, shopProducts2;
    private TextView tvProductMsg, tvCartCount;
    private StoresProducts storesProducts;
    private StoreProductAdaptar storeProductAdaptar;
    private SubCategoryAdaptar subCategoryAdaptar;
    private RecyclerView.LayoutManager layoutManager, layoutManagerSubCategory;
    private RecyclerView recyclerViewProduct, recyclerViewSubCategory;
    private ArrayList<ShopProduct> filteredList;
    private ArrayList<SubCategory> subcategories;
    private ArrayList<SubCategory> subCategories1;
    private ProgressDialog progressDialog;
    private AppDBHelper dbHelper;
    private ArrayList<ShopProduct> shopProds;
    public ProductListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_product_list, container, false);
        sharedPreferences=new AppSharedPreferences(getActivity());
        dbHelper = new AppDBHelper(getContext());
        init();
        shopProds = new ArrayList<>();
        shopProducts = new ArrayList<>();
        tvCartCount.setText(String.valueOf(AppConstant.cart_item.size()));
        token=sharedPreferences.doGetFromSharedPreferences(AppConstant.token);
        if(getArguments()!=null) {
            if (getArguments().containsKey("storeId")) {
                storeId= (String) getArguments().get("storeId");
                categoryid = (String) getArguments().get("categoryid");
                shopProds = dbHelper.productList("0", storeId);
                shopProducts1 = new ArrayList<>();
                if (shopProds!=null && shopProds.size()>0) {
                    shopProducts1 = shopProds;
                    shopProducts = shopProducts1;
                    subcategories = dbHelper.getAllSubCategory(storeId);
                    getStoreProducts(categoryid, shopProducts, subcategories);
                }
                getStoreProducts(storeId);
            }
        }
        llTopMenu.setVisibility(View.GONE);
        setListener();
        return v;
    }

    private void getStoreProducts(String categoryid, ArrayList<ShopProduct> sp, ArrayList<SubCategory> subcategories) {
        subCategories1 = new ArrayList<>();
        boolean valid=true;
        for (SubCategory cat: subcategories) {
            cat.setActive("0");
            if (valid) {
                SubCategory sub_category = new SubCategory();
                sub_category.setCategoryId(0);
                sub_category.setId(0);
                sub_category.setActive("1");
                sub_category.setName("All");
                subCategories1.add(sub_category);
                valid=!valid;
            }
            subCategories1.add(cat);
        }
        if (sp.size()>0) {
            filteredList = new ArrayList<>();
            filteredList.addAll(sp);
            assignSubCategory(subCategories1);
            assignProductList(filteredList);
            getFavoritesList();
            tvProductMsg.setVisibility(View.GONE);
        }else {
            tvProductMsg.setVisibility(View.VISIBLE);
            recyclerViewSubCategory.setVisibility(View.GONE);
            recyclerViewProduct.setVisibility(View.GONE);
        }
    }

    private void getFavoritesList() {
        Call<FavoriteMain> call = RetrofitClient.getApiInterface().favoriteList("Bearer "+token);
        call.enqueue(new Callback<FavoriteMain>() {
            @Override
            public void onResponse(Call<FavoriteMain> call, Response<FavoriteMain> response) {
                try {
                    if (response.code() == 200) {
                    } else {
                        Toast.makeText(getActivity(), String.valueOf(response.errorBody()), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FavoriteMain> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void assignSubCategory(ArrayList<SubCategory> subCategories1) {
        subCategoryAdaptar = new SubCategoryAdaptar(subCategories1, getActivity(), this);
        layoutManagerSubCategory = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        // gridLayoutManager=new GridLayoutManager(getActivity(),3);
        recyclerViewSubCategory.setLayoutManager(layoutManagerSubCategory);
        recyclerViewSubCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSubCategory.setAdapter(subCategoryAdaptar);
    }

    private void assignProductList(ArrayList<ShopProduct> productsList) {
        storeProductAdaptar = new StoreProductAdaptar(productsList,getActivity(), storesProducts, this, dbHelper);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        // gridLayoutManager=new GridLayoutManager(getActivity(),3);
        recyclerViewProduct.setLayoutManager(layoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(storeProductAdaptar);
    }

    private void getStoreProducts(String storeId) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody shop_id = RequestBody.create(MediaType.parse("text/plain"), storeId);
//        RequestBody shop_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(30));
        Call<StoresProducts> call = RetrofitClient.getApiInterface().getStoreProducts("Bearer "+token, shop_id);
        call.enqueue(new Callback<StoresProducts>() {
            @Override
            public void onResponse(Call<StoresProducts> call, Response<StoresProducts> response) {
                try {
                    if (response.code() == 200) {
                        storesProducts=response.body();
                        ArrayList<ShopProduct> sp = storesProducts.getShopProducts();
                        shopProducts2 = new ArrayList<>();
                        for (ShopProduct s : sp) {
                            if (s.getInStock().equals("1")) {
                                if (s.getVariants()==null) {
                                    s.variants=new ArrayList<>();
                                }
                                shopProducts2.add(s);
                            }
                        }
                        if (shopProducts1.size()!=shopProducts2.size()) {
                            subcategories = new ArrayList<>();
                            subcategories=storesProducts.getSubcategories();
                            shopProducts = new ArrayList<>();
                            shopProducts = shopProducts2;
                            getStoreProducts(categoryid, shopProducts, subcategories);
                        }
                        progressDialog.dismiss();
                        for (ShopProduct s: sp) {
                            ArrayList<ShopProduct> s1 = dbHelper.productList(String.valueOf(s.getId()),String.valueOf(s.getUserId()));
                            if (s1.size()>0) {
                                dbHelper.addProducts(s,"update");
                            } else {
                                dbHelper.addProducts(s,"add");
                            }
                            for (Variant v:s.getVariants()) {
                                ArrayList<Variant> variant = dbHelper.getVariant(v.getId(),"variant_id");
                                if (variant.size()>0) {
                                    dbHelper.addVariants(v,"update");
                                } else {
                                    dbHelper.addVariants(v,"add");
                                }
                            }
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


    private void setListener() {
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
        edProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(storeProductAdaptar != null) {
                    if(shopProducts.size()>0) {
                        filteredList=new ArrayList<>();
                        for (ShopProduct prod : shopProducts) {
                            if (prod.getProductName().toLowerCase().contains(s.toString().toLowerCase())) {
                                filteredList.add(prod);
                            }
                        }
                        assignProductList(filteredList);
//                        filteredList.addAll(productsList);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        llCart = v.findViewById(R.id.llCart);
        tvProductMsg=v.findViewById(R.id.tvProductMsg);
        tvCartCount=v.findViewById(R.id.tvCartCount);
        tvProductMsg.setVisibility(View.GONE);
        edProduct = v.findViewById(R.id.edProduct);
        recyclerViewProduct=v.findViewById(R.id.recyclerViewProduct);
        recyclerViewSubCategory=v.findViewById(R.id.recyclerViewSubCategory);
    }

    @Override
    public void onItemClick(View view, int position) {
//        loadFragment(new ProductDetailsFragment(), categoryid, shopProducts, storesProducts, productsList.get(position));
    }

    @Override
    public void onItemClick(int lastSelectedPosition) {
        if (filteredList.get(lastSelectedPosition).getIs_available()==1) {
            loadFragment(new ProductDetailsFragment(), filteredList.get(lastSelectedPosition));
        } else {
            Toast.makeText(getActivity(), "Product not available right now.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment, ShopProduct shopProduct) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedProduct", shopProduct);
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void subCategoryId(int lastSelectedPosition) {
        int subCategoryId = (int) subCategories1.get(lastSelectedPosition).getId();
        for (int i=0;i<subCategories1.size(); i++) {
            subCategories1.get(i).setActive("0");
        }
        subCategories1.get(lastSelectedPosition).setActive("1");
        assignSubCategory(subCategories1);
        filteredList = new ArrayList<>();
        if (String.valueOf(subCategoryId).equals("0")) {
            filteredList=shopProducts;
        } else {
            for (ShopProduct products : shopProducts) {
                if (products.getSubcategory_id() == subCategoryId) {
                    filteredList.add(products);
                }
            }
        }
        assignProductList(filteredList);
    }

    @Override
    public void setCartCount(String value) {
        tvCartCount.setText(value);
    }
}