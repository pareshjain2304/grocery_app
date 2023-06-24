package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.fragment.OrderDetailsFragment;
import in.happiness.groceryapp.model.NotificationBody;
import in.happiness.groceryapp.model.NotificationData;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdaptar extends RecyclerView.Adapter<NotificationAdaptar.MyViewHolder> {
    private List<NotificationBody> SelectBrandList;
    private List<NotificationBody> filterList;
    Context context;
    Bundle bundle;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvType, tvMessage,tvTime;
        private ImageView notificationImage;

        public MyViewHolder(View view) {
            super(view);
            tvType = view.findViewById(R.id.tvType);
            notificationImage = (ImageView) view.findViewById(R.id.imageNfication);
            tvMessage=view.findViewById(R.id.tvMessage);
            tvTime=view.findViewById(R.id.tvTime);
        }
    }

    public NotificationAdaptar(List<NotificationBody> moviesList, Context context) {
        this.SelectBrandList = moviesList;
        filterList = new ArrayList<>(SelectBrandList);
        this.context = context;
        this.bundle = new Bundle();
    }

    @Override
    public NotificationAdaptar.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list, parent, false);
        return new NotificationAdaptar.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationAdaptar.MyViewHolder holder, int position) {
      /*  final HistoryList movie = SelectBrandList.get(position);
        holder.tvMedicalName.setText(movie.getMedical_name());
        holder.tvDate.setText(movie.getDate());
        holder.tvTime.setText(movie.getTime());
        holder.tvRefNo.setText(movie.getRef_no());
        holder.tvStatus.setText(movie.getStatus());*/

        /*String image=movie.getBrand_logo();
        if(image!=null && !image.isEmpty())
            Picasso.with(context).load(image).into(holder.ivCar);*/
        final NotificationBody movie = SelectBrandList.get(position);
        final NotificationData data = movie.getData();
        String type = "";
        if (data.getType().equals("order_rejected")) {
            type = "Order Rejected";
        } else if (data.getType().equals("order_accepted")) {
            type = "Order Accepted";
        } else if (data.getType().equals("order_placed")) {
            type = "Order Placed";
        } else if (data.getType().equals("order_dispatched")) {
            type = "Order Dispatched";
        } else if (data.getType().equals("order_packed")) {
            type = "Order Packed";
        } else if (data.getType().equals("order_cancelled")) {
            type = "Order Cancelled";
        } else if (data.getType().equals("order_arrived")) {
            type = "Order Arrived";
        } else {
            type = "Order Placed";
        }
        holder.tvType.setText(type);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new OrderDetailsFragment(), data.getOrderId());
            }
        });
//        if(movie.getHeading().equals("Order Success")) {
//            holder.notificationImage.setImageResource(R.drawable.ic_ordersuccess);
//        }else if(movie.getHeading().equals("Order Arrived")) {
//            holder.notificationImage.setImageResource(R.drawable.ic_orderarrived);
//        }else if(movie.getHeading().equals("Order Cancelled")) {
//            holder.notificationImage.setImageResource(R.drawable.ic_ordercancelled);
//        } else {
//            holder.notificationImage.setImageResource(R.drawable.ic_ordercancelled);
//        }
        holder.tvMessage.setText(data.getMessage());
        holder.tvTime.setText(movie.getCreatedAt());
//        holder.clOrderItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new OrderDetailsFragment());
//            }
//        });

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


    @Override
    public int getItemCount() {
        return SelectBrandList.size();
    }

    private void loadFragment(Fragment fragment) {
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}