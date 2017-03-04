package com.example.quyet.podomoro.decoration;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by quyet on 3/4/2017.
 */

public class PSquareRelativeLayout extends RelativeLayout {
    public PSquareRelativeLayout(Context context) {
        super(context);
    }

    public PSquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PSquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
