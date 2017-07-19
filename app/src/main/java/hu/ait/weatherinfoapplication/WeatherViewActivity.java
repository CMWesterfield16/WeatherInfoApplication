package hu.ait.weatherinfoapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import hu.ait.weatherinfoapplication.data.CityData;
import hu.ait.weatherinfoapplication.data.WeatherResult;
import hu.ait.weatherinfoapplication.retrofit.CityAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static hu.ait.weatherinfoapplication.CityListAdapter.CITY_NAME;

public class WeatherViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView cityTitle;
    private TextView currentTemp;
    private TextView description;
    private TextView minTemp;
    private TextView maxTemp;
    private TextView humidity;
    private TextView sunrise;
    private TextView sunset;

    private GoogleMap mMap;
    private Marker markerCity = null;

    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_view);

        cityTitle = (TextView) findViewById(R.id.cityTitle);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        description = (TextView) findViewById(R.id.description);
        minTemp = (TextView) findViewById(R.id.minTemp);
        maxTemp = (TextView) findViewById(R.id.maxTemp);
        humidity = (TextView) findViewById(R.id.humidity);
        sunrise = (TextView) findViewById(R.id.sunrise);
        sunset = (TextView) findViewById(R.id.sunset);

        city = "";

        if (getIntent().hasExtra(CityListAdapter.CITY_NAME)) {
            city = getIntent().getStringExtra(CityListAdapter.CITY_NAME);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public String getSunriseTime(long time) {
        Date date = new Date(time*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CityAPI cityAPI = retrofit.create(CityAPI.class);

        Call<WeatherResult> result = cityAPI.getWeatherInfo(city, "metric", "c4947e4488bf183143ee0c4700806006");



        result.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                WeatherResult weatherResult = response.body();

                cityTitle.setText(weatherResult.getName());
                currentTemp.setText(getString(R.string.currentTemperature) + " " + weatherResult.getMain().getTemp() + " " + getString(R.string.degrees));
                description.setText(getString(R.string.typeOfWeather) + " " + weatherResult.getWeather().get(0).getDescription());
                minTemp.setText(getString(R.string.minTemperature) + " " + weatherResult.getMain().getTempMin() + " " + getString(R.string.degrees));
                maxTemp.setText(getString(R.string.maxTemperature) + " " + weatherResult.getMain().getTempMax() + " " + getString(R.string.degrees));
                humidity.setText(getString(R.string.humidityTitle) + " " + weatherResult.getMain().getHumidity() + " " + getString(R.string.percent));
                sunrise.setText(getString(R.string.sunriseTime) + " " + getSunriseTime(weatherResult.getSys().getSunrise()));
                sunset.setText(getString(R.string.sunsetTime) + " " + getSunriseTime(weatherResult.getSys().getSunset()));

                ImageView imageView = (ImageView) findViewById(R.id.my_image_view);
                Glide.with(WeatherViewActivity.this).load("http://openweathermap.org/img/w/" + weatherResult.getWeather().get(0).getIcon() + ".png").into(imageView);


                setMyMarker(new LatLng(weatherResult.getCoord().getLat(), weatherResult.getCoord().getLon()));
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                description.setText(getString(R.string.errorMessage) + t.getMessage());
            }
        });
    }

    public void setMyMarker(LatLng cityPosition) {

        MarkerOptions markerOptions = new MarkerOptions().
                position(cityPosition).
                title(getString(R.string.cityLocation)).
                icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        markerCity = mMap.addMarker(markerOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(cityPosition));
    }
}