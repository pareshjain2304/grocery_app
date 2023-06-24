package in.happiness.groceryapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.activity.OTPActivity;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class UserProfileFragment extends Fragment implements View.OnClickListener {
    LinearLayout llTopMenu, llClose, llCategory, llSignout, llBack, llTermsCondition, llAboutUs, llPrivacyPolicy, llFaq, llContactUs,
            llRefundCancellation, llShippingPolicy, llAddress, llRefer, llNeedHelp;
//    llOrder, , llHome, llWallet;
    View v;
    ImageView ivHelp,ivNotification;
    AppSharedPreferences sharedPreferences;
    String userName;
    TextView tvMobile,tvName,tvMail, tvUser, tvUpdateProfile;
    Bundle bundle;
    BottomNavigationView bottom_navigation;
    private AppDBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        bundle=new Bundle();
        sharedPreferences=new AppSharedPreferences(getActivity());
        dbHelper = new AppDBHelper(getContext());
        User user = dbHelper.getUser(AppConstant.userToken);
        if (user!=null) {
            userName = user.getName();
        } else {
            userName = "User";
        }
        init();
        llTopMenu.setVisibility(View.GONE);
        setListener();
        return v;
    }

    private void init() {
        llTopMenu = getActivity().findViewById(R.id.llTopMenu);
        llClose = v.findViewById(R.id.llClose);
//        llOrder = v.findViewById(R.id.llOrder);
//        llWallet = v.findViewById(R.id.llWallet);
        llBack = v.findViewById(R.id.llBack);
        llCategory = v.findViewById(R.id.llCategory);
        tvUpdateProfile=v.findViewById(R.id.tvUpdateProfile);
        llSignout = v.findViewById(R.id.llSignout);
        tvUser = v.findViewById(R.id.tvUser);
        llTermsCondition = v.findViewById(R.id.llTermsCondition);
        llAboutUs = v.findViewById(R.id.llAboutUs);
        llPrivacyPolicy = v.findViewById(R.id.llPrivacyPolicy);
        llFaq = v.findViewById(R.id.llFaq);
        llContactUs = v.findViewById(R.id.llContactUs);
        llRefundCancellation = v.findViewById(R.id.llRefundCancellation);
        llShippingPolicy = v.findViewById(R.id.llShippingPolicy);
        llAddress = v.findViewById(R.id.llAddress);
        llRefer = v.findViewById(R.id.llRefer);
        llNeedHelp = v.findViewById(R.id.llNeedHelp);
        tvUser.setText("Hey "+userName);
//        llHome=v.findViewById(R.id.llHome);
    }

    @Override
    public void onStart() {
        super.onStart();
        //   getUserProfile();
    }

    private void setListener() {
//        llWallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new WalletFragment());
//            }
//        });
//        llHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new MainFragment());
//            }
//        });
        llNeedHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new NeedHelpFragment());
            }
        });
        llRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ReferEarnFragment());
            }
        });
        llAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LocationsFragment());
            }
        });
        tvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new EditProfileFragment());
            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MainFragment());
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MainFragment());
            }
        });
//        llOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new OrdersFragment());
//            }
//        });
        llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CategoryFragment());
            }
        });
        llSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        llTermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WebFragment(), "terms");
            }
        });
        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WebFragment(), "aboutUs");
            }
        });
        llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WebFragment(), "privacy");
            }
        });
        llFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WebFragment(), "faq");
            }
        });
        llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WebFragment(), "contact");
            }
        });
        llRefundCancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WebFragment(), "refund");
            }
        });
        llShippingPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WebFragment(), "shipping");
            }
        });

    }

    private void showLogoutDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();

        TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.doSaveToSharedPreferences(AppConstant.isLoggedIn, "false");
                sharedPreferences.doSaveToSharedPreferences(AppConstant.locationAvailable, "false");
                sharedPreferences.doSaveToSharedPreferences(AppConstant.token, "");
                dialog.dismiss();
                Intent i = new Intent(getActivity(), OTPActivity.class);
                startActivity(i);
            }
        });

        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

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

    private void loadFragment(Fragment fragment, String type) {
        bundle.putString("type",type);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}