package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.ReplyTicket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TicketDetailsAdapter extends RecyclerView.Adapter<TicketDetailsAdapter.MyViewHolder> {
    private List<ReplyTicket> SelectBrandList;
    Context context;
    Bundle bundle;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUser,tvReply,tvDateTime,tvReplyAdmin,tvDateTimeAdmin,tvNoComplaintFound;
        LinearLayout llAdminReply,llUserReply;

        public MyViewHolder(View view) {
            super(view);
            tvNoComplaintFound = (TextView) view.findViewById(R.id.tvNoComplaintFound);
            tvUser = (TextView) view.findViewById(R.id.tvUser);
            tvReply = (TextView) view.findViewById(R.id.tvReply);
            tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
            tvReplyAdmin = (TextView) view.findViewById(R.id.tvReplyAdmin);
            tvDateTimeAdmin = (TextView) view.findViewById(R.id.tvDateTimeAdmin);
            llAdminReply = (LinearLayout) view.findViewById(R.id.llAdminReply);
            llUserReply = (LinearLayout) view.findViewById(R.id.llUserReply);
            //ivCar = (ImageView) view.findViewById(R.id.ivCar);
        }
    }

    public TicketDetailsAdapter(List<ReplyTicket> moviesList,Context context) {
        this.SelectBrandList = moviesList;
        this.context=context;
        this.bundle=new Bundle();
    }

    @Override
    public TicketDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_list_row, parent, false);

        return new TicketDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TicketDetailsAdapter.MyViewHolder holder, int position) {
        final ReplyTicket movie = SelectBrandList.get(position);
       /* if(position==0 && movie.getUser_id().equals("0")){
            holder.tvNoComplaintFound.setVisibility(View.VISIBLE);
        }else {
            holder.tvNoComplaintFound.setVisibility(View.GONE);
        }*/

        if(movie.getUser_id().equals("0")){
            holder.llUserReply.setVisibility(View.GONE);
            holder.tvReplyAdmin.setText(movie.getReply_message());
            holder.tvDateTimeAdmin.setText(movie.getCreated_at());
        }else {
            holder.llAdminReply.setVisibility(View.GONE);
            holder.tvReply.setText(movie.getReply_message());
            holder.tvDateTime.setText(movie.getCreated_at());
        }


        /*SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
            holder.tvDateTime.setText(formatDate(movie.getCreated(),input.toString(),output.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        /*String image=movie.getBrand_logo();
        if(image!=null && !image.isEmpty())
            Picasso.with(context).load(image).into(holder.ivCar);*/

        /*holder.llHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new HistoryDetailsFragment());
            }
        });*/

    }

    public static String formatDate (String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
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