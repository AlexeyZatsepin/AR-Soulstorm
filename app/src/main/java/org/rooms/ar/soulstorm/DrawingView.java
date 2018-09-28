package org.rooms.ar.soulstorm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class DrawingView extends View {
    private static final int STROKE_WIDTH = 200;
    private Paint paint = new Paint();
    private Path path = new Path();

    private List<OnTouchListener> mListeners = new ArrayList<>();

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(STROKE_WIDTH);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
    }

    public void addOnTouchListener(OnTouchListener listener) {
        mListeners.add(listener);
    }

    public void removeOnTouchListener(OnTouchListener listener) {
        mListeners.remove(listener);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        path.moveTo(0,0);
        path.lineTo(1,1);
        path.moveTo(getWidth(),getHeight());
        path.lineTo(getWidth()-1,getHeight()-1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        mListeners.forEach(listener -> listener.onTouch(this, event));
        invalidate();
        return true;
    }
}
