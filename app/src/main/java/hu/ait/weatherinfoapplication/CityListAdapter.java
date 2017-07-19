package hu.ait.weatherinfoapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import hu.ait.weatherinfoapplication.data.CityData;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {


    private Realm realmCity;
    private List<CityData> listCity;
    public static final String CITY_NAME = "City Name";

    private Context context;

    public CityListAdapter(Context context, Realm realmCity){

        this.context = context;
        this.realmCity = realmCity;



        listCity = new ArrayList<CityData>();

        RealmResults<CityData> cityResult =
                realmCity.where(CityData.class).findAll().sort("cityTitle", Sort.ASCENDING);
        listCity = new ArrayList<CityData>();
        for (int i = 0; i <cityResult.size(); i++) {
            listCity.add(cityResult.get(i));
        }
    }

    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cityRow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.city_row, parent, false);
        return new ViewHolder(cityRow);
    }

    @Override
    public void onBindViewHolder(final CityListAdapter.ViewHolder holder, final int position) {

        holder.tvCity.setText(listCity.get(position).getCityTitle());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCity(position);
            }
        });

        holder.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStartWeather = new Intent();
                intentStartWeather.setClass(context, WeatherViewActivity.class);

                intentStartWeather.putExtra(CITY_NAME, holder.tvCity.getText().toString());

                context.startActivity(intentStartWeather);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCity.size();
    }


    public void addCity(String cityTitle) {
        realmCity.beginTransaction();
        CityData newCity = realmCity.createObject(CityData.class, UUID.randomUUID().toString());
        newCity.setCityTitle(cityTitle);
        realmCity.commitTransaction();

        listCity.add(newCity);
        notifyDataSetChanged();
    }

    public void deleteCity(int position) {
        realmCity.beginTransaction();
        listCity.get(position).deleteFromRealm();
        realmCity.commitTransaction();
        listCity.remove(position);

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCity;
        private Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
