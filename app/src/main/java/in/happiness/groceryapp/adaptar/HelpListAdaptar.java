package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.fragment.TicketDetailsFragment;
import in.happiness.groceryapp.model.Tickets;

import java.util.ArrayList;


public class HelpListAdaptar extends RecyclerView.Adapter<HelpListAdaptar.ViewHolder> {

    private ArrayList<Tickets> courseModelArrayList;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;
    private Bundle bundle;

    // data is passed into the constructor
    public HelpListAdaptar(ArrayList<Tickets> courseModelArrayList, Context context, ItemClickListener mClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.courseModelArrayList = courseModelArrayList;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public HelpListAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_ticket, parent, false);
        return new HelpListAdaptar.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull HelpListAdaptar.ViewHolder holder, int position) {
        final Tickets movie = courseModelArrayList.get(position);
        holder.tvTicketId.setText("Ticket Id : "+movie.getId());
        holder.tvDateTime.setText(movie.getCreated_at());
        String status="";
        if (movie.getStatus()!=null) {
            String a = movie.getStatus().substring(0, 1).toUpperCase();
            String b = movie.getStatus().substring(1).toLowerCase();
            status=a+b;
        }
        holder.tvStatus.setText(status);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle=new Bundle();
                bundle.putString("ticket_id",movie.getId());
                loadFragment(new TicketDetailsFragment());
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTicketId, tvDateTime, tvStatus, tvTitle, tvView;
        ImageView ivDelete;
        ViewHolder(View itemView) {
            super(itemView);
            tvTicketId=itemView.findViewById(R.id.tvTicketId);
            tvDateTime=itemView.findViewById(R.id.tvDateTime);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvView=itemView.findViewById(R.id.tvView);
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(courseModelArrayList.get(id));
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