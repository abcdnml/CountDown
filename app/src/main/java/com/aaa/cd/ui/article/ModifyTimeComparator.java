package com.aaa.cd.ui.article;

import com.aaa.cd.po.Catalogue;

import java.util.Comparator;

/**
 * Description
 * Created by aaa on 2018/4/25.
 */

public class ModifyTimeComparator implements Comparator<Catalogue>
{
    boolean isASC=true;
    public ModifyTimeComparator(boolean isASC)
    {
        this.isASC=isASC;
    }

    @Override
    public int compare(Catalogue c1, Catalogue c2)
    {
        int t1=c1.getType();
        int t2=c2.getType();
        if(t1==0&&t2!=0)
        {
            return -1;
        }else if(t2==0&&t1!=0)
        {
            return 1;
        } else
        {
            if (c1.getModifyTime() > c2.getModifyTime())
            {
                return isASC?1:-1;
            } else if (c1.getModifyTime() < c2.getModifyTime())
            {
                return isASC?-1:1;
            } else
            {
                return 0;
            }
        }
    }
}
