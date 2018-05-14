package com.aaa.cd.ui.article;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.po.Catalogue;
import com.aaa.cd.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Description
 * Created by aaa on 2018/4/2.
 */

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.CatalogueViewHolder>
{
    private DisplayMode displayMode;
    private SortMode sortMode;
    private LayoutInflater inflater;
    List<Catalogue> list;
    List<Catalogue> showList;
    static Drawable fileDrawable;
    static Drawable directoryDrawable;
    static Comparator<Catalogue> comparator;
    private int MODE_NORMAL = 0;
    private int MODE_SELECT_FOLDER = 1;
    private int mode = MODE_NORMAL;

    public CatalogueAdapter(Context context, List<Catalogue> list)
    {
        this(context, DisplayMode.MODE_LIST, SortMode.SORT_ALPHA_ASC, list, null);
    }

    public CatalogueAdapter(Context context, DisplayMode displayMode, List<Catalogue> list)
    {
        this(context, displayMode, SortMode.SORT_ALPHA_ASC, list, null);
    }

    public CatalogueAdapter(Context context, SortMode sortMode, List<Catalogue> list)
    {
        this(context, DisplayMode.MODE_LIST, sortMode, list, null);
    }

    public CatalogueAdapter(Context context, DisplayMode displayMode, SortMode sortMode, List<Catalogue> list, ItemClickListener itemClickListener)
    {
        this.inflater = LayoutInflater.from(context);
        this.displayMode = displayMode;
        this.list = list;

        this.mOnItemClickListener = itemClickListener;
        if (this.list == null)
        {
            this.list = new ArrayList<>();
        }
        this.setSortMode(sortMode);
        fileDrawable = ContextCompat.getDrawable(context, R.mipmap.file);
        directoryDrawable = ContextCompat.getDrawable(context, R.mipmap.directory);
    }

    ItemClickListener mOnItemClickListener;

    public interface ItemClickListener
    {
        void onItemClick(Catalogue catalogue);

        void onItemLongClick(Catalogue catalogue);
    }


    public void setOnItemClickLitsener(ItemClickListener mOnItemClickLitsener)
    {
        mOnItemClickListener = mOnItemClickLitsener;
    }

    public void setFunctionMode(int mode)
    {
        if (mode == MODE_NORMAL)
        {
            this.showList = list;
            this.mode = mode;
            notifyDataSetChanged();
        } else if (mode == MODE_SELECT_FOLDER)
        {
            showList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++)
            {
                if (list.get(i).getType() == Catalogue.FOLDER)
                {
                    showList.add(list.get(i));
                }
            }
            this.mode = mode;
            notifyDataSetChanged();
        }
    }

    public void setSortMode(SortMode sort)
    {
        LogUtil.i("setSortMode : " + sort.getMode());
        sortMode = sort;
        sort();
        setFunctionMode(mode);
    }

    public void sort()
    {
        switch (sortMode)
        {
            case SORT_ALPHA_ASC:
                comparator = new AlphaComparator(true);
                break;
            case SORT_ALPHA_DESC:
                comparator = new AlphaComparator(false);
                break;
            case SORT_CREATE_TIME_ASC:
                comparator = new CreateTimeComparator(true);
                LogUtil.i("setSortMode : SORT_CREATE_TIME_ASC");
                break;
            case SORT_CREATE_TIME_DESC:
                comparator = new CreateTimeComparator(false);
                LogUtil.i("setSortMode : SORT_CREATE_TIME_DESC");
                break;
            case SORT_MODIFY_TIME_ASC:
                comparator = new ModifyTimeComparator(true);
                break;
            case SORT_MODIFY_TIME_DESC:
                comparator = new ModifyTimeComparator(false);
                break;
            case SORT_SIZE_ASC:
                comparator = new SizeComparator(true);
                break;
            case SORT_SIZE_DESC:
                comparator = new SizeComparator(false);
                break;
            default:
                comparator = new AlphaComparator(true);
                break;
        }

        Collections.sort(list, comparator);
        notifyDataSetChanged();
    }


    public void updateList(List<Catalogue> list)
    {
        this.list = list;
        sort();
        setFunctionMode(mode);
    }

    @Override
    public CatalogueViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        CatalogueViewHolder holder = null;
        Log.i("CatalogueAdapter", "onCreateViewHolder displayMode" + displayMode);
        switch (displayMode)
        {
            case MODE_GRID:
                view = inflater.inflate(R.layout.rv_item_catalogue_grid, parent, false);
                holder = new CatalogueGridViewHolder(view);
                break;
            case MODE_LIST:
                view = inflater.inflate(R.layout.rv_item_catalogue_list, parent, false);
                holder = new CatalogueListViewHolder(view);
                break;
            case MODE_DETAIL:
                view = inflater.inflate(R.layout.rv_item_catalogue_detail, parent, false);
                holder = new CatalogueDetailViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.rv_item_catalogue_grid, parent, false);
                holder = new CatalogueGridViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CatalogueViewHolder holder, final int position)
    {
        final Catalogue catalogue = showList.get(position);
        Log.i("CatalogueAdapter", "onBindViewHolder list postion : " + position);
        if (catalogue.getType() == Catalogue.FOLDER)
        {
            holder.iv_icon.setImageDrawable(directoryDrawable);
        } else
        {
            holder.iv_icon.setImageDrawable(fileDrawable);
        }
        holder.tv_name.setText(catalogue.getName());

        if (holder.tv_detail != null)
        {
            if (displayMode == DisplayMode.MODE_LIST)
            {
                if (catalogue.getType() == Catalogue.FOLDER)
                {
                    holder.tv_detail.setText("(" + catalogue.getSubItem() + ")");
                } else
                {
                    holder.tv_detail.setText("");
                }
            } else if (displayMode == DisplayMode.MODE_DETAIL)
            {
                holder.tv_detail.setText(catalogue.getContent());
            }
        }

        holder.vg_parent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mOnItemClickListener.onItemClick(catalogue);
            }
        });
        holder.vg_parent.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                mOnItemClickListener.onItemLongClick(catalogue);
                return true;
            }
        });
    }


    public Catalogue getItem(int position)
    {
        return showList.get(position);
    }

    @Override
    public int getItemCount()
    {
        Log.i("CatalogueAdapter", "getItemCount : " + showList.size());
        return showList.size();
    }

    static class CatalogueViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_detail;
        ViewGroup vg_parent;

        public CatalogueViewHolder(View itemView)
        {
            super(itemView);
        }
    }

    static class CatalogueListViewHolder extends CatalogueViewHolder
    {
        public CatalogueListViewHolder(View itemView)
        {
            super(itemView);
            vg_parent = (ViewGroup) itemView.findViewById(R.id.ll_rv_list_catalogue);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_rv_list_catalogue_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_rv_list_catalogue_name);
            tv_detail = (TextView) itemView.findViewById(R.id.tv_rv_list_catalogue_detail);
        }
    }

    static class CatalogueGridViewHolder extends CatalogueViewHolder
    {
        public CatalogueGridViewHolder(View itemView)
        {
            super(itemView);
            vg_parent = (ViewGroup) itemView.findViewById(R.id.ll_rv_grid_catalogue);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_rv_grid_catalogue_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_rv_grid_catalogue_name);
        }
    }

    static class CatalogueDetailViewHolder extends CatalogueViewHolder
    {
        public CatalogueDetailViewHolder(View itemView)
        {
            super(itemView);
            vg_parent = (ViewGroup) itemView.findViewById(R.id.rl_rv_detail_catalogue);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_rv_detail_catalogue_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_rv_detail_catalogue_name);
            tv_detail = (TextView) itemView.findViewById(R.id.tv_rv_detail_catalogue_detail);
        }
    }
}
