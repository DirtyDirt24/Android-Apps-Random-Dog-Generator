package com.example.tugasspecial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private String[] list;
    private String[] img;
    private LayoutInflater inflater;

    public GridAdapter(Context context, String[] list, String[] img) {
        this.context = context;
        this.list = list;
        this.img = img;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String error_msg = "Error!";

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.gridImage);
            holder.textView = convertView.findViewById(R.id.itemName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(list[position]);
        Picasso.get().load(img[position]).into(holder.imageView);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
