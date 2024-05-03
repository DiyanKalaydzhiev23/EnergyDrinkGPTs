package com.example.enrgydrinksgpts.utils.shadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.example.enrgydrinksgpts.R;

public class ShadowFrameLayout extends FrameLayout {
    private Paint shadowPaint;
    private RectF shadowRect;
    private float cornerRadius = 40;

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
        shadowPaint.setShadowLayer(20, 5, 5, ContextCompat.getColor(this.getContext(), R.color.shadow));
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
        shadowRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        shadowRect.set(15, 15, w - 15, h - 10);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setShader(null);
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        super.dispatchDraw(canvas);
    }
}
