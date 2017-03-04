package com.example.quyet.podomoro.decoration;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by quyet on 3/4/2017.
 */

public class LSquareRelativeLayout extends RelativeLayout {
    public LSquareRelativeLayout(Context context) {
        super(context);
    }

    public LSquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LSquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = height;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
