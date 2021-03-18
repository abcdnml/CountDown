package com.aaa.cd.ui.article;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.model.ItemClickListener;

/**
 * Description
 * Created by aaa on 2018/4/20.
 */

public class DisplayModeAdapter extends RecyclerView.Adapter<DisplayModeAdapter.DisplayViewHolder>
{
    private Context context;
    private DisplayMode currentMode;
    private Drawable listDrawable;
    private Drawable gridDrawable;
    private Drawable detailDrawable;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public DisplayModeAdapter(Context context, DisplayMode mode,ItemClickListener itemClickListener)
    {
        this.context = context;
        this.currentMode=mode;
        this.itemClickListener = itemClickListener;
        this.inflater = LayoutInflater.from(context);
        listDrawable = ContextCompat.getDrawable(context, R.drawable.selector_display_list);
        gridDrawable = ContextCompat.getDrawable(context, R.drawable.selector_display_grid);
        detailDrawable = ContextCompat.getDrawable(context, R.drawable.selector_display_detail);
    }

    @Override
    public DisplayModeAdapter.DisplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.item_markdown_display, parent, false);
        DisplayViewHolder holder = new DisplayViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DisplayModeAdapter.DisplayViewHolder holder, final int position)
    {

        Drawable drawable;
        if (position == DisplayMode.MODE_GRID.getMode())
        {
            drawable = gridDrawable;
            holder.tv_mode.setText(R.string.display_grid);
        } else if (position ==  DisplayMode.MODE_DETAIL.getMode())
        {
            drawable = detailDrawable;
            holder.tv_mode.setText(R.string.display_detail);
        } else
        {
            drawable = listDrawable;
            holder.tv_mode.setText(R.string.display_list);
        }
        drawable.setBounds(0, 0, 48, 48);
        if (currentMode.getMode() == position)
        {
            holder.tv_mode.setBackgroundColor(Color.CYAN);
        } else
        {
            holder.tv_mode.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
        holder.tv_mode.setCompoundDrawables(drawable, null, null, null);
        holder.tv_mode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (itemClickListener != null)
                {
                    currentMode=DisplayMode.getDisplay(position);
                    notifyDataSetChanged();
                    itemClickListener.onItemClick(position ,v);
                }
            }
        });

    }

    public void setCurrentMode(DisplayMode displayMode)
    {
        currentMode = displayMode;
        notifyDataSetChanged();
    }
    public DisplayMode getCurrentMode(){
        return currentMode;
    }

    @Override
    public int getItemCount()
    {
        return 3;
    }

    static class DisplayViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_mode;

        public DisplayViewHolder(View itemView)
        {
            super(itemView);
            tv_mode = (TextView) itemView.findViewById(R.id.rv_item_tv_markdown_display);
        }
    }
}
