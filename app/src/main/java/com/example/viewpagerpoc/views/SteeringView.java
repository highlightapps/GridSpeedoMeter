package com.example.viewpagerpoc.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.viewpagerpoc.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class SteeringView extends View {

    private static final AtomicBoolean drawing = new AtomicBoolean(false);
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static int parentWidth = 0;
    private static int parentHeight = 0;
    private static Matrix matrix = null;
    private static Bitmap bitmap = null;

    private float steeringAngle = 0;

    public SteeringView(Context context) {
        super(context);

        initialize();
    }

    public SteeringView(Context context, AttributeSet attr) {
        super(context, attr);

        initialize();
    }

    private void initialize() {
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.steering);
    }

    public void setSteeringAngle(float steeringAngle) {
        this.steeringAngle = steeringAngle;
        postInvalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentWidth, parentHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) throw new NullPointerException();

        if (!drawing.compareAndSet(false, true)) return;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        //if (bitmap.getWidth() > canvasWidth || bitmap.getHeight() > canvasHeight) {
        if (bitmap.getHeight() > canvasWidth) {
            // Resize the bitmap to the size of the canvas
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmapWidth * .25), (int) (bitmapHeight * .25), true);
        }

        int bitmapX = bitmap.getWidth() / 2;
        int bitmapY = bitmap.getHeight() / 2;

        int parentX = parentWidth / 2;
        int parentY = parentHeight / 2;

        int centerX = parentX - bitmapX;
        int centerY = parentY - bitmapY;

        int rotation = (int) (steeringAngle) ;

        matrix.reset();
        // Rotate the bitmap around it's center point so it's always pointing north
        matrix.setRotate(rotation, bitmapX, bitmapY);
        // Move the bitmap to the center of the canvas
        matrix.postTranslate(centerX, centerY);

        canvas.drawBitmap(bitmap, matrix, paint);


        paint.setColor(Color.parseColor("#FFFFFF00"));
        canvas.drawRect(new Rect(canvasWidth/2 - 50, canvasHeight/2 -((canvasHeight/100) * 20)-50 , canvasWidth/2 + 50, canvasHeight/2 - ((canvasHeight/100) * 20)+50), paint);

        paint.setColor(Color.parseColor("#FFFF0000"));
        paint.setTextSize(50f);
        canvas.drawText(rotation + "°", canvasWidth/2 - 25, canvasHeight/2 - ((canvasHeight/100) * 20) + 25 , paint);

        drawing.set(false);
    }

}
