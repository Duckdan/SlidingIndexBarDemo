package study.com.slidingindexbardemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2018/5/8.
 */

public class SlidingIndexBar extends View {
    //画笔
    private Paint paint;

    //索引值
    private static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    //每个单元格的宽度
    private int cellWidth;
    //每个单元格高度
    private float cellHeight;
    private Context context;
    private OnLetterUpdateListener listener;

    public void setListener(OnLetterUpdateListener listener) {
        this.listener = listener;
    }

    public interface OnLetterUpdateListener {
        void onLetterUpdate(String letter);
    }

    public SlidingIndexBar(Context context) {
        this(context, null);
    }

    public SlidingIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        //设置画笔颜色
        paint.setColor(Color.WHITE);
        //设置画笔样式
        paint.setStyle(Paint.Style.STROKE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(30);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //控件宽度
        int width = w;
        //控件高度
        int height = h;

        cellWidth = width;
        cellHeight = height * 1.0f / LETTERS.length;
    }

    private Rect bounds = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < LETTERS.length; i++) {
            //尺寸
            String letter = LETTERS[i];
            paint.getTextBounds(letter, 0, letter.length(), bounds);
            //文本高度
            int textHeight = bounds.height();
            //文本宽度
            float textWidth = paint.measureText(letter, 0, letter.length());
            float x = (cellWidth - textWidth) * 1.0f / 2;
            float y = cellHeight * 1.0f / 2 + textHeight * 1.0f / 2 + i * cellHeight;
            canvas.drawText(letter, x, y, paint);
        }
    }


    private int lastIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = -1;
        float actionY = -1;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                actionY = event.getY();
                index = (int) (actionY / cellHeight);
                if (index >= 0 && index < LETTERS.length) {
                    if (index != lastIndex) {
                        lastIndex = index;
                        String letter = LETTERS[index];
                        if (listener != null) {
                            listener.onLetterUpdate(letter);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                lastIndex = -1;
                break;
        }
        return true;
    }
}
