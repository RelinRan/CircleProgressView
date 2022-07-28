package androidx.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 圈圈进度控件
 * 圆圈进度控件支持进度显示，修改进度背景色
 * 和进度颜色、圆圈边框宽度、圆圈半径大小
 */
public class CircleProgressView extends View {

    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 中心坐标
     */
    private float circleX, circleY;
    /**
     * 半径
     */
    private float progressRadius = 0;
    /**
     * 进度背景颜色
     */
    private int progressBackgroundColor = Color.parseColor("#F2F2F2");
    /**
     * 进度颜色
     */
    private int progressColor = Color.parseColor("#73B2FF");
    /**
     * 进度
     */
    private int progress = 65;
    /**
     * 进度最大值
     */
    private int max = 100;
    /**
     * 进度宽度
     */
    private float progressStrokeWidth = dip(20);
    /**
     * 进度开始角度
     */
    private float progressStartAngle = -90;
    /**
     * 中间进度值可见
     */
    private int progressTextVisibility = View.VISIBLE;
    /**
     * 中间进度值文字大小
     */
    private float progressTextSize = dip(14);
    /**
     * 中间进度值颜色
     */
    private int progressTextColor = Color.parseColor("#222222");
    /**
     * 是否帽圆
     */
    private boolean strokeCapRound = false;


    public CircleProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        progress = array.getInt(R.styleable.CircleProgressView_progress, progress);
        max = array.getInt(R.styleable.CircleProgressView_max, max);
        progressTextVisibility = array.getInt(R.styleable.CircleProgressView_progressTextVisibility, progressTextVisibility);
        progressTextSize = array.getDimension(R.styleable.CircleProgressView_progressTextSize, progressTextSize);
        progressTextColor = array.getColor(R.styleable.CircleProgressView_progressTextColor, progressTextColor);
        progressColor = array.getColor(R.styleable.CircleProgressView_progressColor, progressColor);
        progressBackgroundColor = array.getColor(R.styleable.CircleProgressView_progressBackgroundColor, progressBackgroundColor);
        progressStartAngle = array.getFloat(R.styleable.CircleProgressView_progressStartAngle, progressStartAngle);
        progressStrokeWidth = array.getDimension(R.styleable.CircleProgressView_progressStrokeWidth, progressStrokeWidth);
        progressRadius = array.getDimension(R.styleable.CircleProgressView_progressRadius, 0);
        strokeCapRound = array.getBoolean(R.styleable.CircleProgressView_strokeCapRound, strokeCapRound);



    }

    private float dip(int value) {
        return Resources.getSystem().getDisplayMetrics().density * value;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        circleX = getMeasuredWidth() / 2F;
        circleY = getMeasuredHeight() / 2F;
        boolean isHorizontal = circleX > circleY;
        float radius;
        if (progressRadius == 0) {
            radius = isHorizontal ? circleY : circleX;
        } else {
            float limit = isHorizontal ? circleY : circleX;
            radius = progressRadius >= limit ? limit : progressRadius;
        }
        float paddingVertical = getPaddingLeft() + getPaddingRight();
        float paddingHorizontal = getPaddingTop() + getPaddingBottom();
        float paddingSum = isHorizontal ? paddingVertical : paddingHorizontal;
        progressRadius = radius - paddingSum - progressStrokeWidth/4;
        progressStrokeWidth = progressStrokeWidth >= radius/2 ? radius/2 : progressStrokeWidth;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(progressStrokeWidth);
        paint.setColor(progressBackgroundColor);
        canvas.drawCircle(circleX, circleY, progressRadius, paint);
        paint.setColor(progressColor);
        if (strokeCapRound) {
            paint.setStrokeCap(Paint.Cap.ROUND);
        }
        RectF rectF = new RectF(circleX - progressRadius, circleY - progressRadius, circleX + progressRadius, circleY + progressRadius);
        canvas.drawArc(rectF, progressStartAngle, 360 * progress / max, false, paint);
        //百分比
        if (progressTextVisibility == View.VISIBLE) {
            String progressText = 100 * progress / max + "%";
            paint.setColor(progressTextColor);
            Paint paint = new Paint();
            paint.setColor(progressTextColor);
            paint.setTextSize(progressTextSize);
            Rect rect = new Rect();
            paint.getTextBounds(progressText, 0, progressText.length(), rect);
            int w = rect.width();
            int h = rect.height();
            canvas.drawText(progressText, circleX - (w / 2), circleY + (h / 2), paint);
        }
    }

    /**
     * 获取圆心 -x
     *
     * @return
     */
    public float getCircleX() {
        return circleX;
    }

    /**
     * 设置圆心 -x
     *
     * @param circleX
     */
    public void setCircleX(float circleX) {
        this.circleX = circleX;
        invalidate();
    }

    /**
     * 获取圆心 -y
     *
     * @return
     */
    public float getCircleY() {
        return circleY;
    }

    /**
     * 设置圆心 - y
     *
     * @param circleY
     */
    public void setCircleY(float circleY) {
        this.circleY = circleY;
        invalidate();
    }

    /**
     * 获取进度半径
     *
     * @return
     */
    public float getProgressRadius() {
        return progressRadius;
    }

    /**
     * 设置进度半径
     *
     * @param progressRadius
     */
    public void setProgressRadius(float progressRadius) {
        this.progressRadius = progressRadius;
        invalidate();
    }

    /**
     * 获取进度半径
     *
     * @return
     */
    public float getProgressStartAngle() {
        return progressStartAngle;
    }

    /**
     * 设置开始角度
     *
     * @param progressStartAngle
     */
    public void setProgressStartAngle(float progressStartAngle) {
        this.progressStartAngle = progressStartAngle;
        invalidate();
    }

    /**
     * 获取进度背景颜色
     *
     * @return
     */
    public int getProgressBackgroundColor() {
        return progressBackgroundColor;
    }

    /**
     * 设置进度背景颜色
     *
     * @param progressBackgroundColor
     */
    public void setProgressBackgroundColor(int progressBackgroundColor) {
        this.progressBackgroundColor = progressBackgroundColor;
        invalidate();
    }

    /**
     * 获取进度颜色
     *
     * @return
     */
    public int getProgressColor() {
        return progressColor;
    }

    /**
     * 设置进度背景颜色
     *
     * @param progressColor
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    /**
     * 获取进度
     *
     * @return
     */
    public float getProgress() {
        return progress;
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    /**
     * 获取进度宽度
     *
     * @return
     */
    public float getProgressStrokeWidth() {
        return progressStrokeWidth;
    }

    /**
     * 设置进度宽度
     *
     * @param progressStrokeWidth
     */
    public void setProgressStrokeWidth(float progressStrokeWidth) {
        this.progressStrokeWidth = progressStrokeWidth;
        invalidate();
    }

    /**
     * 获取最大进度
     *
     * @return
     */
    public float getMax() {
        return max;
    }

    /**
     * 设置最大进度
     *
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    /**
     * 获取进度文字显示属性
     *
     * @return
     */
    public int getProgressTextVisibility() {
        return progressTextVisibility;
    }

    /**
     * 设置文字显示属性
     *
     * @param progressTextVisibility
     */
    public void setProgressTextVisibility(int progressTextVisibility) {
        this.progressTextVisibility = progressTextVisibility;
        invalidate();
    }

    /**
     * 获取文字大小
     *
     * @return
     */
    public float getProgressTextSize() {
        return progressTextSize;
    }

    /**
     * 设置进度文字大小
     *
     * @param progressTextSize
     */
    public void setProgressTextSize(float progressTextSize) {
        this.progressTextSize = progressTextSize;
        invalidate();
    }

    /**
     * 获取进度文字颜色
     *
     * @return
     */
    public int getProgressTextColor() {
        return progressTextColor;
    }

    /**
     * 设置进度文字颜色
     *
     * @param progressTextColor
     */
    public void setProgressTextColor(int progressTextColor) {
        this.progressTextColor = progressTextColor;
        invalidate();
    }

    /**
     * @return 是否帽圆
     */
    public boolean isStrokeCapRound() {
        return strokeCapRound;
    }

    /**
     * 设置是否帽圆
     *
     * @param strokeCapRound
     */
    public void setStrokeCapRound(boolean strokeCapRound) {
        this.strokeCapRound = strokeCapRound;
        invalidate();
    }

}

