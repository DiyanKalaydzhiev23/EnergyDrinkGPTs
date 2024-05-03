package com.example.enrgydrinksgpts.utils.shadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ShadowFrameLayout extends FrameLayout {
    private Paint shadowPaint;
    private RectF shadowRect;
    private float cornerRadius = 50; // Default corner radius that matches your CardView

    public ShadowFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initShadow();
    }

    public void setCornerRadius(float radius) {
        this.cornerRadius = radius;
        invalidate(); // Redraw the view with the new corner radius
    }

    private void initShadow() {
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setShadowLayer(10, 0, 5, Color.WHITE); // Smaller shadow spread
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
        shadowRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Adjust padding if needed to prevent clipping of the shadow
        shadowRect.set(10, 10, w - 10, h - 10); // Update rect size to have padding for shadows
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(Color.WHITE); // Set the shadow color
        // Draw a rounded rectangle that matches the corners of the CardView
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        super.dispatchDraw(canvas);
    }
}
