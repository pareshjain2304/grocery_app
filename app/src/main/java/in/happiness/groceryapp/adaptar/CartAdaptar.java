package in.happiness.groceryapp.adaptar;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.fragment.CartFragment;
import in.happiness.groceryapp.fragment.MainFragment;
import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.CartItem;
import in.happiness.groceryapp.model.OTP;
import in.happiness.groceryapp.model.StoresProducts;
import in.happiness.groceryapp.model.UserProfile;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;


import in.happiness.groceryapp.utils.AppDBHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdaptar extends RecyclerView.Adapter<CartAdaptar.ViewHolder> {
    private ArrayList<CartItem> cartItems;
    private StoresProducts storesProducts;
    private LayoutInflater mInflater;
    private Context context;
    private CartAdaptar.ItemClickListener mClickListener;
    private ProgressDialog progressDialog;
    private AppDBHelper dbHelper;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void subCategoryId(int lastSelectedPosition);
    }

    // data is passed into the constructor
    public CartAdaptar(ArrayList<CartItem> cartItems, Context context, CartAdaptar.ItemClickListener mClickListener, AppDBHelper dbHelper) {
        this.mInflater = LayoutInflater.from(context);
        this.cartItems=cartItems;
        this.storesProducts=storesProducts;
        this.context=context;
        this.mClickListener=mClickListener;
        this.dbHelper=dbHelper;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public CartAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cart_list, parent, false);
        return new CartAdaptar.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdaptar.ViewHolder holder, int position) {
        final CartItem movie = cartItems.get(position);
        holder.tvTotalProdPrice.setText(String.valueOf(movie.getSubtotal()));
        holder.tvQty.setText(String.valueOf(movie.getQuantity()));
        holder.tvProdPrice.setText(String.valueOf(movie.getPrice()));
        holder.tvProdName.setText(movie.getProduct_name());
        holder.llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(movie.getProduct_user_id(), movie.getVariant_id(), "add");
                getCartItems();
            }
        });
        holder.llRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = dbHelper.getQuantity(String.valueOf(movie.getProduct_user_id()), String.valueOf(movie.getVariant_id()));
                if (Integer.valueOf(val)==1) {
                    removeItem(movie.getProduct_user_id());
                    dbHelper.deleteProduct(String.valueOf(movie.getProduct_user_id()), String.valueOf(movie.getVariant_id()));
                } else {
                    addToCart(movie.getProduct_user_id(), movie.getVariant_id(), "remove");
                }
                getCartItems();
            }
        });
        holder.llRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(movie.getProduct_user_id());
                dbHelper.deleteProduct(String.valueOf(movie.getProduct_user_id()), String.valueOf(movie.getVariant_id()));
                getCartItems();
            }
        });
    }

    private void getCartItems() {
        AppConstant.cart_item = dbHelper.getCartItem();
        if (AppConstant.cart_item.size()>0) {
            loadFragment(new CartFragment());
        } else {
            loadFragment(new MainFragment());
        }
    }

    private void addToCart(int product_user_id, int variant_id, String key) {
        CartItem c=dbHelper.getCartItem(String.valueOf(product_user_id), String.valueOf(variant_id));
        int count = 0;
        if (c!=null) {
            int quantity = 0;
            if (key.equals("remove")) {
                quantity = Integer.valueOf(c.getQuantity()) - 1;
            } else {
                quantity = Integer.valueOf(c.getQuantity()) + 1;
            }
            float total = quantity * c.getPrice();
            dbHelper.addToCart("update", String.valueOf(product_user_id), quantity, String.valueOf(variant_id), total, c);
            addToCart1(product_user_id, variant_id, quantity);
        }
    }

    private void addToCart1(int product_user_id, int variant_id, int prod_qty) {
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(product_user_id));
        RequestBody quantity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(prod_qty));
        RequestBody variantId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(variant_id));
        Call<OTP> call = RetrofitClient.getApiInterface().addToCart("Bearer " + AppConstant.userToken, product_id, quantity, variantId);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body().isStatus()) {
                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG);
                        }
                    } else {
                    }
                } catch (Exception e) {
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

    private void removeItem(int id) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
        Call<OTP> call = RetrofitClient.getApiInterface().removeItemCart("Bearer " + AppConstant.userToken, product_id);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
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
    }

    private void getCartItems1() {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<UserProfile> call = RetrofitClient.getApiInterface().getCartItem("Bearer "+AppConstant.userToken);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        AppConstant.userProfile=response.body();
                        AppConstant.cart_item = AppConstant.userProfile.getCart_items();
                        if (AppConstant.cart_item.size()>0) {
                            loadFragment(new CartFragment());
                        } else {
                            loadFragment(new MainFragment());
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.i("error", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
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
        return cartItems.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalProdPrice, tvQty, tvProdPrice, tvProdName;
        LinearLayout llSubCategory, llRemove, llAdd, llRemoveItem;
        ViewHolder(View itemView) {
            super(itemView);
            tvTotalProdPrice=itemView.findViewById(R.id.tvTotalProdPrice);
            tvQty=itemView.findViewById(R.id.tvQty);
            tvProdPrice=itemView.findViewById(R.id.tvProdPrice);
            tvProdName=itemView.findViewById(R.id.tvProdName);
            llSubCategory = itemView.findViewById(R.id.llSubCategory);
            llRemove=itemView.findViewById(R.id.llRemove);
            llAdd=itemView.findViewById(R.id.llAdd);
            llRemoveItem=itemView.findViewById(R.id.llRemoveItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition=getAdapterPosition();
                    mClickListener.subCategoryId(getPosition);
                }
            });
        }


    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(cartItems.get(id));
    }


}