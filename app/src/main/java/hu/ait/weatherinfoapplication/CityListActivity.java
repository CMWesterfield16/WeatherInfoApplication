package hu.ait.weatherinfoapplication;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static hu.ait.weatherinfoapplication.R.id.about;
import static hu.ait.weatherinfoapplication.R.id.parent;


public class CityListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    public static final String KEY_CITY_ID = "KEY_CITY_ID";
    private int cityEditIndex = -1;

    private CityListAdapter cityListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_main);

        ((RealmApplication)getApplication()).openRealm();
        cityListAdapter = new CityListAdapter(this, ((RealmApplication)getApplication()).getRealmCity());

        RecyclerView recyclerCity = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerCity.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCity.setLayoutManager(layoutManager);
        recyclerCity.setAdapter(cityListAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCity();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





    }

    public void showAddCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.enterCityHere);

        final EditText etCity = new EditText(this);
        builder.setView(etCity);

        builder.setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(etCity.getText().toString())) {
                    Toast.makeText(CityListActivity.this, R.string.pleaseEnterCity, Toast.LENGTH_LONG).show();
                    showAddCity();
                } else if (etCity.getText().toString().length() < 3) {
                    Toast.makeText(CityListActivity.this, R.string.fullNameError, Toast.LENGTH_LONG).show();
                    showAddCity();
                } else if (!etCity.getText().toString().matches("[a-zA-Z]+")) {
                    Toast.makeText(CityListActivity.this, R.string.lettersError, Toast.LENGTH_LONG).show();
                    showAddCity();
                } else {
                    cityListAdapter.addCity(etCity.getText().toString());
                }
            }
        });

        builder.setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == about) {
            Toast.makeText(CityListActivity.this, R.string.madeByAIT, Toast.LENGTH_LONG).show();
        } else {
            showAddCity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        ((RealmApplication)getApplication()).closeRealm();
        super.onDestroy();
    }
}
