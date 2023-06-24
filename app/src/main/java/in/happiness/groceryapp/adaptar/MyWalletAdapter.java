package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.fragment.WalletDetailsFragment;
import in.happiness.groceryapp.model.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyWalletAdapter extends RecyclerView.Adapter<MyWalletAdapter.MyViewHolder> implements Filterable {
    private List<Transaction> SelectBrandList;
    private List<Transaction> filterList;
    Context context;
    Bundle bundle;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDateTime,tvPoints,tvStatus;
        LinearLayout llHistoryList;
        private ImageView ivCar;

        public MyViewHolder(View view) {
            super(view);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            tvPoints = (TextView) view.findViewById(R.id.tvPoints);
            tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
            llHistoryList = (LinearLayout) view.findViewById(R.id.llHistoryList);
            //ivCar = (ImageView) view.findViewById(R.id.ivCar);
        }
    }

    public MyWalletAdapter(List<Transaction> moviesList, Context context) {
        this.SelectBrandList = moviesList;
        filterList = new ArrayList<>(SelectBrandList);
        this.context=context;
        this.bundle=new Bundle();
    }

    @Override
    public MyWalletAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mywallet_list_row, parent, false);

        return new MyWalletAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyWalletAdapter.MyViewHolder holder, int position) {
        final Transaction movie = SelectBrandList.get(position);
        holder.tvPoints.setText(movie.getAmount()+" /-");
        holder.tvDateTime.setText(movie.getCreated_at());
        holder.tvStatus.setText(movie.getType());

        if(movie.getType().equals("Credited To")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorGreenBg));
        }else {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
        }
        /*String image=movie.getBrand_logo();
        if(image!=null && !image.isEmpty())
            Picasso.with(context).load(image).into(holder.ivCar);*/

        holder.llHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("Flag","MyWallet");
                bundle.putSerializable("wTransaction", movie);
                loadFragment(new WalletDetailsFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return SelectBrandList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Transaction> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filterList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Transaction item : filterList) {
                    if (item.getType().toLowerCase().contains(filterPattern) ||
                            formatFilterDate(item.getCreated_at()).toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            SelectBrandList.clear();
            if(results.values!=null)
                SelectBrandList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private String formatFilterDate(String dateString) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat("MM/yyyy");
            return sd.format(d);
        } catch (ParseException e) {
        }
        return "";
    }
    private String formatDate(String dateString) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat("dd/MM/yyyy");
            return sd.format(d);
        } catch (ParseException e) {
        }
        return "";
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








