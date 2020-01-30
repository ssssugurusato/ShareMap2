package com.example.sharemap2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class EditListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int layoutID;
    private ArrayList<Marker> mMarkerList;


    static class ViewHolder {
        TextView text;
    }

    EditListAdapter(Context context, int itemLayoutId,
                    ArrayList<Marker> MarkerList){
        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;
        mMarkerList= MarkerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(mMarkerList.get(position).getTitle());
        return convertView;
    }

    @Override
    public int getCount() {
        return mMarkerList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
