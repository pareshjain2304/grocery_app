package in.happiness.groceryapp.utils;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

public class SmsVerificationApp extends Application {
    @Override public void onCreate() {
        super.onCreate();
        ArrayList<String> appCodes = new ArrayList<>();
        SmsVerification hash = new SmsVerification(this);
        appCodes= hash.getAppSignatures();
        String yourhash = appCodes.get(0);
        Log.i("yourhash",yourhash);
//        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
//        appSignatureHelper.getAppSignatures();
    }
}
