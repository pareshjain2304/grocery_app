package in.happiness.groceryapp.adaptar;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.utils.AppDBHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.CartItem;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.ProductTime;
import in.happiness.groceryapp.model.ShopProduct;
import in.happiness.groceryapp.model.StoresProducts;
import in.happiness.groceryapp.model.UserProfile;
import in.happiness.groceryapp.model.Variant;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class StoreProductAdaptar extends RecyclerView.Adapter<StoreProductAdaptar.ViewHolder>  {
    private ArrayList<ShopProduct> courseModelArrayList;
    private StoresProducts storesProducts;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;
    private ArrayList<String> variantType;
    private Variant variant;
    private boolean valid=false;
    private AppDBHelper dbHelper;
    private String message="";
    private ProgressDialog progressDialog;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void setCartCount(String value);
        void onItemClick(int lastSelectedPosition);
//        void addToCart(int position,int variantId);
//        void removeFromCart(int position, int variantId);
    }

    // data is passed into the constructor
    public StoreProductAdaptar(ArrayList<ShopProduct> courseModelArrayList, Context context, StoresProducts storesProducts, ItemClickListener mClickListener, AppDBHelper dbHelper) {
        this.mInflater = LayoutInflater.from(context);
        this.courseModelArrayList=courseModelArrayList;
        this.storesProducts=storesProducts;
        this.context=context;
        this.mClickListener=mClickListener;
        this.dbHelper=dbHelper;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public StoreProductAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_products, parent, false);
        return new StoreProductAdaptar.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull StoreProductAdaptar.ViewHolder holder, int position) {
        ShopProduct movie = courseModelArrayList.get(position);
        boolean cartItem=false;
        holder.tvCartStatus.setVisibility(View.VISIBLE);
        holder.tvItemNo.setVisibility(View.GONE);
        holder.productDec.setVisibility(View.GONE);
        holder.productInc.setVisibility(View.GONE);
        String qty="";
        holder.productName.setText(movie.getProductName());
        holder.tvVariantId.setText("");
        holder.productPrice.setText("");
        Glide.with(context).clear(holder.idIVcourse);
        if (movie.getIs_available()==0) {

        }
        if (movie.getProduct_image()!=null && movie.getProduct_image()!="" && !movie.getProduct_image().isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(context).load(AppConstant.IMAGE_URL+movie.getProduct_image()).into(holder.idIVcourse);
        } else {
            holder.idIVcourse.setImageResource(R.drawable.category4);
        }
        Variant v = movie.getVariants().get(0);
//        holder.productDescription.setText(movie.getDescription());
        if (movie.getVariants().size()>0) {
            holder.simpleSpinner.setVisibility(View.VISIBLE);
            if (movie.getVariants().size()>0) {
                variantType = new ArrayList<>();
                for ( Variant var : movie.getVariants()) {
                    variantType.add(var.getQuantity()+" "+var.getUnit());
                    ArrayAdapter aa = new ArrayAdapter(context,android.R.layout.simple_spinner_item,variantType);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    holder.simpleSpinner.setAdapter(aa);
                }
            }
        } else {
            holder.simpleSpinner.setVisibility(View.GONE);
        }

        holder.simpleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (movie.getVariants().size()>0) {
                    variant = new Variant();
                    variant = movie.getVariants().get(pos);
                    holder.tvVariantId.setText(variant.getId());
                    holder.productPrice.setText("Rs "+String.valueOf(variant.getSelling_price()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.productDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = false;
                Integer value = Integer.valueOf(dbHelper.getQuantity(String.valueOf(movie.getId()), holder.tvVariantId.getText().toString()));
                if (value > 1) {
                        if (holder.tvVariantId.getText().toString().equals("")) {
                            holder.tvVariantId.setText(movie.getVariants().get(0).getId());
                            holder.productPrice.setText(movie.getVariants().get(0).getPrice());
                        }
                        valid = changeItemQtyWithVariant(String.valueOf(movie.getId()), 0, holder.tvVariantId.getText().toString(), "remove");
                    if (valid) {
                        String quantity = dbHelper.getQuantity(String.valueOf(movie.getId()), holder.tvVariantId.getText().toString());
                        holder.tvItemNo.setText(quantity);
                        getCartItems();
                    }
                } else {
                    valid = removeProductFrmCart(String.valueOf(movie.getId()), holder.tvVariantId.getText().toString());
                    if (valid) {
                        holder.tvItemNo.setVisibility(View.GONE);
                        holder.productDec.setVisibility(View.GONE);
                        holder.productInc.setVisibility(View.GONE);
                        holder.tvCartStatus.setVisibility(View.VISIBLE);
                        holder.llProductCart.setBackgroundColor(Color.parseColor("#8DCA08"));
                        holder.tvCartStatus.setText("Add to Cart");
                        getCartItems();
                        removeProductFrmCart1(String.valueOf(movie.getId()));
                    }
                }
            }
        });
        holder.productInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = false;
                if (movie.getVariants().size() > 0) {
                    if (holder.tvVariantId.getText().toString().equals("")) {
                        holder.tvVariantId.setText(movie.getVariants().get(0).getId());
                        holder.productPrice.setText(movie.getVariants().get(0).getPrice());
                    }
                    valid = changeItemQtyWithVariant(String.valueOf(movie.getId()), 0, holder.tvVariantId.getText().toString(),"add");
                } else {
//                    changeItemQtyWithoutVariant(String.valueOf(movie.getId()), String.valueOf(val));
                }
                if (valid) {
                    String quantity = dbHelper.getQuantity(String.valueOf(movie.getId()), holder.tvVariantId.getText().toString());
                    holder.tvItemNo.setText(quantity);
                    getCartItems();
                } else {
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.tvCartStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = false;
                if (holder.tvCartStatus.getText().equals("Add to Cart")) {
                    if (movie.getVariants().size() > 0) {
                        if (holder.tvVariantId.getText().toString().equals("")) {
                            holder.tvVariantId.setText(movie.getVariants().get(0).getId());
                            holder.productPrice.setText(movie.getVariants().get(0).getPrice());
                        }
                        valid = changeItemQtyWithVariant(String.valueOf(movie.getId()), 1, holder.tvVariantId.getText().toString(), "add");
                    } else {
//                        changeItemQtyWithoutVariant(String.valueOf(movie.getId()), "1");
                    }
                    if (valid) {
                        String quantity = dbHelper.getQuantity(String.valueOf(movie.getId()), holder.tvVariantId.getText().toString());
                        holder.tvItemNo.setVisibility(View.VISIBLE);
                        holder.tvItemNo.setText(quantity);
                        holder.productDec.setVisibility(View.VISIBLE);
                        holder.productInc.setVisibility(View.VISIBLE);
                        holder.tvCartStatus.setVisibility(View.GONE);
                        getCartItems();
                        changeItemQtyWithVariant1(String.valueOf(movie.getId()), "1", holder.tvVariantId.getText().toString());
                    } else {
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    valid = removeProductFrmCart(String.valueOf(movie.getId()), holder.tvVariantId.getText().toString());
                    if (valid) {
                        holder.llProductCart.setBackgroundColor(Color.parseColor("#8DCA08"));
                        holder.tvCartStatus.setText("Add to Cart");
                        getCartItems();
                        holder.changeView();
                        removeProductFrmCart1(String.valueOf(movie.getId()));
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (movie.getIs_available()==1) {
            holder.rlAvailable.setVisibility(View.VISIBLE);
            AppConstant.cart_item = dbHelper.getCartItem();
            if (AppConstant.cart_item.size()>0) {
                for (CartItem item : AppConstant.cart_item) {
                    if (item.getProduct_user_id() == courseModelArrayList.get(position).getId()) {
                        cartItem = true;
                        qty = String.valueOf(item.getQuantity());
                        break;
                    }
                }
            }
            if (cartItem) {
                holder.tvCartStatus.setVisibility(View.GONE);
                holder.tvItemNo.setVisibility(View.VISIBLE);
                holder.tvItemNo.setText(qty);
                holder.productDec.setVisibility(View.VISIBLE);
                holder.productInc.setVisibility(View.VISIBLE);
            }
        } else {
            String msg = "Product available between ";
            int i=0;
            for (ProductTime pT:movie.getProduct_time()) {
                String s="";
                if (i!=0) {
                    s=" and ";
                }
                msg=msg+s+pT.getOpen_time()+"-"+pT.getClose_time();
                ++i;
            }
            holder.tvPTime.setVisibility(View.VISIBLE);
            holder.tvPTime.setText(msg);
        }
    }

    private boolean removeProductFrmCart(String productId, String variantId) {
        int count = dbHelper.deleteProduct(productId, variantId);
        if (count==1) {
            return true;
        }
        return false;
    }

    private boolean removeProductFrmCart1(String productId) {
        valid=false;
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), productId);
        Call<OTP> call = RetrofitClient.getApiInterface().removeItemCart("Bearer " + AppConstant.userToken, product_id);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body().isStatus()) {
                            valid = true;
                        } else {
                            message = response.body().getMessage();
                        }
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
            }
        });
        return valid;
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

    private boolean changeItemQtyWithVariant1(String productId, String qty, String variant_id) {
        valid=false;
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), productId);
        RequestBody quantity = RequestBody.create(MediaType.parse("text/plain"), qty);
        RequestBody variantId = RequestBody.create(MediaType.parse("text/plain"), variant_id);
        Call<OTP> call = RetrofitClient.getApiInterface().addToCart("Bearer " + AppConstant.userToken, product_id, quantity, variantId);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body().isStatus()) {
                            valid = true;
                        } else {
                            message = response.body().getMessage();
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
            }
        });
        return valid;
    }

    private boolean changeItemQtyWithoutVariant(String productId, String qty) {
        return false;
    }

    private boolean changeItemQtyWithoutVariant1(String productId, String qty) {
        valid=false;
        progressDialog = new ProgressDialog(context);
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
                            valid = true;
                        } else {
                            message = response.body().getMessage();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG);
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
        return valid;
    }

    private void getCartItems() {
        AppConstant.cart_item = dbHelper.getCartItem();
        mClickListener.setCartCount(String.valueOf(AppConstant.cart_item.size()));
    }
//    private void getCartItems1() {
//        progressDialog = new ProgressDialog(context);
//        progressDialog.show();
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setContentView(R.layout.progress_dialog);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        Call<UserProfile> call = RetrofitClient.getApiInterface().getCartItem("Bearer "+AppConstant.userToken);
//        call.enqueue(new Callback<UserProfile>() {
//            @Override
//            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
//                try {
//                    if (response.code() == 200) {
//                        progressDialog.dismiss();
//                        AppConstant.userProfile=response.body();
//                        AppConstant.cart_item = AppConstant.userProfile.getCart_items();
//                        mClickListener.setCartCount(String.valueOf(AppConstant.cart_item.size()));
//                    } else {
//                        progressDialog.dismiss();
//                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    progressDialog.dismiss();
//                    Log.i("error", e.toString());
//                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserProfile> call, Throwable t) {
//                progressDialog.dismiss();
//                //  Log.e(AppConstant.TAG, ":onFailure" + t.getMessage());
//                Log.i("onFailure", t.getMessage());
//                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
//                //progressDialog.dismiss();
//            }
//        });
//    }

    private void loadFragment(Fragment fragment, ShopProduct product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("product",product);
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private boolean loadFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();

        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) {
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_home, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    //  .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,  productPrice, tvCartStatus, tvItemNo, tvVariantId, tvCartCount, tvPTime;   //productDescription,
        LinearLayout llProductCart;
        ImageView productDec, productInc, idIVcourse;
        RelativeLayout rlAvailable;
        private Spinner simpleSpinner;
        ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
//            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            llProductCart = itemView.findViewById(R.id.llProductCart);
            idIVcourse = itemView.findViewById(R.id.idIVcourse);
            tvCartStatus = itemView.findViewById(R.id.tvCartStatus);
            simpleSpinner = itemView.findViewById(R.id.simpleSpinner);
            rlAvailable = itemView.findViewById(R.id.rlAvailable);
            tvItemNo = itemView.findViewById(R.id.tvItemNo);
            tvItemNo.setVisibility(View.GONE);
            productDec = itemView.findViewById(R.id.productDec);
            productDec.setVisibility(View.GONE);
            productInc = itemView.findViewById(R.id.productInc);
            productInc.setVisibility(View.GONE);
            tvVariantId = itemView.findViewById(R.id.tvVariantId);
            tvVariantId.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lastSelectedPosition = getAdapterPosition();
                    mClickListener.onItemClick(lastSelectedPosition);
                }
            });
            tvPTime=itemView.findViewById(R.id.tvPTime);
        }


        public void changeView() {
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(courseModelArrayList.get(id));
    }


}