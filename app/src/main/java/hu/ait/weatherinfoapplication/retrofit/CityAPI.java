package hu.ait.weatherinfoapplication.retrofit;

import hu.ait.weatherinfoapplication.data.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by caitlinwesterfield on 7/2/17.
 */

public interface CityAPI {

    @GET("data/2.5/weather")
Call<WeatherResult> getWeatherInfo(@Query("q") String name, @Query("units") String units, @Query("appID") String appID);

}
