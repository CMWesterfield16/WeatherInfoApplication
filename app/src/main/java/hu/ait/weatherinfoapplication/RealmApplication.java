package hu.ait.weatherinfoapplication;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by caitlinwesterfield on 6/29/17.
 */

public class RealmApplication extends Application {

    private Realm realmCity;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder().
                deleteRealmIfMigrationNeeded().
                build();
        realmCity = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmCity.close();
    }

    public Realm getRealmCity() {
        return realmCity;
    }
}
