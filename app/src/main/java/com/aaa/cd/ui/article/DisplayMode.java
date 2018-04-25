package com.aaa.cd.ui.article;

public enum DisplayMode
{
    MODE_LIST(0), MODE_GRID(1), MODE_DETAIL(2);
    private int mode;

    DisplayMode(int mode)
    {
        this.mode = mode;
    }

    public int getMode()
    {
        return mode;
    }

    public static DisplayMode getDisplay(int mode)
    {
        switch (mode)
        {
            case 0:
                return MODE_LIST;
            case 1:
                return MODE_GRID;
            case 2:
                return MODE_DETAIL;
            default:
                return MODE_LIST;
        }
    }
}
