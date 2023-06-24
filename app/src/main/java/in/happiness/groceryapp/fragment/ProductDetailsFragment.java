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
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import in.happiness.groceryapp.R;
import in.happiness.groceryapp.adaptar.ProductImageAdaptar;
import in.happiness.groceryapp.adaptar.RelatedProductAdaptar;
import in.happiness.groceryapp.adaptar.ViewPagerAdapter;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.CartItem;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.ProductImage;
import in.happiness.groceryapp.model.ShopProd;
import in.happiness.groceryapp.model.ShopProduct;
import in.happiness.groceryapp.model.StoreProducts;
import in.happiness.groceryapp.model.Stores;
import in.happiness.groceryapp.model.StoresProducts;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.model.UserProfile;
import in.happiness.groceryapp.model.Variant;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener, RelatedProductAdaptar.ItemClickListener {
    private View v;
    private LinearLayout llCart, llBack, llTopMenu, llProductCart, llVProdDesc;
    private ShopProduct shopProduct;
    private AppSharedPreferences sharedPreferences;
    private String token;
    private ImageView productInc, productDec, ivFavorites;
    private TextView productName, productPrice, productDescription, tvCartStatus, viewAll, tvItemNo, tvCartCount, tvVProdDesc;     //, productQty
    private StoreProducts productDetails;
    private Spinner productVariant;
    private ViewPager imageSlider;
    private Variant variant;
    private String flag;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView rvRelatedProduct;
    private String categoryid;
    private ArrayList<ShopProduct> shopProducts;
    private StoresProducts storesProducts;
    private ShopProd product_details;
    private ArrayList<String>  variantType;
    private ArrayList<SubCategory> subCategories;
    private ArrayList<ShopProduct> related_products;
    private RelatedProductAdaptar storeProductAdaptar;
    private Stores store;
    private boolean valid=false;
    private Category category;
    private ProgressDialog progressDialog;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 8000; // time in milliseconds between successive task executions.
    private int NUM_PAGES;
    private AppDBHelper dbHelper;
    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_product_details, container, false);
        sharedPreferences=new AppSharedPreferences(getActivity());
        dbHelper=new AppDBHelper(getContext());
        init();
        token=sharedPreferences.doGetFromSharedPreferences(AppConstant.token);
        if(getArguments()!=null) {
            shopProduct = (ShopProduct) getArguments().get("selectedProduct");
            getStoreProductDetail(shopProduct);
        }
        tvCartCount.setText(String.valueOf(AppConstant.cart_item.size()));
        llTopMenu.setVisibility(View.GONE);
        setListener();
        return v;
    }

    private void getStoreProductDetail(ShopProduct shopProduct) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(shopProduct.getId()));
        RequestBody search = RequestBody.create(MediaType.parse("text/plain"), "");
        Call<StoreProducts> call = RetrofitClient.getApiInterface().getProductDetails("Bearer "+token, product_id, search);
        call.enqueue(new Callback<StoreProducts>() {
            @Override
            public void onResponse(Call<StoreProducts> call, Response<StoreProducts> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        productDetails=response.body();
                        product_details = productDetails.getProduct_details();
                        productName.setText(product_details.getProductName());
                        if (product_details.getIs_favourite()!=null && !product_details.getIs_favourite().equals("0")) {
                            ivFavorites.setVisibility(View.GONE);
                        }
                        if (product_details.getProduct_image()!=null && product_details.getProduct_image().size()>0) {
                            NUM_PAGES = product_details.getProduct_image().size();
                            imageSlider.setAdapter(new ProductImageAdaptar(getActivity(), product_details.getProduct_image()));
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                    }
                                    Log.i("timer",String.valueOf(System.currentTimeMillis()));
                                    imageSlider.setCurrentItem(currentPage++, true);
                                }
                            };

                            timer = new Timer(); // This will create a new Thread
                            timer.schedule(new TimerTask() { // task to be scheduled
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, DELAY_MS, PERIOD_MS);
//                            imageSlider.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                            imageSlider.setScrollTimeInSec(2); //set scroll delay in seconds :
//                            setLayout(product_details.getProduct_image());
                        } else {
                            imageSlider.setVisibility(View.GONE);
                        }
                        if (product_details.getVariants().size()>0) {
                            variantType = new ArrayList<>();
                            variant=product_details.getVariants().get(0);
                            for ( Variant var : product_details.getVariants()) {
                                variantType.add(var.getQuantity()+" "+var.getUnit());
                                productVariant.setVisibility(View.VISIBLE);
                                productPrice.setVisibility(View.VISIBLE);
                                productPrice.setText("Rs. "+String.valueOf(variant.getSelling_price()));
                                ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,variantType);
                                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //Setting the ArrayAdapter data on the Spinner
                                productVariant.setAdapter(aa);
                            }
                        } else {
                            productPrice.setVisibility(View.GONE);
                        }
                        if (product_details.getVendor_description()!=null) {
                            llVProdDesc.setVisibility(View.VISIBLE);
                            tvVProdDesc.setText(product_details.getVendor_description());
                        }
                        boolean itemAdded=false;
                        String qty="";
                        if (AppConstant.cart_item.size()>0) {
                            for (CartItem item:AppConstant.cart_item) {
                                if (item.getProduct_user_id()==product_details.getId()) {
                                    itemAdded=true;
                                    qty=String.valueOf(item.getQuantity());
                                    break;
                                }
                            }
                        }
                        if (itemAdded) {
                            tvCartStatus.setVisibility(View.GONE);
                            tvItemNo.setVisibility(View.VISIBLE);
                            tvItemNo.setText(qty);
                            productDec.setVisibility(View.VISIBLE);
                            productInc.setVisibility(View.VISIBLE);
                        }
                        if (product_details.getDescription()!=null && product_details.getDescription()!="" && !product_details.getDescription().isEmpty()) {
                            productDescription.setText(product_details.getDescription());
                        } else {
                            productDescription.setText("No description available.");
                        }
                        related_products = new ArrayList<>();
                        ArrayList<ShopProduct> sp = productDetails.getRelated_products();
                        if (sp!=null) {
                            for (ShopProduct s : sp) {
                                if (s.getInStock().equals("1")) {
                                    related_products.add(s);
                                }
                            }
                        }
                        if (related_products.size()>0) {
                            if (related_products.size()>3) {
                                viewAll.setVisibility(View.VISIBLE);
                            }
                            storeProductAdaptar = new RelatedProductAdaptar(related_products,getActivity(), storesProducts, ProductDetailsFragment.this);
                            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            // gridLayoutManager=new GridLayoutManager(getActivity(),3);
                            rvRelatedProduct.setLayoutManager(layoutManager);
                            rvRelatedProduct.setItemAnimator(new DefaultItemAnimator());
                            rvRelatedProduct.setAdapter(storeProductAdaptar);
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
            public void onFailure(Call<StoreProducts> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
                Toast.makeText(getActivity(), "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLayout(List<ProductImage> img_url) {

//        Integer[] images = { R.drawable.category_snacks, R.drawable.product_category_deselected, R.drawable.product_delivery, R.drawable.ic_aboutus};
        for (int i = 0; i < img_url.size(); i++) {

            SliderView sliderView = new SliderView(getActivity());
            sliderView.setImageUrl(AppConstant.IMAGE_URL+img_url.get(i).getImage());

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                }
            });

//            imageSlider.addSliderView(sliderView);
        }
    }

    private void setListener() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ivFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorites(product_details.getId());
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
        tvCartStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvCartStatus.getText().equals("Remove")) {
                    if (product_details.getVariants().size() > 0) {
                        boolean valid = changeItemQtyWithVariant(String.valueOf(product_details.getId()), 1, String.valueOf(variant.getId()), "add");
                        if (valid) {
                            changeItemQtyWithVariant1(String.valueOf(product_details.getId()), "1", String.valueOf(variant.getId()));
                            showAddToCart(1);
                            getCartItems();
                        }
                    } else {
                        changeItemQtyWithoutVariant("add",String.valueOf(product_details.getId()), "1");
                    }
                } else {
                    removeProductFrmCart(String.valueOf(product_details.getId()));
                }
            }
        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        productDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem c=dbHelper.getCartItem(String.valueOf(product_details.getId()), String.valueOf(variant.getId()));
                int count = 0;
                if (c!=null) {
                    count = Integer.valueOf(c.getQuantity())-1;
                }
                if (count==0) {

                    removeProductFrmCart(String.valueOf(product_details.getId()));
                } else {
                    changeItemQtyWithVariant(String.valueOf(product_details.getId()), 1, String.valueOf(variant.getId()), "remove");
                }
                showAddToCart(count);
                getCartItems();
            }
        });
        productInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(tvItemNo.getText().toString());
                ++quantity;
                if (product_details.getVariants().size() > 0) {
                    changeItemQtyWithVariant(String.valueOf(product_details.getId()), 1, String.valueOf(variant.getId()), "add");
                    CartItem c=dbHelper.getCartItem(String.valueOf(product_details.getId()), String.valueOf(variant.getId()));
                    int count = 0;
                    if (c!=null) {
                        count = Integer.valueOf(c.getQuantity());
                    }
                    showAddToCart(count);
                    getCartItems();
                } else {
                    changeItemQtyWithoutVariant("updateQty",String.valueOf(product_details.getId()), String.valueOf(quantity));
                }
            }
        });
        productVariant.setOnItemSelectedListener(this);
    }

    private void showAddToCart(int i) {
        tvCartStatus.setVisibility(View.GONE);
        tvItemNo.setVisibility(View.VISIBLE);
        tvItemNo.setText(String.valueOf(i));
        productDec.setVisibility(View.VISIBLE);
        productInc.setVisibility(View.VISIBLE);
    }

    private boolean changeItemQtyWithVariant (String productId, int qty, String variant_id, String key) {
        CartItem c=dbHelper.getCartItem(productId, variant_id);
        int count = 0;
        if (c!=null) {
            int quantity = 0;
            if (key.equals("remove")) {
                quantity = Integer.valueOf(c.getQuantity()) - 1;
            } else {
                quantity = Integer.valueOf(c.getQuantity()) + 1;
            }
            float total = quantity * c.getPrice();
            count = dbHelper.addToCart("update", productId, quantity, variant_id, total, c);
        } else {
            CartItem cI = dbHelper.getPriceCategory(variant_id);
            float total = qty * cI.getPrice();
            count = dbHelper.addToCart("add", productId, qty, variant_id, total, cI);
        }
        if (count!=0) {
            return true;
        }
        return false;
    }

    private void addToFavorites(int productId) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(productId));
        Call<OTP> call = RetrofitClient.getApiInterface().favorites("Bearer " + AppConstant.userToken, product_id);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        if (response.body().isStatus()) {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            ivFavorites.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
            }
        });
    }

    private void changeItemQtyWithoutVariant(String actionCall, String productId, String qty) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), productId);
        RequestBody quantity = RequestBody.create(MediaType.parse("text/plain"), qty);
        Call<OTP> call = RetrofitClient.getApiInterface().addToCart("Bearer " + AppConstant.userToken, product_id, quantity);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        if (response.body().isStatus()) {
                            if (actionCall.equals("add")) {
                                tvCartStatus.setVisibility(View.GONE);
                                tvItemNo.setVisibility(View.VISIBLE);
                                tvItemNo.setText(qty);
                                productDec.setVisibility(View.VISIBLE);
                                productInc.setVisibility(View.VISIBLE);
                            } else {
                                tvItemNo.setText(qty);
                            }
                            getCartItems();
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG);
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
            }
        });
    }

    private void changeItemQtyWithVariant1(String productId, String qty, String variant) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), productId);
        RequestBody quantity = RequestBody.create(MediaType.parse("text/plain"), qty);
        RequestBody variantId = RequestBody.create(MediaType.parse("text/plain"), variant);
        Call<OTP> call = RetrofitClient.getApiInterface().addToCart("Bearer " + AppConstant.userToken, product_id, quantity, variantId);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("onFailure", t.getMessage());
            }
        });
    }

    private void removeProductFrmCart(String productId) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), productId);
            Call<OTP> call = RetrofitClient.getApiInterface().removeItemCart("Bearer " + AppConstant.userToken, product_id);
            call.enqueue(new Callback<OTP>() {
                @Override
                public void onResponse(Call<OTP> call, Response<OTP> response) {
                    try {
                        if (response.code() == 200) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG);
                            tvCartStatus.setVisibility(View.VISIBLE);
                            tvItemNo.setVisibility(View.GONE);
                            productInc.setVisibility(View.GONE);
                            productDec.setVisibility(View.GONE);
                            int count = dbHelper.deleteProduct(productId, variant.getId());
                            if (count==0) {
                                removeProductFrmCart1(productId, variant.getId());
                            } else {
                                getCartItems();
                            }
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Log.i("error", e.toString());
                    }
                }

                @Override
                public void onFailure(Call<OTP> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.i("onFailure", t.getMessage());
                }
            });

    }

    private void removeProductFrmCart1(String productId, String id) {
        int count = dbHelper.deleteProduct(productId, variant.getId());
        if (count==0) {
            removeProductFrmCart1(productId, variant.getId());
        } else {
            getCartItems();
        }
    }

    private void getCartItems() {
        AppConstant.cart_item = dbHelper.getCartItem();
        tvCartCount.setText(String.valueOf(AppConstant.cart_item.size()));
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
        imageSlider = v.findViewById(R.id.imageSlider);
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        llBack = v.findViewById(R.id.llBack);
        llCart = v.findViewById(R.id.llCart);
        llVProdDesc = v.findViewById(R.id.llVProdDesc);
        tvVProdDesc = v.findViewById(R.id.tvVProdDesc);
        llProductCart = v.findViewById(R.id.llProductCart);
        productName = v.findViewById(R.id.productName);
        productPrice = v.findViewById(R.id.productPrice);
        productDescription= v.findViewById(R.id.productDescription);
        productVariant = v.findViewById(R.id.productVariant);
        ivFavorites = v.findViewById(R.id.ivFavorites);
        productVariant.setVisibility(View.GONE);
        tvCartStatus = v.findViewById(R.id.tvCartStatus);
        tvCartStatus.setVisibility(View.VISIBLE);
        tvItemNo = v.findViewById(R.id.tvItemNo);
        tvItemNo.setVisibility(View.GONE);
        productInc=v.findViewById(R.id.productInc);
        productInc.setVisibility(View.GONE);
        productDec=v.findViewById(R.id.productDec);
        productDec.setVisibility(View.GONE);
        rvRelatedProduct = v.findViewById(R.id.rvRelatedProduct);
        viewAll = v.findViewById(R.id.viewAll);
        viewAll.setVisibility(View.GONE);
        tvCartCount = v.findViewById(R.id.tvCartCount);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (product_details.getVariants().size()>0) {
            variant = product_details.getVariants().get(position);
            productPrice.setText("Rs. "+String.valueOf(variant.getSelling_price()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemClick(int lastSelectedPosition) {
        loadFragment(new ProductDetailsFragment(), related_products.get(lastSelectedPosition));
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


}