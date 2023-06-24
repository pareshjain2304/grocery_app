package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.OrderStatus;

import java.util.ArrayList;


public class OrderStatusAdaptar  extends RecyclerView.Adapter<OrderStatusAdaptar.ViewHolder> {

    private ArrayList<OrderStatus> courseModelArrayList;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void orderStatus(int lastSelectedPosition);
    }

    // data is passed into the constructor
    public OrderStatusAdaptar(ArrayList<OrderStatus> courseModelArrayList, Context context, ItemClickListener mClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.courseModelArrayList=courseModelArrayList;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public OrderStatusAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_order_status, parent, false);
        return new OrderStatusAdaptar.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusAdaptar.ViewHolder holder, int position) {
        final OrderStatus movie = courseModelArrayList.get(position);
        holder.subCategoryName.setText(movie.getOrderStatus());
        if (movie.isSelected()) {
            holder.llSubCategory.setBackgroundResource(R.drawable.background_green_capsule);
        } else {
            holder.llSubCategory.setBackgroundResource(R.drawable.ic_product_container);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subCategoryName;
        LinearLayout llSubCategory;
        ViewHolder(View itemView) {
            super(itemView);
            subCategoryName = itemView.findViewById(R.id.subCategoryName);
            llSubCategory = itemView.findViewById(R.id.llSubCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition=getAdapterPosition();
                    mClickListener.orderStatus(getPosition);
                }
            });
        }


    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(courseModelArrayList.get(id));
    }
}