package in.happiness.groceryapp.adaptar;

import android.content.Context;
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
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.utils.AppConstant;

import java.util.List;

public class MainCategoryAdaptar extends RecyclerView.Adapter<MainCategoryAdaptar.ViewHolder> {

    private List<Category> courseModelArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public MainCategoryAdaptar(Context context, List<Category> courseModelArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.courseModelArrayList = courseModelArrayList;
        this.context=context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_main_category, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Category movie = courseModelArrayList.get(position);

        holder.categoryName.setText(movie.getName());
        Glide.with(context).clear(holder.categoryImage);
        if (movie.getImage()!=null && movie.getImage()!="" && !movie.getImage().isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(context).load(AppConstant.IMAGE_URL+movie.getImage()).apply(options).into(holder.categoryImage);
        } else {
            holder.categoryImage.setImageResource(R.drawable.category4);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryName;
        ImageView categoryImage;

        ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(courseModelArrayList.get(id));
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}