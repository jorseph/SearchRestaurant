package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jorseph on 2017/7/11.
 */

public class PageThreeView extends PageView{
    public PageThreeView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.page_content, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText("Page three");
        addView(view);
    }

    @Override
    public void refreshView() {

    }
}
