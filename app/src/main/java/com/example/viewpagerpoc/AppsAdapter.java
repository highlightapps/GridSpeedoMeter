package com.example.viewpagerpoc;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AppsAdapter extends BaseAdapter {

    private final Context mContext;

    ArrayList<AppsModel> appsModelArrayList;

    public AppsAdapter(Context mContext, ArrayList<AppsModel> appsModelArrayList) {
        this.mContext = mContext;
        this.appsModelArrayList = appsModelArrayList;
    }

    @Override
    public int getCount() {
        return appsModelArrayList.size();
    }

    @Override
    public AppsModel getItem(int i) {
        return appsModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolderItem viewHolder;

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.apps_grid_row_item, viewGroup, false);
            viewHolder = new ViewHolderItem();
            viewHolder.imageViewItem = (ImageView) convertView.findViewById(R.id.android_gridview_image);
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.android_gridview_text);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        AppsModel appsModel = appsModelArrayList.get(position);
        viewHolder.textViewItem.setText(appsModel.getAppName());
        viewHolder.imageViewItem.setImageResource(appsModel.getAppIcon());

        return convertView;
    }

    static class ViewHolderItem {
        ImageView imageViewItem;
        TextView textViewItem;
    }
}
