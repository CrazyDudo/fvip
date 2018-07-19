package com.fvip.cd.fvipplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fvip.cd.fvipplayer.R;
import com.fvip.cd.fvipplayer.bean.PlaylistBean;

import java.util.List;


@SuppressWarnings("NullableProblems")
public class PlatformListAdapter extends ArrayAdapter<PlaylistBean.PlatformlistBean> {


    private int resourceId;
    private final List<PlaylistBean.PlatformlistBean> datas;

    public PlatformListAdapter(Context context, int textViewResourceId, List<PlaylistBean.PlatformlistBean> data) {
        super(context, textViewResourceId, data);
        datas = data;
        resourceId = textViewResourceId;
    }


    @SuppressWarnings("NullableProblems")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PlaylistBean.PlatformlistBean platformListBean = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvPlatform = convertView.findViewById(R.id.tv_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //noinspection ConstantConditions
        viewHolder.tvPlatform.setText(platformListBean.getName());

        return convertView;
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public PlaylistBean.PlatformlistBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView tvPlatform;
    }

}
