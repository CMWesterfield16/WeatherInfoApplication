package hu.ait.weatherinfoapplication.data;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by caitlinwesterfield on 6/29/17.
 */

public class CityData extends RealmObject implements RealmModel {

    @PrimaryKey
    private String cityID;

    private String cityTitle;

    public void City(){

    }

    public void City(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

}