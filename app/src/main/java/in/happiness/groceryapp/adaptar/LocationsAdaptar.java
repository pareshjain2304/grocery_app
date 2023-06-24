package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.Data;

import java.util.ArrayList;


public class LocationsAdaptar extends RecyclerView.Adapter<LocationsAdaptar.ViewHolder> {

    private ArrayList<Data> courseModelArrayList;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public LocationsAdaptar(ArrayList<Data> courseModelArrayList, Context context, ItemClickListener mClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.courseModelArrayList = courseModelArrayList;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void editLocation(int position);
        void deleteLocation(int position);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public LocationsAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_locations, parent, false);
        return new LocationsAdaptar.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull LocationsAdaptar.ViewHolder holder, int position) {
        final Data movie = courseModelArrayList.get(position);
        holder.tvLocationType.setText(movie.getTitle());
        holder.tvLocationAddress.setText(movie.getAddress());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.deleteLocation(position);
            }
        });
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.editLocation(position);
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
        TextView tvLocationType, tvLocationAddress, tvEdit;
        ImageView ivDelete;
        ViewHolder(View itemView) {
            super(itemView);
            tvLocationType = itemView.findViewById(R.id.tvLocationType);
            tvLocationAddress = itemView.findViewById(R.id.tvLocationAddress);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            tvEdit = itemView.findViewById(R.id.tvEdit);
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(courseModelArrayList.get(id));
    }


}