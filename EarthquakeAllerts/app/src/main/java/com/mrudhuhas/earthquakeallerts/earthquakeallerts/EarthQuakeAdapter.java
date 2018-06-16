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

import java.util.List;

public class EarthQuakeAdapter extends RecyclerView.Adapter<EarthQuakeAdapter.MyViewHolder> {

    private static final String LOCATION_SEPERATOR = "of";

    String primaryLocation;
    String locationOffset;

    Context context;

    List<EarthQuake> earthQuakeList;
    public EarthQuakeAdapter(Context context,List<EarthQuake> earthQuakeList) {
        this.earthQuakeList = earthQuakeList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        return new MyViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EarthQuake earthQuake = earthQuakeList.get(position);

        String originalLocation = earthQuake.getPlace();

        if (originalLocation.contains(LOCATION_SEPERATOR)){
            String[] parts = originalLocation.split(LOCATION_SEPERATOR);

            locationOffset = parts[0] + LOCATION_SEPERATOR;

            primaryLocation = parts[1];
        }
        else {
            // Otherwise, there is no " of " text in the originalLocation string.
            // Hence, set the default location offset to say "Near the".
            locationOffset = "Near the";
            // The primary location will be the full location string "Pacific-Antarctic Ridge".
            primaryLocation = originalLocation;
        }
        holder.mag.setText(String.valueOf(earthQuake.getMagnitude()));
        holder.place.setText(primaryLocation);
        holder.time.setText(earthQuake.getTime());
        holder.date.setText(earthQuake.getDate());
        holder.details.setText(locationOffset);
        holder.depth.setText(earthQuake.getDepth());

        GradientDrawable magnitudeCircle = (GradientDrawable) holder.mag.getBackground();

        int magnitudeColor = getMagnitudeColor(earthQuake.getMagnitude());

        magnitudeCircle.setColor(magnitudeColor);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
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

        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }

    @Override
    public int getItemCount() {
        return earthQuakeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mag,place,time,date,details,depth;
        public MyViewHolder(View itemView) {
            super(itemView);
            mag = (TextView)itemView.findViewById(R.id.magnitude);


            place = (TextView)itemView.findViewById(R.id.cityname);
            time = (TextView)itemView.findViewById(R.id.time);
            date = (TextView)itemView.findViewById(R.id.date);
            details = (TextView)itemView.findViewById(R.id.details);
            depth = (TextView)itemView.findViewById(R.id.depth);
        }
    }

    public void clear(){
        for (int i=0;i<earthQuakeList.size();i++){
            earthQuakeList.remove(i);
        }
        notifyItemMoved(0,earthQuakeList.size());
    }

    public EarthQuake getItem(int position){
        return earthQuakeList.get(position);
    }
}
