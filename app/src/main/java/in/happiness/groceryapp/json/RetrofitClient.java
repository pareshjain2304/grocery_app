package in.happiness.groceryapp.json;

import in.happiness.groceryapp.utils.AppConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String MainURL = AppConstant.BASE_URL;
    private static Retrofit retrofit = null;

    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(180, TimeUnit.SECONDS)
            .connectTimeout(180, TimeUnit.SECONDS)
            .build();

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    public static Retrofit getClient()
    {
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
    public static ApiInterface getApiInterface()
    {
        ApiInterface interfaceUserInfo= RetrofitClient.getClient().create(ApiInterface.class);
        return interfaceUserInfo;
    }
}
