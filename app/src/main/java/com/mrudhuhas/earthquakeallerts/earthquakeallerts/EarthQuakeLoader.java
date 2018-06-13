package com.mrudhuhas.earthquakeallerts.earthquakeallerts;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.net.URL;
import java.util.List;

public class EarthQuakeLoader extends android.support.v4.content.AsyncTaskLoader<List<EarthQuakes>> {

    private static final String LOG_TAG = EarthQuakeLoader.class.getName();
    private String url;
    public EarthQuakeLoader(Context context,String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<EarthQuakes> loadInBackground() {
        if (url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<EarthQuakes> earthquakes = QueryUtils.fetchDatafromWeb(url);
        return earthquakes;
    }
}
