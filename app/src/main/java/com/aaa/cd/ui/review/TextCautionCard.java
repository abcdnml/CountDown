package com.aaa.cd.ui.review;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.bumptech.glide.Glide;

/**
 * Description
 * Created by aaa on 2018/1/30.
 */

public class TextCautionCard extends BaseCardItem
{
    private static final String TAG = "ImageCardItem";

    private String text;

    public TextCautionCard(Context context, String  text)
    {
        super(context);
        this.text = text;
    }


    @Override
    public View getView(View convertView, ViewGroup parent)
    {
        convertView = View.inflate(mContext, R.layout.item_text_caution, null);
        TextView cautionText = Utils.findViewById(convertView, R.id.tv_caution_text);
        cautionText.setText(text);
        return convertView;
    }
}
