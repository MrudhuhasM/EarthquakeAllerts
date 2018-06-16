package com.mrudhuhas.earthquakeallerts.earthquakeallerts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    private static final String LOG_TAG = EarthQuakeLoader.class.getName();

    private String mUrl;
    public EarthQuakeLoader(@NonNull Context context , String mUrl) {
        super(context);
        this.mUrl = mUrl;
        Log.e("Loader","Initialised");
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<EarthQuake> loadInBackground() {
        if (mUrl == null){
            Log.e("Loader","URL is null");
            return null;
        }
        Log.e("Loader","URL is not null");
        List<EarthQuake> earthQuakes = QuerryUtills.fetchDatafromWeb(mUrl);
        return earthQuakes;
    }
}
