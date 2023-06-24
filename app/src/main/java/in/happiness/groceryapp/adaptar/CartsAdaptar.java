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


import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartsAdaptar extends RecyclerView.Adapter<CartsAdaptar.ViewHolder> {
    private ArrayList<CartItem> cartItems;
    private StoresProducts storesProducts;
    private LayoutInflater mInflater;
    private Context context;
    private CartsAdaptar.ItemClickListener mClickListener;
    private ProgressDialog progressDialog;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void subCategoryId(int lastSelectedPosition);
    }

    // data is passed into the constructor
    public CartsAdaptar(ArrayList<CartItem> cartItems, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.cartItems=cartItems;
        this.storesProducts=storesProducts;
        this.context=context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public CartsAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.carts_list, parent, false);
        return new CartsAdaptar.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartsAdaptar.ViewHolder holder, int position) {
        final CartItem movie = cartItems.get(position);
        holder.tvTotalProdPrice.setText(String.valueOf(movie.getSubtotal()));
        holder.tvQty.setText(String.valueOf(movie.getQuantity()));
        holder.tvProdPrice.setText(String.valueOf(movie.getPrice()));
        holder.tvProdName.setText(movie.getProduct_name());
        holder.llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer val = Integer.valueOf(holder.tvQty.getText().toString());
                addToCart(movie.getProduct_user_id(), movie.getVariant_id(), ++val);
            }
        });
        holder.llRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer val = Integer.valueOf(holder.tvQty.getText().toString());
                if (val==1) {
                    removeItem(movie.getProduct_user_id());
                } else {
                    addToCart(movie.getProduct_user_id(), movie.getVariant_id(), --val);
                }
            }
        });
        holder.llRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(movie.getProduct_user_id());
            }
        });
    }

    private void addToCart(int product_user_id, int variant_id, int prod_qty) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(product_user_id));
        RequestBody quantity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(prod_qty));
        RequestBody variantId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(variant_id));
        Call<OTP> call = RetrofitClient.getApiInterface().addToCart("Bearer " + AppConstant.userToken, product_id, quantity, variantId);
        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        if (response.body().isStatus()) {
                            getCartItems();
                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG);
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
                        getCartItems();
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

    private void getCartItems() {
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
        LinearLayout llRemove, llAdd, llRemoveItem;
        ViewHolder(View itemView) {
            super(itemView);
            tvTotalProdPrice=itemView.findViewById(R.id.tvTotalProdPrice);
            tvTotalProdPrice.setVisibility(View.GONE);
            tvQty=itemView.findViewById(R.id.tvQty);
//            tvQty.setVisibility(View.GONE);
            tvProdPrice=itemView.findViewById(R.id.tvProdPrice);
            tvProdPrice.setVisibility(View.GONE);
            tvProdName=itemView.findViewById(R.id.tvProdName);
            llRemove=itemView.findViewById(R.id.llRemove);
            llRemove.setVisibility(View.GONE);
            llAdd=itemView.findViewById(R.id.llAdd);
            llAdd.setVisibility(View.GONE);
            llRemoveItem=itemView.findViewById(R.id.llRemoveItem);
            llRemoveItem.setVisibility(View.GONE);
        }


    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(cartItems.get(id));
    }


}