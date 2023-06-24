package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.CartItem;
import in.happiness.groceryapp.model.ShopProduct;
import in.happiness.groceryapp.model.StoresProducts;
import in.happiness.groceryapp.model.Variant;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;


public class RelatedProductAdaptar extends RecyclerView.Adapter<RelatedProductAdaptar.ViewHolder>  {
    private ArrayList<ShopProduct> courseModelArrayList;
    private StoresProducts storesProducts;
    private LayoutInflater mInflater;
    private Context context;
    private RelatedProductAdaptar.ItemClickListener mClickListener;
    private ArrayList<String> variantType;
    private Variant variant;
    private String variant_id;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onItemClick(int lastSelectedPosition);
    }

    // data is passed into the constructor
    public RelatedProductAdaptar(ArrayList<ShopProduct> courseModelArrayList, Context context, StoresProducts storesProducts, RelatedProductAdaptar.ItemClickListener mClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.courseModelArrayList=courseModelArrayList;
        this.storesProducts=storesProducts;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public RelatedProductAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_related_product, parent, false);
        return new RelatedProductAdaptar.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull RelatedProductAdaptar.ViewHolder holder, int position) {
        final ShopProduct movie = courseModelArrayList.get(position);
        holder.tvProdName.setText(movie.getProductName());
        holder.tvProdPrice.setText("Rs. "+movie.getSellingPrice());
        Glide.with(context).clear(holder.idIVcourse);
        if (movie.getProduct_image()!=null && movie.getProduct_image()!="" && !movie.getProduct_image().isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(context).load(AppConstant.IMAGE_URL+movie.getProduct_image()).into(holder.idIVcourse);
        } else {
            holder.idIVcourse.setImageResource(R.drawable.category4);
        }
        boolean cartItem=false;
        if (AppConstant.cart_item.size()>0) {
            for (CartItem item : AppConstant.cart_item) {
                if (item.getProduct_user_id()==movie.getId()) {
                    cartItem=true;
                    break;
                }
            }
        }
        if (cartItem) {
            holder.tvCart.setBackgroundColor(Color.parseColor("#EF4712"));
            holder.tvCart.setText("Remove");
        }
        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(position);
            }
        });
     }

    // total number of cells
    @Override
    public int getItemCount() {
        if (courseModelArrayList.size()>3) {
            return 3;
        } else {
            return courseModelArrayList.size();
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProdName, tvProdPrice, tvCart, tvView;
        private ImageView idIVcourse;
        ViewHolder(View itemView) {
            super(itemView);
            tvProdName = itemView.findViewById(R.id.tvProdName);
            tvProdPrice = itemView.findViewById(R.id.tvProdPrice);
            tvProdPrice.setVisibility(View.GONE);
            tvCart = itemView.findViewById(R.id.tvCart);
            tvCart.setVisibility(View.GONE);
            idIVcourse = itemView.findViewById(R.id.idIVcourse);
            tvView = itemView.findViewById(R.id.tvView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lastSelectedPosition = getAdapterPosition();
                    mClickListener.onItemClick(lastSelectedPosition);
                }
            });
        }


    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(courseModelArrayList.get(id));
    }


}