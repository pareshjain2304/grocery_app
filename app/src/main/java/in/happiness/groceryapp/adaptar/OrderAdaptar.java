package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.fragment.FeedBackFormFragment;
import in.happiness.groceryapp.fragment.OrderDetailsFragment;
import in.happiness.groceryapp.fragment.TrackOrderFragment;
import in.happiness.groceryapp.model.Order;

import java.util.ArrayList;


public class OrderAdaptar extends RecyclerView.Adapter<OrderAdaptar.ViewHolder> {
    private ArrayList<Order> orders;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void subCategoryId(int lastSelectedPosition);
    }

    // data is passed into the constructor
    public OrderAdaptar(ArrayList<Order> orders, Context context, ItemClickListener mClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.orders=orders;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public OrderAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_order, parent, false);
        return new OrderAdaptar.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdaptar.ViewHolder holder, int position) {
        final Order movie = orders.get(position);
        holder.tvOrderNumber.setText(movie.getOrderNumber());
        holder.tvOrderTime.setText(movie.getOrderDate());
        if (movie.getGrandTotal()!=null) {
            holder.tvTotal.setText(movie.getGrandTotal());
        }
        holder.tvFeedback.setVisibility(View.GONE);
        if (movie.getStatus().toLowerCase().equals("delivered") && movie.getFeedbackAvailable().equals("0")) {
            holder.tvFeedback.setVisibility(View.VISIBLE);
        }
        holder.tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FeedBackFormFragment(), movie.getId());
            }
        });
        holder.tvOrderStatus.setText(movie.getStatus().toUpperCase());
        if (movie.getStatus().toLowerCase().equals("pending") || movie.getStatus().toLowerCase().equals("cancelled")
                || movie.getStatus().toLowerCase().equals("rejected")) {
            holder.llTrackOrder.setVisibility(View.GONE);
        } else {
            holder.llTrackOrder.setVisibility(View.VISIBLE);
        }
        holder.llTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new TrackOrderFragment(), movie.getId());
            }
        });
        holder.tvViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new OrderDetailsFragment(), movie.getId());
            }
        });
    }

    private void loadFragment(Fragment fragment, String order_id) {
        Bundle bundle = new Bundle();
        bundle.putString("order_id",order_id);
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
        return orders.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvOrderTime, tvTotal, tvOrderStatus, tvViewDetail, tvFeedback;
        LinearLayout llTrackOrder;
        ViewHolder(View itemView) {
            super(itemView);
            tvOrderNumber=itemView.findViewById(R.id.tvOrderNumber);
            tvOrderTime=itemView.findViewById(R.id.tvOrderTime);
            tvTotal=itemView.findViewById(R.id.tvTotal);
            tvOrderStatus=itemView.findViewById(R.id.tvOrderStatus);
            llTrackOrder=itemView.findViewById(R.id.llTrackOrder);
            tvViewDetail=itemView.findViewById(R.id.tvViewDetail);
            tvFeedback=itemView.findViewById(R.id. tvFeedback);
            tvFeedback.setVisibility(View.GONE);
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
        return String.valueOf(orders.get(id));
    }


}