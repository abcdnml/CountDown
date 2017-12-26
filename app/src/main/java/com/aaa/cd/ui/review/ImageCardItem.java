package com.aaa.cd.ui.review;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.bumptech.glide.Glide;

/**
 * Created by wensefu on 17-3-4.
 */
public class ImageCardItem extends BaseCardItem
{

    private static final String TAG = "ImageCardItem";

    private String url;
    private String label;

    public ImageCardItem(Context context, String url, String label)
    {
        super(context);
        this.url = url;
        this.label = label;
    }


    @Override
    public View getView(View convertView, ViewGroup parent)
    {
        convertView = View.inflate(mContext, R.layout.item_imagecard, null);
        ImageView imageView = Utils.findViewById(convertView, R.id.image);
        TextView labelview = Utils.findViewById(convertView, R.id.label);
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.img_dft)
                .centerCrop()
                .crossFade()
                .into(imageView);
        labelview.setText(label);
        return convertView;
    }
}
