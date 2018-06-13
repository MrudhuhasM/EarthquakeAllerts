package com.mrudhuhas.earthquakeallerts.earthquakeallerts;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.LoaderManager.LoaderCallbacks;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataFromWeb extends Fragment implements LoaderCallbacks<List<EarthQuakes>>,SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Object> {

    private static final String USGS_SAMPLE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    EarthQuakeAdapter earthQuakeAdapter;

    public DataFromWeb() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_data_from__web, container, false);
        final ArrayList<EarthQuakes> earthquakes = QueryUtils.extractEarthquakes();

       earthQuakeAdapter = new EarthQuakeAdapter(getContext(),earthquakes);
        RecyclerView earthquakeview = (RecyclerView)rootview.findViewById(R.id.main_recyclerView);
        earthquakeview.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), earthquakeview, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                EarthQuakes currentEarthquake = earthquakes.get(position);

                Uri earthQuakeuri = Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthQuakeuri);

                startActivity(websiteIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
           LoaderManager loaderManager = getLoaderManager();

           loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        }
        else{
            //TODO set empty view and loding indicator
        }
        earthquakeview.setLayoutManager(new LinearLayoutManager(getContext()));
        earthquakeview.setItemAnimator(new DefaultItemAnimator());
        earthquakeview.setAdapter(earthQuakeAdapter);
        return rootview;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("minmagnitude") || key.equals("orderby")){
            earthQuakeAdapter.clear();

            //TODO set empty textview and loading indicator

            getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID,null,this);
        }
    }

    @Override
    public android.content.Loader<List<EarthQuakes>> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        //TODO change hardcoding
        String magnitude = preferences.getString("minmagnitude","5");
        String orderby = preferences.getString("orderby","magnitude");

        Uri baseuri = Uri.parse(USGS_SAMPLE_URL);
        Uri.Builder builder = baseuri.buildUpon();

        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("limit","10");
        builder.appendQueryParameter("minmag",magnitude);
        builder.appendQueryParameter("orderby",orderby);


        return new EarthQuakeLoader(getContext(),builder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Object> loader, Object data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Object> loader) {

    }
}
