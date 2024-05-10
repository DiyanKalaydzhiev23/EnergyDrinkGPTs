package com.example.enrgydrinksgpts.utils.shadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.enrgydrinksgpts.R;

public class ShadowFrameLayout extends FrameLayout {
    private Paint shadowPaint;
    private RectF shadowRect;
    private float cornerRadius = 50;

    public ShadowFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initShadow();
    }

    public void setCornerRadius(float radius) {
        this.cornerRadius = radius;
        invalidate();
    }

    private void initShadow() {
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int shadowColor = getResources().getColor(R.color.shadow);
        shadowPaint.setShadowLayer(10, 0, 5, shadowColor);

        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
        shadowRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        shadowRect.set(10, 10, w, h - 10);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int shadowColor = getResources().getColor(R.color.shadow);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(shadowColor);
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        super.dispatchDraw(canvas);
    }
}
