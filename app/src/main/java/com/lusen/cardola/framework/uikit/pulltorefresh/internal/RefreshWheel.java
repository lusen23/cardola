package com.lusen.cardola.framework.uikit.pulltorefresh.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.util.DisplayUtil;

/**
 *
 * Created by ak
 */
public class RefreshWheel extends View {

    private Paint progressPaint = new Paint();
    private RectF progressBounds = new RectF();
    private static final float DEFAULT_SIZE = 40.0f;
    private int progress;
    private int size;
    private int barWidth = 3;
    private int rimWidth = 3;


    public RefreshWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        size = DisplayUtil.dip2px((int)DEFAULT_SIZE);
        setupPaint();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupBounds();
    }

    private void setupBounds(){
        int paddingTop = this.getPaddingTop();
        int paddingBottom = this.getPaddingBottom();
        int paddingLeft = this.getPaddingLeft();
        int paddingRight = this.getPaddingRight();
        progressBounds = new RectF(paddingLeft + barWidth + rimWidth,
                paddingTop + barWidth + rimWidth,
                this.getLayoutParams().width - paddingRight - barWidth - rimWidth,
                this.getLayoutParams().height - paddingBottom - barWidth - rimWidth);
    }

    private void setupPaint(){
        progressPaint.setColor(getResources().getColor(R.color.pull_to_refresh_text));
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(barWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        progressPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(progressBounds, -90, progress, false, progressPaint);
//        canvas.drawArc(progressBounds, 0, 360 * progress / MAX_PROGRESS, false, progressPaint);
    }

    protected void setProgress(int progress){
        this.progress = progress;
        postInvalidate();
    }
}
