package in.happiness.groceryapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.model.User;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import in.happiness.groceryapp.json.RetrofitClient;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.MainData;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppDBHelper;
import in.happiness.groceryapp.utils.AppSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    AppSharedPreferences sharedPreferences;
    private String token;
    private AppDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbHelper = new AppDBHelper(this);
        sharedPreferences=new AppSharedPreferences(SplashActivity.this);
        createChannel();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                String firstTimeLaunch = sharedPreferences.doGetFromSharedPreferences(AppConstant.firstTimeLaunch);
                if (firstTimeLaunch!=null && firstTimeLaunch.equals("true")) {
                    String isLoggedIn = sharedPreferences.doGetFromSharedPreferences(AppConstant.isLoggedIn);
                       if (isLoggedIn!=null && isLoggedIn.equals("true")) {
                        token = sharedPreferences.doGetFromSharedPreferences(AppConstant.token);
                        User user = dbHelper.getUser(token);
                        if (user!=null) {
                            AppConstant.user = user;
                        } else {
                            getUserData(token);
                        }
                        getData(token);
                    } else {
                        Intent mainIntent = new Intent(SplashActivity.this, OTPActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, IntroductionActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void getUserData(String token) {
        Call<User> call = RetrofitClient.getApiInterface().getUserDetails("Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.code() == 200) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.lastDate, sdf.format(date));
                        AppConstant.user = response.body();
                        dbHelper.addUser(response.body(), token);
                    } else {
                        Toast.makeText(SplashActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(SplashActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getData(String token) {
        dbHelper = new AppDBHelper(this);
        AppConstant.categoryList = dbHelper.getAllCategory();
        String lastDate = "";
        if (AppConstant.categoryList.size()>0) {
            lastDate = sharedPreferences.doGetFromSharedPreferences(AppConstant.lastDate);
        }
        RequestBody last_date = RequestBody.create(MediaType.parse("text/plain"), lastDate);
        Call<MainData> call = RetrofitClient.getApiInterface().getData("Bearer " + token, last_date);
        call.enqueue(new Callback<MainData>() {
            @Override
            public void onResponse(Call<MainData> call, Response<MainData> response) {
                try {
                    if (response.code() == 200) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sharedPreferences.doSaveToSharedPreferences(AppConstant.lastDate, sdf.format(date));
                        AppConstant.response = response.body();
                        if (response.body().getCategories().size()>0 || response.body().getSubcategories().size()>0 ||
                                response.body().getBrands().size()>0 || response.body().getChildcategories().size()>0) {
                            AppConstant.validate = true;
                            AppConstant.response = response.body();
                        } else {
                            fetchData();
                        }
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SplashActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                    Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainData> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
                Toast.makeText(SplashActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchData() {
        Map<Integer, List<SubCategory>> subcategories = new HashMap<>();
        if (AppConstant.categoryList.size()>0) {
            for (Category c: AppConstant.categoryList) {
                List<SubCategory> subCategoryList= dbHelper.getAllSubCategory(c.getId());
                subcategories.put(c.getId(),subCategoryList);
            }
        }
        AppConstant.subCategoryList = subcategories;
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/audio3" ) ;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("in.happiness.groceryapp", name, importance);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(sound , audioAttributes) ;
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}