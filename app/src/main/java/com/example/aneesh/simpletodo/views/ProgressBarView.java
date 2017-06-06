package com.example.aneesh.simpletodo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Aneesh on 6/6/2017.
 */

public class ProgressBarView extends View {

    Paint mInnerCirclePaint;
    Paint mProgressCompletePaint;
    Paint mProgressWaitingPaint;
    Paint textPaint;

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, 100, mProgressWaitingPaint);
        canvas.drawArc(getMeasuredWidth()/2 -100, getMeasuredHeight()/2 - 100, getMeasuredWidth()/2 + 100, getMeasuredHeight()/2 + 100, 270, 360, true, mProgressCompletePaint);
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, 80, mInnerCirclePaint);

    }

    private void init()
    {
        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCirclePaint.setColor(Color.WHITE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.DKGRAY);

        mProgressCompletePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressCompletePaint.setColor(Color.BLUE);

        mProgressWaitingPaint =  new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressWaitingPaint.setColor(Color.DKGRAY);
    }
}
