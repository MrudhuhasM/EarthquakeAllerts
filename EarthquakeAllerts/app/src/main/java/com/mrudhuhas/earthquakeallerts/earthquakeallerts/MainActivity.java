package com.mrudhuhas.earthquakeallerts.earthquakeallerts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>>,SharedPreferences.OnSharedPreferenceChangeListener{

    EarthQuakeAdapter earthQuakeAdapter;
    RecyclerView earthquakerecyclerview;
    private int EARTHQUAKE_LOADER_ID = 1;
    ArrayList<EarthQuake> earthQuakeArrayList;
    TextView emptyViewNodata;
    TextView emptyViewNoConnection;

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        earthQuakeArrayList = new ArrayList<>();

        emptyViewNodata = (TextView)findViewById(R.id.emptyViewnoData);
        emptyViewNoConnection = (TextView)findViewById(R.id.emptyViewnoConnection);

        //TODO initialize earthQuakeArrayList

        earthQuakeAdapter = new EarthQuakeAdapter(this,earthQuakeArrayList);

        earthquakerecyclerview = (RecyclerView)findViewById(R.id.main_recyclerview);
        earthquakerecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        earthquakerecyclerview.setItemAnimator(new DefaultItemAnimator());
        earthquakerecyclerview.setAdapter(earthQuakeAdapter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        preferences.registerOnSharedPreferenceChangeListener(this);

        earthquakerecyclerview.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), earthquakerecyclerview, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void OnClick(View view, int position) {
            }

            @Override
            public void OnLongClick(View view, int position) {
                EarthQuake earthQuake = earthQuakeAdapter.getItem(position);

                Uri uri = Uri.parse(earthQuake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,uri);

                startActivity(websiteIntent);
            }
        }));

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
           LoaderManager loaderManager = getLoaderManager();

           loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        }else {
            //TODO add loading indicator
            View loadingIndiacator = findViewById(R.id.loading_indicator);
            loadingIndiacator.setVisibility(View.GONE);

            earthquakerecyclerview.setVisibility(View.GONE);
            emptyViewNodata.setVisibility(View.GONE);
            emptyViewNoConnection.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.settings_min_magnitude_key)) ||
                key.equals(getString(R.string.settings_order_by_key))) {
            // Clear the ListView as a new query will be kicked off
            earthQuakeAdapter.clear();

            // Hide the empty state text view as the loading indicator will be displayed
            emptyViewNodata.setVisibility(View.GONE);
            emptyViewNoConnection.setVisibility(View.GONE);
            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the USGS as the query settings have been updated
            getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, null, this);
        }

    }

    @Override
    public android.content.Loader<List<EarthQuake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthQuakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<EarthQuake>> loader, List<EarthQuake> earthquakes) {

         View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        if (earthquakes != null && !earthquakes.isEmpty()) {
            emptyViewNodata.setVisibility(View.GONE);
            emptyViewNoConnection.setVisibility(View.GONE);
            earthQuakeAdapter = new EarthQuakeAdapter(this,earthquakes);
            earthQuakeAdapter.notifyDataSetChanged();
            earthquakerecyclerview.setAdapter(earthQuakeAdapter);
        }
        else
            emptyViewNodata.setVisibility(View.VISIBLE);

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<EarthQuake>> loader) {
        earthQuakeAdapter.clear();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, Settings.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
