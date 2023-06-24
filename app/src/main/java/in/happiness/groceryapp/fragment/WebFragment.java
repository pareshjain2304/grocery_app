package in.happiness.groceryapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.utils.NetworkConnection;

public class WebFragment extends Fragment {
    View v;
    LinearLayout llBack,llNoNetwork,llRefresh,llTerms;
    WebView wvTermsConditions;
    String type;
    TextView heading;

    public WebFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_web, container, false);
        init();
        if(getArguments()!=null) {
            if (getArguments().containsKey("type")) {
                type = (String) getArguments().get("type");
            }
        }
        setListener();
        return v;
    }

    private void init(){
        llBack = v.findViewById(R.id.llBack);
        llNoNetwork = v.findViewById(R.id.llNoNetwork);
        llRefresh = v.findViewById(R.id.llRefresh);
        wvTermsConditions = (WebView) v.findViewById(R.id.wvTermsConditions);
        llTerms = (LinearLayout) v.findViewById(R.id.llTerms);
        heading = v.findViewById(R.id.heading);
    }

    private void setListener() {
        if (!NetworkConnection.isNetworkAvailable(getActivity())) {
            llNoNetwork.setVisibility(View.VISIBLE);
            llTerms.setVisibility(View.GONE);
        } else {
            llNoNetwork.setVisibility(View.GONE);
            llTerms.setVisibility(View.VISIBLE);
            if (type.equals("faq")) {
                heading.setText("FAQ");
                wvTermsConditions.loadUrl("https://thehappinessindia.com/faq.html");
            } else if (type.equals("privacy")) {
                heading.setText("Privacy Policy");
                wvTermsConditions.loadUrl("https://thehappinessindia.com/privacy.html");
            } else if (type.equals("aboutUs")) {
                heading.setText("About Us");
                wvTermsConditions.loadUrl("https://thehappinessindia.com/about.html");
            } else if (type.equals("terms")) {
                heading.setText("Terms and Conditions");
                wvTermsConditions.loadUrl("https://thehappinessindia.com/Terms.html");
            } else if (type.equals("contact")) {
                heading.setText("Contact Us");
                wvTermsConditions.loadUrl("https://thehappinessindia.com/contact.html");
            } else if (type.equals("refund")) {
                heading.setText("Refund & Cancellation");
                wvTermsConditions.loadUrl("https://thehappinessindia.com/refund.html");
            } else if (type.equals("shipping")) {
                heading.setText("Shipping & Delivery Policy");
                wvTermsConditions.loadUrl("https://thehappinessindia.com/shipping.html");
            }
        }

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        llRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setListener();
            }
        });
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

}