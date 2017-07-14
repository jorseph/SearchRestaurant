package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by jorseph on 2017/7/13.
 */

public class SelectiveViewPager extends ViewPager {
    private boolean paging = true;

    public SelectiveViewPager(Context context) {
        super(context);
    }

    public SelectiveViewPager(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (paging) {
            Log.v("hi","hi");
            return super.onInterceptTouchEvent(e);
        }

        return false;
    }

    public void setPaging(boolean p){ paging = p; }

}

