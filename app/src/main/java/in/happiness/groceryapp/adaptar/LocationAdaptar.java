package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.Data;

import java.util.ArrayList;


public class LocationAdaptar extends RecyclerView.Adapter<LocationAdaptar.ViewHolder> {

    private ArrayList<Data> courseModelArrayList;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public LocationAdaptar(ArrayList<Data> courseModelArrayList, Context context, ItemClickListener mClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.courseModelArrayList = courseModelArrayList;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onLocationId(int position);

        void deleteLocation(int position);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public LocationAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_location, parent, false);
        return new LocationAdaptar.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull LocationAdaptar.ViewHolder holder, int position) {
        final Data movie = courseModelArrayList.get(position);
        holder.tvLocationType.setText(movie.getTitle());
        holder.tvLocationAddress.setText(movie.getAddress());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.deleteLocation(position);
            }
        });
        holder.rbLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("hi","hi");
                mClickListener.onLocationId(position);
            }
        });
        if (movie.isFlag()) {
            holder.rbLocation.setChecked(true);
        } else {
            holder.rgLocation.clearCheck();
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocationType, tvLocationAddress;
        ImageView ivDelete;
        RadioGroup rgLocation;
        RadioButton rbLocation;

        ViewHolder(View itemView) {
            super(itemView);
            tvLocationType = itemView.findViewById(R.id.tvLocationType);
            tvLocationAddress = itemView.findViewById(R.id.tvLocationAddress);
            rgLocation = itemView.findViewById(R.id.rgLocation);
            rbLocation = itemView.findViewById(R.id.rbLocation);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            rbLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition=getAdapterPosition();
                    mClickListener.onLocationId(getPosition);
                }
            });
        }

    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(courseModelArrayList.get(id));
    }


}