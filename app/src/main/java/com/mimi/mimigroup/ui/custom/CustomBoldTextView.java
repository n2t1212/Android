package com.mimi.mimigroup.ui.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CustomBoldTextView extends AppCompatTextView {
    public CustomBoldTextView(Context context) {
        super(context);
        init();
    }

    public CustomBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/robotobold.ttf");
        setTypeface(tf);

    }
}
