package es.upo.alu.fsaufer.dm.divinglogapp.control.rest;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.concurrent.TimeUnit;

import es.upo.alu.fsaufer.dm.divinglogapp.dto.WeatherServiceResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherService {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private WeatherService() {
    }

    private static ApiInterface api;

    public interface ApiInterface {

        @GET("weather")
        Call<WeatherServiceResponse> getCurrentWeather(@Query("appid") String apiKey,
                                                       @Query("units") String units,
                                                       @Query("lon") String longitude,
                                                       @Query("lat") String latitude);

    }

    public static ApiInterface getApi(Context context) {
        if (api == null)
            createInstance(context);

        return api;
    }

    private static void createInstance(Context context) {
        if (api == null) {
            synchronized (WeatherService.class) {
                if (api == null) {
                    api = buildApiClient(context);
                }
            }
        }
    }

    private static ApiInterface buildApiClient(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ChuckInterceptor(context))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit.create(ApiInterface.class);
    }
}
