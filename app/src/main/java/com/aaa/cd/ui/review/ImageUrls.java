package com.aaa.cd.ui.review;

/**
 * Created by wensefu on 2017/2/12.
 */

public class ImageUrls {

    public static final String[] images = {
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517317807119&di=075c28bc43712fef01a226a29519f3a2&imgtype=0&src=http%3A%2F%2Fimg2.niutuku.com%2F1312%2F0827%2F0827-niutuku.com-13638.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517317840219&di=f5566fe19420fd2f3bb50f276937f52c&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F7c1ed21b0ef41bd5af9a14e85bda81cb38db3de4.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517317866882&di=6d1c86782bf169c20170c3a45697c7a7&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0bd162d9f2d3572ca2f8c0f08113632762d0c380.jpg",
    };

    public static final String[] labels;


    static {
        labels = new String[images.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = "图片 " + (i + 1);
        }
    }
}
