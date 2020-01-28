package com.example.sharemap2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EditListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int layoutID;
    private String[] namelist;


    static class ViewHolder {
        TextView text;
    }

    EditListAdapter(Context context, int itemLayoutId,
                String[] names){

        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;

        namelist = names;
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



        holder.text.setText(namelist[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        return namelist.length;
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
