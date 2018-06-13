package com.mrudhuhas.earthquakeallerts.earthquakeallerts;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class EarthQuakeAdapter extends RecyclerView.Adapter<EarthQuakeAdapter.MyViewHolder> {

    List<EarthQuakes> earthQuakesList;


    private static final String LOCATION_SEPERATOR = "of";


    Context context;

    String primaryLocation,locationOffset;



    public EarthQuakeAdapter(Context context,List<EarthQuakes> earthQuakes) {
        this.context = context;
        this.earthQuakesList = earthQuakes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recyclerview_earthquakes,parent,false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EarthQuakes earthQuakes = earthQuakesList.get(position);

        String originalLocation = earthQuakes.getPlace();

        if (originalLocation.contains(LOCATION_SEPERATOR)){
            String[] parts = originalLocation.split(LOCATION_SEPERATOR);
            locationOffset = parts[0] + LOCATION_SEPERATOR;
            primaryLocation = parts[1];
        }
        else {
            locationOffset = "near the";
            primaryLocation = originalLocation;
        }
        holder.magnitude.setText(String.valueOf(formatMagnitude(earthQuakes.getMagnitude())));
        holder.cityname.setText(primaryLocation);
        holder.locationOfsetView.setText(locationOffset);
        holder.time.setText(earthQuakes.getTime());
        holder.date.setText(earthQuakes.getDate());

        GradientDrawable magnitudeCircle = (GradientDrawable)holder.magnitude.getBackground();
        int magnitudeColor = getMagnitudeCololr(earthQuakes.getMagnitude());
        
        magnitudeCircle.setColor(magnitudeColor);

    }

    private int getMagnitudeCololr(double magnitude) {
        int magnitudeColorResourceId;;
        int magnitudeFloor = (int)Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(context,magnitudeColorResourceId);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeformat = new DecimalFormat("0.0");
        return magnitudeformat.format(magnitude);
    }

    @Override
    public int getItemCount() {
        return earthQuakesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView cityname,magnitude,date,time,locationOfsetView;
        public MyViewHolder(View itemView) {
            super(itemView);
            cityname = (TextView)itemView.findViewById(R.id.cityname_txtview);
            magnitude = (TextView)itemView.findViewById(R.id.magnitude_textview);
            date = (TextView)itemView.findViewById(R.id.date_textView);
            time = (TextView)itemView.findViewById(R.id.time_textView);
            locationOfsetView = (TextView)itemView.findViewById(R.id.location_details);


        }
    }

    public void clear() {
        final int size = earthQuakesList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                earthQuakesList.remove(i);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}
