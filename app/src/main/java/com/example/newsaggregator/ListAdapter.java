package com.example.newsaggregator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<SourcesObj> {

    private Context mContext;
    int mResource;

    public ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SourcesObj> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView nametxt = (TextView) convertView.findViewById(R.id.text1);
        nametxt.setText(name);
        return convertView;

    }
}
