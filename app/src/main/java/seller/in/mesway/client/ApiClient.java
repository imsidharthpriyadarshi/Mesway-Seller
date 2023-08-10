package seller.in.mesway.client;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import seller.in.mesway.activity.App;

public class ApiClient {

    private final static String BASE_URL= App.getAPIUrl();
    private static Retrofit retrofit;

    public static Retrofit getClient(){

        if(retrofit==null){
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .callTimeout(3, TimeUnit.MINUTES)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60,TimeUnit.SECONDS)
                    .writeTimeout(60,TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }
        return retrofit;
    }



}
