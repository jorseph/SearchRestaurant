package com.example.currentplacedetailsonmap.PageViewFile;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Created by jorseph on 2017/7/11.
 */

public abstract class PageView extends RelativeLayout{
    public PageView(Context context) {
        super(context);
    }
    public abstract void refreshView();
}
