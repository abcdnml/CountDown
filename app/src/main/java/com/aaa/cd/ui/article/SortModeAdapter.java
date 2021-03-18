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

public class SortModeAdapter extends RecyclerView.Adapter<SortModeAdapter.SortViewHolder>
{
    private Context context;
    private SortMode sortMode;
    private Drawable sortCreateTimeASC;
    private Drawable sortCreateTimeDESC;
    private Drawable sortModifyTimeASC;
    private Drawable sortModifyTimeDESC;
    private Drawable sortAlphaASC;
    private Drawable sortAlphaDESC;
    private Drawable sortSizeASC;
    private Drawable sortSizeDESC;
    private LayoutInflater inflater;
    ItemClickListener itemClickListener;

    public SortModeAdapter(Context context,SortMode mode, ItemClickListener itemClickListener)
    {
        this.context = context;
        this.sortMode=mode;
        this.itemClickListener = itemClickListener;
        this.inflater = LayoutInflater.from(context);
        sortCreateTimeASC = ContextCompat.getDrawable(context, R.drawable.selector_sort_create_time_asc);
        sortCreateTimeDESC = ContextCompat.getDrawable(context, R.drawable.selector_sort_create_time_desc);
        sortModifyTimeASC = ContextCompat.getDrawable(context, R.drawable.selector_sort_modify_time_asc);
        sortModifyTimeDESC = ContextCompat.getDrawable(context, R.drawable.selector_sort_modify_time_desc);
        sortAlphaASC = ContextCompat.getDrawable(context, R.drawable.selector_sort_alpha_asc);
        sortAlphaDESC = ContextCompat.getDrawable(context, R.drawable.selector_sort_alpha_desc);
        sortSizeASC = ContextCompat.getDrawable(context, R.drawable.selector_sort_size_asc);
        sortSizeDESC = ContextCompat.getDrawable(context, R.drawable.selector_sort_size_desc);
    }

    @Override
    public SortViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.item_markdown_sort, parent, false);
        SortViewHolder holder = new SortViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SortViewHolder holder, final int position)
    {

        Drawable drawable;
        SortMode mode=SortMode.getSort(position);
        switch (mode)
        {
            case SORT_ALPHA_ASC:
                drawable = sortAlphaASC;
                holder.tv_mode.setText(R.string.sort_alpha_asc);
                break;
            case SORT_ALPHA_DESC:
                drawable = sortAlphaDESC;
                holder.tv_mode.setText(R.string.sort_alpha_desc);
                break;
            case SORT_CREATE_TIME_ASC:
                drawable = sortCreateTimeASC;
                holder.tv_mode.setText(R.string.sort_create_time_asc);
                break;
            case SORT_CREATE_TIME_DESC:
                drawable = sortCreateTimeDESC;
                holder.tv_mode.setText(R.string.sort_create_time_desc);
                break;
            case SORT_MODIFY_TIME_ASC:
                drawable = sortModifyTimeASC;
                holder.tv_mode.setText(R.string.sort_modify_time_asc);
                break;
            case SORT_MODIFY_TIME_DESC:
                drawable = sortModifyTimeDESC;
                holder.tv_mode.setText(R.string.sort_modify_time_desc);
                break;
            case SORT_SIZE_ASC:
                drawable = sortSizeASC;
                holder.tv_mode.setText(R.string.sort_size_asc);
                break;
            case SORT_SIZE_DESC:
                drawable = sortSizeDESC;
                holder.tv_mode.setText(R.string.sort_size_desc);
                break;
            default:
                drawable = sortAlphaASC;
                holder.tv_mode.setText(R.string.sort_alpha_asc);
                break;
        }
        if (sortMode.getMode() == position)
        {
            holder.tv_mode.setBackgroundColor(Color.CYAN);
        }else{
            holder.tv_mode.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

        drawable.setBounds(0, 0, 48, 48);
        holder.tv_mode.setCompoundDrawables(drawable, null, null, null);
        holder.tv_mode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (itemClickListener != null)
                {
                    sortMode =SortMode.getSort(position);
                    notifyDataSetChanged();
                    itemClickListener.onItemClick(position,v);
                }
            }
        });

    }

    public void setSortMode(SortMode sortMode)
    {
        this.sortMode=sortMode;
    }
    public SortMode getSortMode(){
        return sortMode;
    }

    @Override
    public int getItemCount()
    {
        return 8;
    }

    static class SortViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_mode;

        public SortViewHolder(View itemView)
        {
            super(itemView);
            tv_mode = (TextView) itemView.findViewById(R.id.rv_item_tv_markdown_sort);
        }
    }
}
