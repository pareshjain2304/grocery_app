package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.StoresProducts;
import in.happiness.groceryapp.model.SubCategory;

import java.util.ArrayList;

public class SubCategoryAdaptar  extends RecyclerView.Adapter<SubCategoryAdaptar.ViewHolder> {

    private ArrayList<SubCategory> courseModelArrayList;
    private StoresProducts storesProducts;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void subCategoryId(int lastSelectedPosition);
    }

    // data is passed into the constructor
    public SubCategoryAdaptar(ArrayList<SubCategory> courseModelArrayList, Context context, ItemClickListener mClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.courseModelArrayList=courseModelArrayList;
        this.storesProducts=storesProducts;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public SubCategoryAdaptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_subcategory, parent, false);
        return new SubCategoryAdaptar.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdaptar.ViewHolder holder, int position) {
        final SubCategory movie = courseModelArrayList.get(position);
        holder.subCategoryName.setText(movie.getName());
        if (movie.getActive().equals("1")) {
            holder.llSubCategory.setBackgroundResource(R.drawable.product_category_selected);
            holder.subCategoryName.setTextColor(Color.parseColor("#ff9445"));
        } else {
            holder.llSubCategory.setBackgroundResource(R.drawable.product_category_deselected);
            holder.subCategoryName.setTextColor(Color.parseColor("#ababab"));
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
                    mClickListener.subCategoryId(getPosition);
                }
            });
        }


    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(courseModelArrayList.get(id));
    }


}