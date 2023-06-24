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
import in.happiness.groceryapp.model.Stores;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.ArrayList;

public class StoresAdaptar extends RecyclerView.Adapter<StoresAdaptar.ViewHolder> {
    private ArrayList<Stores> courseModelArrayList;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public StoresAdaptar(ArrayList<Stores> courseModelArrayList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.courseModelArrayList = courseModelArrayList;
        this.context=context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClickStores(View view, int position);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public StoresAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_stores, parent, false);
        return new StoresAdaptar.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull StoresAdaptar.ViewHolder holder, int position) {
        final Stores movie = courseModelArrayList.get(position);

        holder.storeName.setText(movie.getShopName());
        if (movie.getDistance()!=null) {
            if (movie.getDistance().length()>3) {
                holder.tvDistance.setText(movie.getDistance().substring(0, 3) + " Km");
            } else {
                holder.tvDistance.setText(movie.getDistance() + " Km");
            }
        } else {
            holder.tvDistance.setText("Distance Not Available.");
        }
        Glide.with(context).clear(holder.idIVcourse);
        if (movie.getShopImage()!=null && movie.getShopImage()!="" && !movie.getShopImage().isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(context).load(AppConstant.IMAGE_URL+movie.getShopImage()).into(holder.idIVcourse);
        } else {
            holder.idIVcourse.setImageResource(R.drawable.category4);
        }
        if (movie.getShopOnline().equals("1")) {
            holder.tvShopStatus.setText("Online");
            holder.tvShopStatus.setTextColor(Color.parseColor("#41d400"));
        } else {
            holder.tvShopStatus.setText("Currently store is unable to accept orders");
            holder.tvShopStatus.setTextColor(Color.parseColor("#fe0002"));
        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView storeName, tvDistance, tvShopStatus;
        ImageView idIVcourse;

        ViewHolder(View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.storeName);
            idIVcourse = itemView.findViewById(R.id.idIVcourse);
            tvShopStatus = itemView.findViewById(R.id.tvShopStatus);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClickStores(v, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(courseModelArrayList.get(id));
    }


}