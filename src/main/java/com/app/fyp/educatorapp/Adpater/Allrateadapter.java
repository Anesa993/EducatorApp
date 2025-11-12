package com.app.fyp.educatorapp.Adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fyp.educatorapp.Model.Rating;
import com.app.fyp.educatorapp.R;


import java.util.List;

public class Allrateadapter extends RecyclerView.Adapter<Allrateadapter.ViewHolder> {

    private Context context;
    private List<Rating> rateList;

    public Allrateadapter(Context context, List<Rating> rateList) {
        this.context = context;
        this.rateList = rateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rate_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rating rate = rateList.get(position);
        // Set the data to the views in the ViewHolder
        holder.nameTextView.setText(rate.getUsername());
        holder.rating.setRating(Float.parseFloat(rate.getUserrate()));

    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        RatingBar rating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTView);
            rating=itemView.findViewById(R.id.ratee);

        }
    }
}
