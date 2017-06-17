package com.example.aneesh.simpletodo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    int mTotalCount;
    int mCompletedCount;

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setCounts(int totalCount, int completedCount)
    {
        this.mTotalCount = totalCount;
        this.mCompletedCount = completedCount;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = Math.min(getMeasuredHeight() - getPaddingTop() -getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight())/2;
        int thickness = radius/5;

        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, radius , mProgressWaitingPaint);
        canvas.drawArc(getMeasuredWidth()/2 -radius, getMeasuredHeight()/2 - radius, getMeasuredWidth()/2 + radius, getMeasuredHeight()/2 + radius, 270, (int)(360 * ((double)mCompletedCount/(double)mTotalCount)), true, mProgressCompletePaint);
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, radius - thickness, mInnerCirclePaint);

        Rect bounds = new Rect();
        String text = ""+mTotalCount;
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        textPaint.setTextSize(radius/2);
        canvas.drawText(text, getMeasuredWidth()/2, getMeasuredHeight()/2 + bounds.height()/2, textPaint);


    }

    private void init()
    {
        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCirclePaint.setColor(Color.WHITE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.GRAY);

        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        mProgressCompletePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressCompletePaint.setColor(Color.GREEN);

        mProgressWaitingPaint =  new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressWaitingPaint.setColor(Color.LTGRAY);
    }
}
