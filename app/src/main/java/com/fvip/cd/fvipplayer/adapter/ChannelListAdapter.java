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

/**
 * Created by cd on 2018/7/19.
 */

@SuppressWarnings("NullableProblems")
public class ChannelListAdapter extends ArrayAdapter<PlaylistBean.ListBean> {


    private int resourceId;
    private final List<PlaylistBean.ListBean> datas;

    public ChannelListAdapter(Context context, int textViewResourceId, List<PlaylistBean.ListBean> data) {
        super(context, textViewResourceId, data);
        datas = data;
        resourceId = textViewResourceId;
    }


    @SuppressWarnings("NullableProblems")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PlaylistBean.ListBean channelListBean = getItem(position);
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
        viewHolder.tvPlatform.setText(channelListBean.getName());

        return convertView;
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public PlaylistBean.ListBean getItem(int position) {
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
