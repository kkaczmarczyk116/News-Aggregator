package com.example.newsaggregator;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;




import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
