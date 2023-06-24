package in.happiness.groceryapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.utils.AppConstant;


public class ReferEarnFragment extends Fragment {
    private View view;
    private ImageView ivShare;
    private User user;
    private String msgCode;
    private LinearLayout llBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_refer_earn, container, false);
        init();
        msgCode="Join me on Happiness App, a secure shopping app to shop anything. Enter my code HAPPINESS323 to earn ₹10 back on registration! https://play.google.com/store/apps/details?id=in.happiness.groceryapp";
        user = AppConstant.user;
        setListener();
        return view;
    }

    private void setListener() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (user.getReferalCode()!=null) {
                if (msgCode!=null) {
                    shareContent(msgCode);
                }
            }
        });
//        ivMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (user.getReferalCode()!=null) {
//                if (msgCode!=null) {
//                    sendMessage(msgCode);
//                }
//            }
//        });
//        ivEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (user.getReferalCode()!=null) {
//                if (msgCode!=null) {
//                    composeEmail(msgCode);
//                }
//            }
//        });
    }

    private void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void composeEmail(String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        // intent.putExtra(Intent.EXTRA_EMAIL  , new String[] { "me@somewhere.com" });
        // intent.putExtra(Intent.EXTRA_SUBJECT, "My subject");
        intent.putExtra(Intent.EXTRA_TEXT, body );
        startActivity(Intent.createChooser(intent, "Email via..."));
    }


    private void sendMessage(String message){
        try {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", message);
            sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void shareContent(String message){
        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() {
        llBack=view.findViewById(R.id.llBack);
        ivShare=view.findViewById(R.id.ivShare);
    }
}