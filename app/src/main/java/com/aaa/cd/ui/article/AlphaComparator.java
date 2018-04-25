package com.aaa.cd.ui.article;

import com.aaa.cd.po.Catalogue;

import java.util.Comparator;

/**
 * Description
 * Created by aaa on 2018/4/25.
 */

public class AlphaComparator implements Comparator<Catalogue>
{
    boolean isASC = true;

    public AlphaComparator(boolean isASC)
    {
        this.isASC = isASC;
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
        }else{
            if (c1.getSortLetter().equals("@") || c2.getSortLetter().equals("#"))
            {
                return -1;
            } else if (c1.getSortLetter().equals("#")|| c2.getSortLetter().equals("@"))
            {
                return 1;
            } else
            {
                int i=c1.getSortLetter().compareTo(c2.getSortLetter());
                return isASC?i:-i;
            }
        }
/*        if (t1 < t1)
        {
            //如果文件类型为文件夹 那么 文件夹优先排列
            return -1;
        } else
        {
            if (c1.getSortLetter().equals("@") || c2.getSortLetter().equals("#"))
            {
                return -1;
            } else if (c1.getSortLetter().equals("#")|| c2.getSortLetter().equals("@"))
            {
                return 1;
            } else
            {
                int i=c1.getSortLetter().compareTo(c2.getSortLetter());
                return isASC?i:-i;
            }
        }*/
    }
}
