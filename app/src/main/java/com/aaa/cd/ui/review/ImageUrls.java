package com.aaa.cd.ui.review;

/**
 * Created by wensefu on 2017/2/12.
 */

public class ImageUrls {

    public static final String[] images = {
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1520575447497&di=401f896f440f1d57f463b2e9b43a854a&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F4%2F57b6a7f82383b.jpg",
            "https://timgsa.baidu.com/https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1520575746518&di=2eb78b6e606c36eb062b589b6d05f558&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fa%2F55d417c5c0cb6.jpg",
            "https://timgsa.baidu.com/https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1520575779028&di=056626f4e96ce04a18d6ffe4f8a7973a&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F17%2F36%2F08%2F09T58PIC4JM_1024.jpg",
    };

    public static final String[] labels;


    static {
        labels = new String[images.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = "图片 " + (i + 1);
        }
    }
}
