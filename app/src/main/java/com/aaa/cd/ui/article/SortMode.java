package com.aaa.cd.ui.article;

public enum SortMode
{
    SORT_ALPHA_ASC(0),
    SORT_ALPHA_DESC(1),
    SORT_CREATE_TIME_ASC(2),
    SORT_CREATE_TIME_DESC(3),
    SORT_MODIFY_TIME_ASC(4),
    SORT_MODIFY_TIME_DESC(5),
    SORT_SIZE_ASC(6),
    SORT_SIZE_DESC(7);
    private int mode;

    SortMode(int mode)
    {
        this.mode = mode;
    }

    public int getMode()
    {
        return mode;
    }
    public static SortMode getSort(int mode){
        switch (mode)
        {
            case 0:
                return SORT_ALPHA_ASC;
            case 1:
                return SORT_ALPHA_DESC;
            case 2:
                return SORT_CREATE_TIME_ASC;
            case 3:
                return SORT_CREATE_TIME_DESC;
            case 4:
                return SORT_MODIFY_TIME_ASC;
            case 5:
                return SORT_MODIFY_TIME_DESC;
            case 6:
                return SORT_SIZE_ASC;
            case 7:
                return SORT_SIZE_DESC;
            default:
                return SORT_ALPHA_ASC;
        }
    }

}
