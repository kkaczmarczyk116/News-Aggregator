package com.example.newsaggregator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.type.DateTime;
import com.google.type.DateTimeOrBuilder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    ArrayList<ViewPagerItem> viewPageItemArrayList;
    Picasso picasso;


    public ViewPagerAdapter(ArrayList<ViewPagerItem> viewPageItemArrayList) {
        this.viewPageItemArrayList = viewPageItemArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        picasso = picasso.get();
        picasso.setLoggingEnabled(true);

        ViewPagerItem viewPagerItem = viewPageItemArrayList.get(position);
        if(viewPagerItem.picUrl == " "){
            holder.page_pic.setImageResource(R.drawable.noimage);
        }else{

            picasso.load(viewPagerItem.picUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.brokenimage)
                    .into(holder.page_pic);
        }


        holder.page_title.setText(viewPagerItem.title);
        holder.page_date.setText(viewPagerItem.date);
        holder.page_source.setText(viewPagerItem.source);
        holder.page_desc.setText(viewPagerItem.desc);
        holder.page_counter.setText(""+(position+1)+" of "+viewPageItemArrayList.size());
        String web = viewPageItemArrayList.get(position).pageUrl;
        holder.page_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                view.getContext().startActivity(browserIntent);

            }
        });

        holder.page_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                view.getContext().startActivity(browserIntent);
            }
        });

        holder.page_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                view.getContext().startActivity(browserIntent);
            }
        });



    }


    @Override
    public int getItemCount() {
        return viewPageItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView page_title,page_date,page_source,page_desc,page_counter;
        ImageView page_pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            page_title = itemView.findViewById(R.id.page_title);
            page_date = itemView.findViewById(R.id.page_date);
            page_source = itemView.findViewById(R.id.page_source);
            page_desc = itemView.findViewById(R.id.page_desc);
            page_pic = itemView.findViewById(R.id.page_pic);
            page_counter = itemView.findViewById(R.id.page_counter);


        }
    }
}
