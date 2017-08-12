package com.example.ian.rentermanager11;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * ListAdapter
 * Created by XiaoWei on 2015-11-14.
 */
public class ListAdapter extends BaseListAdapter<String> {

    public ListAdapter(Context context, List<String> objects) {
        super(context, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (getItemViewType(position) == 0) {
                convertView = ((Activity) (mContext)).getLayoutInflater().inflate(R.layout.layout_has_no_data, parent, false);
                holder.noDataRootLayout =  convertView.findViewById(R.id.root_layout);
            } else {
                convertView = ((Activity) (mContext)).getLayoutInflater().inflate(R.layout.item_house, parent, false);
                holder.textView =  convertView.findViewById(R.id.text_view);

            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (hasNoData) {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(getScreenWidth(), getScreenHeight() * 2 / 3);
            holder.noDataRootLayout.setLayoutParams(lp);
        } else {
            holder.textView.setText(mDataList.get(position));

        }

        return convertView;
    }

    private static final class ViewHolder {
        TextView textView;

        LinearLayout noDataRootLayout;
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetric = Resources.getSystem().getDisplayMetrics();
        return displayMetric.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetric = Resources.getSystem().getDisplayMetrics();
        return displayMetric.heightPixels;
    }

}
