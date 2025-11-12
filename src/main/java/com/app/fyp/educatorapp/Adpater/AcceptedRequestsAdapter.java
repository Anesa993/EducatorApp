package com.app.fyp.educatorapp.Adpater;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fyp.educatorapp.Model.RequestObjectAccept;
import com.app.fyp.educatorapp.R;

import java.util.List;

public class AcceptedRequestsAdapter extends RecyclerView.Adapter<AcceptedRequestsAdapter.ViewHolder> {

    private Context context;
    private List<RequestObjectAccept> acceptedRequestsList;

    public AcceptedRequestsAdapter(Context context, List<RequestObjectAccept> acceptedRequestsList) {
        this.context = context;
        this.acceptedRequestsList = acceptedRequestsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_accepted_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestObjectAccept request = acceptedRequestsList.get(position);
        // Set the data to the views in the ViewHolder
        holder.nameTextView.setText(request.getUserName());
        holder.phoneTextView.setText(request.getUserPhone());
        holder.addressTextView.setText(request.getUserAddress());
        holder.edttime.setText(request.getUsertime());
        holder.edttype.setText(request.getUsertype());
        holder.request.setText(request.getStatus());
        holder.tutorname.setText(request.getTutorName());

        holder.whatspp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = request.getTutorMobile();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    Uri uri = Uri.parse("whatsapp://send?phone=" + phoneNumber);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    // Start the activity if there's an app to handle the intent
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        // Handle the case where WhatsApp is not installed
                        // Maybe show a toast or dialog to the user
                        Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return acceptedRequestsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView addressTextView, edttype, edttime,tutorname;
        TextView request;
        ImageView whatspp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameac);
            phoneTextView = itemView.findViewById(R.id.mobileac);
            addressTextView = itemView.findViewById(R.id.addresac);
            edttime = itemView.findViewById(R.id.time);
            edttype = itemView.findViewById(R.id.type);
            request = itemView.findViewById(R.id.request);
            tutorname = itemView.findViewById(R.id.tname);
            whatspp = itemView.findViewById(R.id.whatsapppp);
        }
    }
}
