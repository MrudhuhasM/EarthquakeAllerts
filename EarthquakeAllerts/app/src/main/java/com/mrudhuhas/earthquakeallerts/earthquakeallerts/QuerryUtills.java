package com.mrudhuhas.earthquakeallerts.earthquakeallerts;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuerryUtills {

    private QuerryUtills(){

    }

    public static List<EarthQuake> fetchDatafromWeb(String mUrl) {
        URL url = CreateUrl(mUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpurlrequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<EarthQuake> earthQuakeList = extratdatefromJson(jsonResponse);
        return earthQuakeList;
    }

    private static List<EarthQuake> extratdatefromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse))
            return null;

        List<EarthQuake> earthQuakes = new ArrayList<>();

        try {
            JSONObject baseJson = new JSONObject(jsonResponse);

            JSONArray earthquakeArray = baseJson.getJSONArray("features");

            for (int i =0;i<earthquakeArray.length();i++){

                JSONObject currenEarthquake = earthquakeArray.getJSONObject(i);

                JSONObject properties = currenEarthquake.getJSONObject("properties");

                double mag = properties.getDouble("mag");

                String location = properties.getString("place");

                long time = properties.getLong("time");

                String url = properties.getString("url");

                JSONObject geometry = currenEarthquake.getJSONObject("geometry");

                JSONArray coordinates = geometry.getJSONArray("coordinates");

                String depth = coordinates.getString(2);

                String formatdepth = "Depth "+depth+" k m";

                EarthQuake earthQuake = new EarthQuake(location,formatDate(time),formatTime(time),formatMagnitude(mag),url,formatdepth);


                earthQuakes.add(earthQuake)            ;

            }

        }catch (Exception e){

        }
        return earthQuakes;
    }

    private static double formatMagnitude(double mag) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return Double.parseDouble(magnitudeFormat.format(mag));
    }


    private static String formatTime(long time) {
        Date dateObject = new Date(time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private static String formatDate(long time) {
        Date dateObject = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private static String makeHttpurlrequest(URL url) throws IOException {
        String jsonresponse = "";

        if (url == null){
            return jsonresponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonresponse = readFromreader(inputStream);
            }
        }catch (IOException e){

        }finally {
            if (httpURLConnection!=null)
                httpURLConnection.disconnect();
            if (inputStream!=null)
                inputStream.close();
        }
        return jsonresponse;
    }

    private static String readFromreader(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static URL CreateUrl(String mUrl) {
        URL url = null;

        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  url;
    }

}
