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
import in.happiness.groceryapp.model.OrderItems;

import java.util.ArrayList;


public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private ArrayList<OrderItems> orders;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void subCategoryId(int lastSelectedPosition);
    }

    // data is passed into the constructor
    public OrderItemAdapter(ArrayList<OrderItems> orders, Context context, ItemClickListener mClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.orders=orders;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_order_items, parent, false);
        return new OrderItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {
        final OrderItems movie = orders.get(position);
        holder.itemName.setText(movie.getProductName()+" x "+movie.getQuantity());
        int total = Integer.valueOf(movie.getPrice())*Integer.valueOf(movie.getQuantity());
        holder.itemPrice.setText(String.valueOf(total));
        holder.itemquantity.setText(movie.getUnit());
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
        TextView itemName, itemPrice, itemquantity;
        LinearLayout llTrackOrder;
        ViewHolder(View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.itemName);
            itemPrice=itemView.findViewById(R.id.itemPrice);
            itemquantity=itemView.findViewById(R.id.itemquantity);
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(orders.get(id));
    }


}
