package com.app.fyp.educatorapp.Adpater;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.fyp.educatorapp.Model.ServiceModel;
import com.app.fyp.educatorapp.R;
import com.app.fyp.educatorapp.User.allrates;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AlllistRecyclerView extends RecyclerView.Adapter<AlllistRecyclerView.RecyclerViewHolder> {
    private Context mContext;
    private List<ServiceModel> serviceModels;
    private OnItemClickListener mListener;

    public AlllistRecyclerView(Context context, List<ServiceModel> uploads) {
        mContext = context;
        serviceModels = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_row, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ServiceModel productModel = serviceModels.get(position);
        holder.nameTextView.setText(productModel.getName());
        holder.addresstext.setText(productModel.getAddress());
        holder.mobile.setText(productModel.getMobile());
        //holder.mobile.setText(getDateToday());
        Picasso.with(mContext)
                .load(productModel.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .fit()
                .centerCrop()
                .into(holder.teacherImageView);
    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView nameTextView, addresstext, mobile, seetext;
        public RatingBar ratingBar;
        public ImageView teacherImageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            addresstext = itemView.findViewById(R.id.addresstext);
            mobile = itemView.findViewById(R.id.serviceTextView);
            teacherImageView = itemView.findViewById(R.id.teacherImageView);
            ratingBar=itemView.findViewById(R.id.rate);
            seetext=itemView.findViewById(R.id.seeall);

            seetext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, allrates.class);
                    mContext.startActivity(intent);
                }
            });

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem showItem = menu.add(Menu.NONE, 1, 1, "Show");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");
            MenuItem editItem = menu.add(Menu.NONE, 3, 3, "Update");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
            editItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                        case 3:
                            mListener.onEditItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onShowItemClick(int position);

        void onDeleteItemClick(int position);

        void onEditItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private String getDateToday() {
        //  DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String today = dateFormat.format(date);
        return today;
    }
}
